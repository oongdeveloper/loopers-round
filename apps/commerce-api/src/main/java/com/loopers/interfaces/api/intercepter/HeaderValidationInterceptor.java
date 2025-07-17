package com.loopers.interfaces.api.intercepter;

import ch.qos.logback.core.util.StringUtil;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@NoArgsConstructor
public class HeaderValidationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("X-USER-ID");
        if (userId == null || StringUtils.isEmpty(userId)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "User ID 정보가 없습니다.");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
