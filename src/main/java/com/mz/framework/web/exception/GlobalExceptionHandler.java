package com.mz.framework.web.exception;

import com.mz.common.context.CommonContext;
import com.mz.common.exception.ObjectEmptyException;
import com.mz.common.exception.ServiceException;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import org.apache.commons.fileupload.FileUploadBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author cqh
 * @date 2020/5/5 10:06 上午
 */
//@ControllerAdvice
@RestControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Http请求消息序列化异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result messageExceptionHandler(HttpMessageNotReadableException e) {
        log.warn("http请求参数转换异常: "+ e.getMessage());
        return Result.failed("http请求参数转换异常: "+ e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest request, Exception e) {
        String msg = "当前网络状况不佳，请稍后再试。";
        log.error("GlobalExceptionHandler#exceptionHandler ... msg -> {}", e);
        this.handleErrorInfo(CommonContext.getRequest(), msg);
        StringWriter sw = new StringWriter();
//        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        String url = this.getUrl(request);
        return Result.instance(ResponseCode.FAILED.getCode(), msg, exceptionAsString);
    }

    @ExceptionHandler(SQLException.class)
    public Result exceptionHandler(SQLException e) {
        return Result.instance(ResponseCode.SUCCESS.getCode(), e.getMessage(), e);
    }

//    @ExceptionHandler(EmptyException.class)
//    public Result exceptionHandler(EmptyException e) {
//        return Result.instance(ResponseCode.success.getCode(), e.getMessage(), new PageInfo<>(Collections.emptyList()));
//    }

    @ExceptionHandler(ObjectEmptyException.class)
    public Result exceptionHandler(ObjectEmptyException e) {
        return Result.instance(ResponseCode.SUCCESS.getCode(), e.getMessage(), Collections.emptyList());
    }

    @ExceptionHandler(ServiceException.class)
    public Result serviceExceptionHandler(ServiceException e) {
        this.handleErrorInfo(CommonContext.getRequest(), e.getMessage());
        return Result.instance(e.getCode(), e.getMessage(), e.getData());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return Result.failed(msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return Result.failed(msg);
    }

    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        String msg = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return Result.failed(msg);
    }

    /**
     * 处理文件上传大小超限制
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MultipartException.class)
    public Result fileUploadExceptionHandler(MultipartException exception) {
        Throwable throwable = exception.getRootCause();
        if (throwable instanceof FileUploadBase.FileSizeLimitExceededException) {
            log.error("上传文件过大", exception);
            return Result.failed("上传文件过大");
        }
        if (throwable instanceof FileUploadBase.SizeLimitExceededException) {
            log.error("总上传文件过大", exception);
            return Result.failed("总上传文件过大");
        }
        log.error("上传文件异常", exception);
        return Result.failed("上传文件异常");
    }
    private void handleErrorInfo(HttpServletRequest request, String message) {
        String url = this.getUrl(request);
        log.error("GlobalExceptionHandler#handleErrorInfo ... WEB 全局异常处理器 message=[{}] -> {}", message, url);
    }

    private String getUrl(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> params = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String paraName = parameterNames.nextElement();
            String value = request.getParameter(paraName);
            params.put(paraName, value);
        }
        // 服务器地址
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI() + " -> " + "请求方式: " + request.getMethod() + " -> " + "请求参数: " + params;
    }

}
