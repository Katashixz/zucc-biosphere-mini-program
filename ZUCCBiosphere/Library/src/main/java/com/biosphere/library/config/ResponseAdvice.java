package com.biosphere.library.config;

import com.biosphere.library.vo.RespBeanEnum;
import com.biosphere.library.vo.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;

/**
 * @Author Administrator
 * @Date 2022/11/18 9:51
 * @Version 1.0
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {


    /* 选择哪些类，或哪些方法需要走beforeBodyWrite
     * 从arg0中可以获取方法名和类名
     * arg0.getMethod().getDeclaringClass().getName()为获取方法名
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ResponseResult){
            return body;
        }
        if (body instanceof LinkedHashMap){
            return body;
        }

        return ResponseResult.success(body);
    }
}
