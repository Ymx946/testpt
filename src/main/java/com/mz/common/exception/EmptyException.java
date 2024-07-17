package com.mz.common.exception;

import com.mz.common.util.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author laughdie
 * @date 2020-06-05 21:37
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmptyException extends RuntimeException {

    private Integer code = ResponseCode.FAILED.getCode();

    public EmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyException(String message) {
        super(message);
    }

    public EmptyException(Throwable cause) {
        super(cause);
    }

    public EmptyException(Integer code, String message) {
        super(message);
        if (code != null) {
            this.code = code;
        }
    }

}
