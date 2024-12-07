package com.snowy.thinkbox.interceptor;

import com.alibaba.fastjson.JSON;

import com.snowy.thinkbox.resp.UserLoginResp;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 拦截器：Spring框架特有的，常用于登录校验，权限校验，请求日志打印
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Resource
    private RedisTemplate redisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取header的token参数
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            log.warn( "token为空，请求被拦截" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        Object object = redisTemplate.opsForValue().get(token);
        if (object == null) {
            log.warn( "token无效，请求被拦截" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } else {
            log.info("已登录：{}", object);
//            LoginUserContext.setUser(JSON.parseObject((String) object, UserLoginResp.class));
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        long startTime = (Long) request.getAttribute("requestStartTime");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
