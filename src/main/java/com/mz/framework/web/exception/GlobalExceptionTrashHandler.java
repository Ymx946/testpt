package com.mz.framework.web.exception;

import com.mz.common.context.CommonContext;
import com.mz.common.exception.ObjectEmptyException;
import com.mz.common.exception.ServiceException;
import com.mz.common.util.ResponseCodeTrash;
import com.mz.common.util.Result;
import com.mz.common.util.ResultTrash;
import com.mz.framework.annotation.TrashRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
 * 全局异常处理(trash)
 */
@RestControllerAdvice(annotations = TrashRestController.class)
@ResponseBody
public class GlobalExceptionTrashHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Http请求消息序列化异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResultTrash messageExceptionHandler(HttpMessageNotReadableException e) {
        log.warn("http请求参数转换异常: "+ e.getMessage());
        return ResultTrash.failed("http请求参数转换异常: "+ e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResultTrash exceptionHandler(HttpServletRequest request, Exception e) {
        String errmsg = "当前网络状况不佳，请稍后再试。";
        log.error("GlobalExceptionHandler#exceptionHandler ... msg -> {}", e);
        this.handleErrorInfo(CommonContext.getRequest(), errmsg);
        StringWriter sw = new StringWriter();
//        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        String url = this.getUrl(request);
        return ResultTrash.instance(ResponseCodeTrash.FAILED.getErrCode(), errmsg, exceptionAsString);
    }

    @ExceptionHandler(SQLException.class)
    public ResultTrash exceptionHandler(SQLException e) {
        return ResultTrash.instance(ResponseCodeTrash.SUCCESS.getErrCode(), e.getMessage(), e);
    }

//    @ExceptionHandler(EmptyException.class)
//    public Result exceptionHandler(EmptyException e) {
//        return Result.instance(ResponseCode.success.getCode(), e.getMessage(), new PageInfo<>(Collections.emptyList()));
//    }

    @ExceptionHandler(ObjectEmptyException.class)
    public ResultTrash exceptionHandler(ObjectEmptyException e) {
        return ResultTrash.instance(ResponseCodeTrash.SUCCESS.getErrCode(), e.getMessage(), Collections.emptyList());
    }

    @ExceptionHandler(ServiceException.class)
    public ResultTrash serviceExceptionHandler(ServiceException e) {
        this.handleErrorInfo(CommonContext.getRequest(), e.getMessage());
        return ResultTrash.instance(ResponseCodeTrash.SUCCESS.getErrCode(), e.getMessage(), e.getData());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResultTrash handleConstraintViolationException(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return ResultTrash.failed(msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultTrash handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return ResultTrash.failed(msg);
    }

    @ExceptionHandler(BindException.class)
    public ResultTrash handleBindException(BindException e) {
        String msg = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return ResultTrash.failed(msg);
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
