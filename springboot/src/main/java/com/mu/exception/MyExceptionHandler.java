package com.mu.exception;

import com.mu.common.Constants;
import com.mu.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {
    /**
     * 如果抛出的是service异常，调用该方法
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handler(ServiceException se){
        return Result.error(se.getCode(),se.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Result handler(UnauthorizedException e){
        return Result.error(Constants.CODE_401,"无访问权限");
    }

    @ExceptionHandler(ExpiredCredentialsException.class)
    @ResponseBody
    public Result handler(ExpiredCredentialsException e){
        return Result.error(Constants.CODE_401,"登录已过期，请重新登录");
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public Result handler(UnauthenticatedException e){
        log.error("运行时异常",e);
        return Result.error(Constants.CODE_401,"没有权限");
    }

}
