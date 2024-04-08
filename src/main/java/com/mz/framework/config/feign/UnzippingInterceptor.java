package com.mz.framework.config.feign;

import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;
import okio.GzipSource;
import okio.Okio;

import java.io.IOException;

/**
 * @author yangh
 * @since 2021-10-21 23:23
 */
@Slf4j
public class UnzippingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request build = chain.request().newBuilder().build();
        Response response = chain.proceed(build);
        Response unzip = unzip(response);
        return unzip;
    }

    // copied from okhttp3.internal.http.HttpEngine (because is private)
    private Response unzip(final Response response) throws IOException {
        if (response.body() == null) {
            return response;
        }

        //check if we have gzip response
        String contentEncoding = response.headers().get("Content-Encoding");
        if (StringUtil.isEmpty(contentEncoding)) {
            contentEncoding = response.headers().get("content-encoding");
        }
        log.info("==============contentEncoding: " + contentEncoding);

        //this is used to decompress gzipped responses
        if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
            log.info("如果对内容进行了压缩，则解压");

            Long contentLength = response.body().contentLength();
            GzipSource responseBody = new GzipSource(response.body().source());
            Headers strippedHeaders = response.headers().newBuilder().build();
            return response.newBuilder().headers(strippedHeaders)
                    .body(new RealResponseBody(response.body().contentType().toString(), contentLength, Okio.buffer(responseBody)))
                    .build();
        } else {
            return response;
        }
    }
}

