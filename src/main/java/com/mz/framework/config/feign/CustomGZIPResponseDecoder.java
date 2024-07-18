package com.mz.framework.config.feign;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.cloud.openfeign.encoding.HttpEncoding;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class CustomGZIPResponseDecoder implements Decoder {

    final Decoder delegate;

    public CustomGZIPResponseDecoder(Decoder delegate) {
        Objects.requireNonNull(delegate, "Decoder must not be null. ");
        this.delegate = delegate;
    }

    private static boolean isCompressed(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }

    public static Response getDecompressedResponse(Response response, byte[] compressed) throws IOException {
        final StringBuilder output = new StringBuilder();
        final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            output.append(line);
        }
        return response.toBuilder().body(output.toString().getBytes()).build();
    }

    public static String getDecompressedResponseAsString(byte[] compressed) throws IOException {
        final StringBuilder output = new StringBuilder();
        final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            output.append(line);
        }
        return output.toString();
    }

    private Object getObject(Type type, Response response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (response.status() == 404 || response.status() == 204)
            return Util.emptyValueOf(type);
        if (Objects.isNull(response.body()))
            return null;
        if (byte[].class.equals(type))
            return Util.toByteArray(response.body().asInputStream());
        if (isParameterizeHttpEntity(type)) {
            type = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (type instanceof Class || type instanceof ParameterizedType
                    || type instanceof WildcardType) {
                @SuppressWarnings({"unchecked", "rawtypes"})
                HttpMessageConverterExtractor<?> extractor = new HttpMessageConverterExtractor(
                        type, Collections.singletonList(new MappingJackson2HttpMessageConverter(mapper)));
                Object decodedObject = extractor.extractData(new FeignResponseAdapter(response));
                return createResponse(decodedObject, response);
            }
            throw new DecodeException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "type is not an instance of Class or ParameterizedType: " + type, response.request());
        } else if (isHttpEntity(type)) {
            return delegate.decode(response, type);
        } else if (String.class.equals(type)) {
            String responseValue = Util.toString(response.body().asReader());
            return StringUtils.isEmpty(responseValue) ? Util.emptyValueOf(type) : responseValue;
        } else {
            String s = Util.toString(response.body().asReader());
            JavaType javaType = TypeFactory.defaultInstance().constructType(type);
            return !StringUtils.isEmpty(s) ? mapper.readValue(s, javaType) : Util.emptyValueOf(type);
        }
    }

    private boolean isParameterizeHttpEntity(Type type) {
        if (type instanceof ParameterizedType) {
            return isHttpEntity(((ParameterizedType) type).getRawType());
        }
        return false;
    }

    private boolean isHttpEntity(Type type) {
        if (type instanceof Class) {
            Class c = (Class) type;
            return HttpEntity.class.isAssignableFrom(c);
        }
        return false;
    }

    private <T> ResponseEntity<T> createResponse(Object instance, Response response) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        for (String key : response.headers().keySet()) {
            headers.put(key, new LinkedList<>(response.headers().get(key)));
        }

        return new ResponseEntity<>((T) instance, headers, HttpStatus.valueOf(response
                .status()));
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        Collection<String> values = response.headers().get(HttpEncoding.CONTENT_ENCODING_HEADER);
        if (Objects.nonNull(values) && !values.isEmpty() && values.contains(HttpEncoding.GZIP_ENCODING)) {
            byte[] compressed = Util.toByteArray(response.body().asInputStream());
            if ((compressed == null) || (compressed.length == 0)) {
                return delegate.decode(response, type);
            }
            //decompression part
            //after decompress we are delegating the decompressed response to default
            //decoder
            if (isCompressed(compressed)) {
                Response uncompressedResponse = getDecompressedResponse(response, compressed);
                return getObject(type, uncompressedResponse);
            } else {
                return getObject(type, response);
            }
        } else {
            return getObject(type, response);
        }
    }

    private class FeignResponseAdapter implements ClientHttpResponse {

        private final Response response;

        private FeignResponseAdapter(Response response) {
            this.response = response;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.valueOf(this.response.status());
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.response.status();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.response.reason();
        }

        @Override
        public void close() {
            try {
                this.response.body().close();
            } catch (IOException ex) {
                // Ignore exception on close...
            }
        }

        @Override
        public InputStream getBody() throws IOException {
            return this.response.body().asInputStream();
        }

        @Override
        public HttpHeaders getHeaders() {
            return getHttpHeaders(this.response.headers());
        }

        private HttpHeaders getHttpHeaders(Map<String, Collection<String>> headers) {
            HttpHeaders httpHeaders = new HttpHeaders();
            for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
                httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return httpHeaders;
        }
    }
}

