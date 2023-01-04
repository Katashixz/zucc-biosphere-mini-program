package com.biosphere.library.config;

import com.biosphere.library.vo.ExceptionLogVo;
import com.biosphere.library.vo.ExceptionNoLogVo;
import com.biosphere.library.vo.RespBeanEnum;
import com.biosphere.library.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author Administrator
 * @Date 2022/11/18 10:43
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * 默认全局异常处理。
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult exception(Exception e) {
        log.error("全局异常捕获:{}", e.getMessage(), e);
        return ResponseResult.error(RespBeanEnum.ERROR);
    }
    /**
     * 自定义全局异常处理。
     */
    @ExceptionHandler(ExceptionLogVo.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult exceptionLogVo(ExceptionLogVo e) {
        log.error("自定义异常捕获:{}", e.getE().getMessage(), e);
        if (Objects.isNull(e.getErrorData()))
            return ResponseResult.error(e.getE());
        return ResponseResult.error(e.getE(), e.getErrorData());
    }

    /**
     * 捕获像STAR_REPEAT这种不需要打印的异常。
     */
    @ExceptionHandler(ExceptionNoLogVo.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult exceptionNoLogVo(ExceptionNoLogVo e) {
        // log.error("自定义异常捕获:{}", e.getE().getMessage());
        if (Objects.isNull(e.getErrorData()))
            return ResponseResult.error(e.getE());
        return ResponseResult.error(e.getE(), e.getErrorData());
    }

    /**
     * 参数异常加入全局异常处理器。
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseResult handleValidatedException(Exception e) {
        ResponseResult res = null;

        if (e instanceof MethodArgumentNotValidException) {
            // BeanValidation exception
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            res = ResponseResult.error(RespBeanEnum.ERROR_INFO,
                    ex.getBindingResult().getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; "))
            );
        } else if (e instanceof ConstraintViolationException) {
            // BeanValidation GET simple param
            ConstraintViolationException ex = (ConstraintViolationException) e;
            res = ResponseResult.error(RespBeanEnum.ERROR_INFO,
                    ex.getConstraintViolations().stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining("; "))
            );
        } else if (e instanceof BindException) {
            // BeanValidation GET object param
            BindException ex = (BindException) e;
            res = ResponseResult.error(RespBeanEnum.ERROR_INFO,
                    ex.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; "))
            );
        }

        return res;
    }
}
