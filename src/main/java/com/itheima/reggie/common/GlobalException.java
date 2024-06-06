package com.itheima.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalException {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.error(e.getMessage());
        if(e.getMessage().contains("Duplicate entry")){
            String[] split = e.getMessage().split(" ");
           String msg =  split[2] + "已存在";
           return R.error(msg);
        }
        return R.error("未知错误！");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException e){
        log.error(e.getMessage());
//        if(e.getMessage().contains("Duplicate entry")){
//            String[] split = e.getMessage().split(" ");
//            String msg =  split[2] + "已存在";
//            return R.error(msg);
//        }
        return R.error(e.getMessage());
    }

}
