package com.mu.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//包装类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;//  标志请求是否成功
    private String msg;//原因
    private Object data;

    //如果请求成功，返回成功信息
    //没有数据的成功
    public static Result success(){
        return new Result(Constants.CODE_200,"",null);
    }
    //有数据的成功
    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data);
    }
    //错误
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }

    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);
    }
}
