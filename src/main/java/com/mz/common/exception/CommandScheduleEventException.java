package com.mz.common.exception;

/**
 * 指令生成异常
 *
 * @author xiao
 */
public class CommandScheduleEventException extends RuntimeException {

    public CommandScheduleEventException() {

    }

    public CommandScheduleEventException(String message) {
        super(message);
    }
}
