package com.mz.framework.web.util;

import cn.hutool.core.util.StrUtil;
import com.mz.common.exception.EmptyException;
import com.mz.common.exception.ObjectEmptyException;
import com.mz.common.exception.ServiceException;
import com.mz.common.util.Result;
import com.mz.framework.web.exception.GlobalExceptionHandler;

import java.util.Collection;

/**
 * @author laughdie
 * @date 2020-06-05 21:42
 */
public class AssertUtil {

    /**
     * 抛业务异常提示
     *
     * @param flag 如果是true
     * @param msg  此参数为空, 则返回空集合
     * @see GlobalExceptionHandler#serviceExceptionHandler(ServiceException e)
     */
    public static void isTrue(boolean flag, String msg) {
        if (flag) {
            throw new ServiceException(msg);
        }
    }

    /**
     * 抛业务异常提示
     *
     * @param flag 如果是true
     * @param msg  此参数为空, 则返回空集合
     * @return Result
     */
    public static Result isTrueResult(boolean flag, String msg) {
        if (flag) {
            return Result.failed(msg);
        }
        return null;
    }

    /**
     * 抛业务异常提示
     *
     * @param flag 如果是false
     * @param msg  此参数为空, 则返回空集合
     * @see GlobalExceptionHandler#serviceExceptionHandler(ServiceException e)
     */
    public static void isFalse(boolean flag, String msg) {
        isTrue(!flag, msg);
    }

    /**
     * 抛业务异常提示
     *
     * @param flag 如果是false
     * @param msg  此参数为空, 则返回空集合
     * @see GlobalExceptionHandler#serviceExceptionHandler(ServiceException e)
     */
    public static Result isFalseResult(boolean flag, String msg) {
        return isTrueResult(!flag, msg);
    }

    /**
     * 抛空异常提示
     *
     * @param str 此参数为空, 则返回空集合
     * @see GlobalExceptionHandler#exceptionHandler(javax.servlet.http.HttpServletRequest, Exception)
     */
    public static void empty(String str, String msg) {
        if (StrUtil.isBlank(str)) {
            throw new EmptyException(msg);
        }
    }

    /**
     * 抛空异常提示
     *
     * @param obj 此参数为空, 则返回空集合
     * @see GlobalExceptionHandler#exceptionHandler(EmptyException e)
     */
    public static void empty(Object obj, String msg) {
        if (null == obj) {
            throw new ObjectEmptyException(msg);
        }
    }

    /**
     * 抛空异常提示
     *
     * @param collection 此参数为空, 则返回空集合
     * @param msg        提示信息
     * @see GlobalExceptionHandler#exceptionHandler(EmptyException e)
     */
    public static void empty(Collection<?> collection, String msg) {
        if (collection == null || collection.isEmpty()) {
            throw new EmptyException(msg);
        }
    }

    /**
     * 抛业务异常提示
     *
     * @param obj 为空抛业务异常提示
     * @param msg 提示信息
     * @see GlobalExceptionHandler#serviceExceptionHandler(ServiceException e)
     */
    public static void isObjEmpty(Object obj, String msg) {
        if (null == obj) {
            throw new ServiceException(msg);
        }
    }

    /**
     * 抛业务异常提示
     *
     * @param obj 为空抛业务异常提示
     * @param msg 提示信息
     * @return Result
     */
    public static Result isObjEmptyResult(Object obj, String msg) {
        if (null == obj) {
            return Result.failed(msg);
        }
        return null;
    }

    /**
     * 抛业务异常提示
     *
     * @param obj 为空抛业务异常提示
     * @param msg 提示信息
     * @see GlobalExceptionHandler#serviceExceptionHandler(ServiceException e)
     */
    public static void isNotObjEmpty(Object obj, String msg) {
        if (null != obj) {
            throw new ServiceException(msg);
        }
    }

    /**
     * 抛业务异常提示
     *
     * @param obj 为空抛业务异常提示
     * @param msg 提示信息
     * @see GlobalExceptionHandler#serviceExceptionHandler(ServiceException e)
     */
    public static Result isNotObjEmptyResult(Object obj, String msg) {
        if (null != obj) {
            return Result.failed(msg);
        }
        return null;
    }

    /**
     * 抛业务异常提示
     *
     * @param str 为空抛业务异常提示
     * @param msg 提示信息
     * @see GlobalExceptionHandler#serviceExceptionHandler(ServiceException e)
     */
    public static void isBlank(String str, String msg) {
        if (StrUtil.isBlank(str)) {
            throw new ServiceException(msg);
        }
    }

    /**
     * 抛业务异常提示
     *
     * @param str 为空抛业务异常提示
     * @param msg 提示信息
     * @return Result
     */
    public static Result isBlankResult(String str, String msg) {
        if (StrUtil.isBlank(str)) {
            return Result.failed(msg);
        }
        return null;
    }

    /**
     * 抛业务异常提示
     *
     * @param str 不为空抛业务异常提示
     * @param msg 提示信息
     * @see GlobalExceptionHandler#serviceExceptionHandler(ServiceException e)
     */
    public static void isNotBlank(String str, String msg) {
        if (!StrUtil.isBlank(str)) {
            throw new ServiceException(msg);
        }
    }

    /**
     * 抛业务异常提示
     *
     * @param collection 为空抛业务异常提示
     * @param msg        提示信息
     * @see GlobalExceptionHandler#serviceExceptionHandler(ServiceException e)
     */
    public static void isCollectionEmpty(Collection<?> collection, String msg) {
        if (collection == null || collection.isEmpty()) {
            throw new ServiceException(msg);
        }
    }

    /**
     * 抛业务异常提示
     *
     * @param collection 为空抛业务异常提示
     * @param msg        提示信息
     */
    public static Result isCollectionEmptyResult(Collection<?> collection, String msg) {
        if (collection == null || collection.isEmpty()) {
            return Result.failed(msg);
        }
        return null;
    }

}
