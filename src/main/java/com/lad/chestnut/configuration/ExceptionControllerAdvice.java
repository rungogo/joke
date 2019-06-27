package com.lad.chestnut.configuration;

import com.alibaba.fastjson.JSONObject;
import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 处理异常
 *
 * @author lad
 * @date 2019/4/28
 *
 */
@RestController
@ControllerAdvice
public class ExceptionControllerAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseData businessExceptionHandler(BusinessException e, HttpServletRequest request) {
        ResponseData responseData = new ResponseData(e.getResultInfo());
        LOGGER.error("{},result:{}", request.getServletPath(), JSONObject.toJSONString(responseData), e);
        return responseData;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseData noHandlerFoundHandler(NoHandlerFoundException e, HttpServletRequest request) {
        ResponseData responseData = new ResponseData(ResponseEnum.NO_HANDLER_FOUND);
        LOGGER.error("{},result:{}", request.getServletPath(), JSONObject.toJSONString(responseData), e);
        return responseData;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseData requestMethodNotSupportedHandler(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        ResponseData responseData =  new ResponseData(ResponseEnum.REQUEST_METHOD_NOT_SUPPORTED);
        LOGGER.error("{},result:{}", request.getServletPath(), JSONObject.toJSONString(responseData), e);
        return responseData;
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseData servletRequestBindingExceptionHandler(ServletRequestBindingException e, HttpServletRequest request) {
        ResponseData responseData = new ResponseData(ResponseEnum.REQUIRED_PARAMETER_LACK);
        LOGGER.error("{},result:{}", request.getServletPath(), JSONObject.toJSONString(responseData), e);
        return responseData;
    }

    @ExceptionHandler(BindException.class)
    public ResponseData bindExceptionHandler(BindException e, HttpServletRequest request) {
        ResponseData responseData = new ResponseData(ResponseEnum.PARAMETER_FORMAT_ERROR);
        LOGGER.error("{},result:{}", request.getServletPath(), JSONObject.toJSONString(responseData), e);
        return responseData;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseData methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        ResponseData responseData = new ResponseData(ResponseEnum.PARAMETER_FORMAT_ERROR);
        LOGGER.error("{},result:{}", request.getServletPath(), JSONObject.toJSONString(responseData), e);
        return responseData;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseData httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e, HttpServletRequest request) {
        ResponseData responseData = new ResponseData(ResponseEnum.PARAMETER_FORMAT_ERROR);
        LOGGER.error("{},result:{}", request.getServletPath(), JSONObject.toJSONString(responseData), e);
        return responseData;
    }

    @ExceptionHandler(IOException.class)
    public ResponseData ioExceptionHandler(IOException e, HttpServletRequest request) {
        ResponseData responseData = new ResponseData(ResponseEnum.IO_EXCEPTION);
        String uri = request.getRequestURI();
        LOGGER.error("{},result:{}", uri, JSONObject.toJSONString(responseData), e);
        return responseData;
    }

    @ExceptionHandler(Exception.class)
    public ResponseData exceptionHandler(Exception e, HttpServletRequest request) {
        ResponseData responseData = ResponseData.fail();
        String uri = request.getRequestURI();
        LOGGER.error("{},result:{}", uri, JSONObject.toJSONString(responseData), e);
        return responseData;
    }
}
