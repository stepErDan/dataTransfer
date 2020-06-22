package com.eaglesoft.common.config;

import com.baomidou.kisso.web.handler.SSOHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LonginHandlerInterceptor implements SSOHandlerInterceptor {
    @Override
    public boolean preTokenIsNullAjax(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return false;
    }

    @Override
    public boolean preTokenIsNull(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return false;
    }
}
