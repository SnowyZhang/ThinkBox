package com.snowy.thinkbox.interceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.ModelAndView;


public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取 token
        String token = request.getHeader("token");

        // 1. 如果 token 为空
        if (token == null || token.isEmpty()) {
            logger.warn("请求中缺少 Token。");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("未授权：缺少 Token");
            return false;
        }

        // 2. 如果 token 无效（通过 Redis 验证）
        if (!isValidToken(token)) {
            logger.warn("Token 无效或已过期。Token: {}", token);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("未授权：Token 无效或已过期");
            return false;
        }

        // 3. 如果 token 有效，允许请求继续执行
        logger.info("Token 校验通过，用户已登录。");
        return true;
    }

    // 使用 Redis 验证 token 是否有效
    private boolean isValidToken(String token) {


        // 检查 Redis 是否有该 token
        Object storedToken = redisTemplate.opsForValue().get(token);

        if (storedToken != null) {
            // Token 在 Redis 中存在，说明有效
            logger.info("Token 在 Redis 中找到。Token: {}", token);
            return true;
        }

        // Redis 中没有该 token，说明无效或已过期
        logger.warn("Redis 中未找到该 Token，Token: {}", token);
        return false;
    }

    // Post-handle method: 在控制器方法执行后、视图渲染前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 可选：在控制器执行后修改模型或添加其他逻辑
    }

    // After-completion method: 在视图渲染后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 可选：请求处理完成后进行清理或日志记录
    }
}
