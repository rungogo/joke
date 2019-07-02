package com.lad.chestnut.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.ResponseEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by sang on 2017/12/29.
 */
@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse resp,
                       AccessDeniedException e) throws IOException {
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.write(new ObjectMapper().writeValueAsString(new ResponseData(ResponseEnum.ACCESS_DENIED)));
        out.flush();
        out.close();
    }
}
