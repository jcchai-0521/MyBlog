package cn.com.jchen.blog.common.exception;

import cn.com.jchen.blog.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
{
    /**
     * 全局异常处理，针对RuntimeException
     * @param e 异常对象
     * @return 封装后的异常信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e)
    {
        log.error("GlobalExceptionHandler.handler RuntimeException.{}", e);
        return Result.fail(e.getMessage());
    }

    /**
     * shiro异常处理
     * @param e 异常对象
     * @return 封装后的异常信息
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e)
    {
        log.error("GlobalExceptionHandler.handler ShiroException.{}", e);
        return Result.fail(401, e.getMessage());
    }

    /**
     * 实体类校验异常（valid异常）
     * @param e 异常对象
     * @return 封装后的异常信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e)
    {
        log.error("GlobalExceptionHandler.handler MethodArgumentNotValidException.{}", e);
        // 将异常校验信息细化处理
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

        return Result.fail(401, objectError.getDefaultMessage());
    }

    /**
     * 断言异常
     * @param e 异常对象
     * @return 封装后的异常信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e)
    {
        log.error("GlobalExceptionHandler.handler IllegalArgumentException.{}", e);
        // 将异常校验信息细化处理

        return Result.fail(401, e.getMessage());
    }



}
