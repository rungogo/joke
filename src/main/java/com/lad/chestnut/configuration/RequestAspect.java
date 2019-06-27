package com.lad.chestnut.configuration;

import com.alibaba.fastjson.JSONObject;
import com.lad.chestnut.annotation.IgnoreTokenValidate;
import com.lad.chestnut.annotation.WebLogController;
import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.ResponseEnum;
import com.lad.chestnut.common.Token;
import com.lad.chestnut.constant.Constant;
import com.lad.chestnut.dao.UserDao;
import com.lad.chestnut.pojo.model.User;
import com.lad.chestnut.util.EncryptDecode;
import com.lad.chestnut.util.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author lad
 * @date 2019/4/28
 */
@Component
@Aspect
public class RequestAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestAspect.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private EncryptDecode encryptDecode;

    @Autowired
    private UserDao userDao;

    @Pointcut("within(com.lad.chestnut.controller.*)")
    public void requestPointcut() {
    }

    @Around("requestPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        Method method = getMethod(joinPoint);

        // 解密token
        Token token = new Token();
        if (method.getAnnotation(IgnoreTokenValidate.class) == null) {
            token = parseTokenAndValidate();
        }

        // 校验参数
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult errorResult = (BindingResult) arg;
                if (errorResult.hasErrors()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (ObjectError allError : errorResult.getAllErrors()) {
                        stringBuilder.append(allError.getDefaultMessage());
                    }
                    return new ResponseData(ResponseEnum.PARAMETER_ERROR, stringBuilder);
                }
            }
        }

        Object returnValue = proceed(joinPoint, args, token);
        // 打印日志
        WebLogController webLogController = method.getAnnotation(WebLogController.class);
        if (webLogController != null) {
            LOGGER.info("URL                :{}", request.getServletPath());
            LOGGER.info("Description        :{}", webLogController.description());
            LOGGER.info("HTTP Method        :{}", request.getMethod());
            LOGGER.info("Class Method       :{}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            LOGGER.info("IP                 :{}", HttpUtils.getIp(request));
            LOGGER.info("Request Args       :{}", args);
            // 返回参数
            LOGGER.info("Response Args      :{}", JSONObject.toJSONString(returnValue));
            // 耗时时间
            LOGGER.info("Time-Consuming     :{}", System.currentTimeMillis() - startTime);
        }
        return returnValue;
    }

    /**
     * 获取controller方法
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }

    /**
     * 转换token并校验
     * @return
     */
    private Token parseTokenAndValidate() {
        String strToken = request.getHeader(Constant.TOKEN);
        if (StringUtils.isEmpty(strToken)) {
            throw new BusinessException(ResponseEnum.NOT_LOGIN);
        }
        Token token = encryptDecode.decryptToken(strToken);
        if (System.currentTimeMillis() > token.getExpireTime()) {
            throw new BusinessException(ResponseEnum.TOKEN_EXPIRED);
        }
        Optional<User> optionalUser = userDao.getUserByUserName(token.getUserName());
        User user = optionalUser.orElseThrow(() -> new BusinessException(ResponseEnum.NO_FIND_USER));
        LOGGER.info("User Info          :{}", user);
        return token;
    }

    /**
     * 替换方法参数
     *
     * @param joinPoint
     * @param args
     * @return
     * @throws Throwable
     */
    private Object proceed(ProceedingJoinPoint joinPoint, Object[] args, Token token) throws Throwable {
        for (int i = 0; i < args.length; i++) {
            Object object = args[i];
            if (object instanceof Token) {
                args[i] = token;
            }
        }
        Object returnValue = joinPoint.proceed(args);
        return returnValue;
    }
}
