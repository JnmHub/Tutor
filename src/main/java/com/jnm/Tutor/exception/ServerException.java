package com.jnm.Tutor.exception;

import com.jnm.Tutor.model.enums.ErrorEnum;


public class ServerException extends DefaultException{
    public ServerException() {}

    public ServerException(ErrorEnum errorEnum) {
        this(errorEnum.getCode(), errorEnum.getMsg());
    }

    public ServerException(int code, String massage) {
        super(code, massage);
    }

    public ServerException(String message) {
        super(400, message);
    }
}
