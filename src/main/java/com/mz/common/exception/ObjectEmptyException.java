package com.mz.common.exception;

import com.mz.common.util.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author laughdie
 * @date 2020-06-22 20:18
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ObjectEmptyException extends RuntimeException {

    private  Integer code = ResponseCode.SUCCESS.getCode();

    public ObjectEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ObjectEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectEmptyException(String message) {
        super(message);
    }

    public ObjectEmptyException(Throwable cause) {
        super(cause);
    }

    public ObjectEmptyException(Integer code, String message) {
        super(message);
        if (code != null) {
            this.code = code;
        }
    }
    
}
