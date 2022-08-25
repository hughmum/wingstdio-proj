package com.mu.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{
    private String code;

    public ServiceException(String code,String msg){
        super(msg);//继承
        this.code = code;
    }
}
