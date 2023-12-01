package com.example.demo.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// JWT 검증하고 인가를 처리하는 필터
@Slf4j
public class JsonWebTokenAuthorizationFilter extends OncePerRequestFilter {

    private static final List<String> PERMIT_URI = List.of("/api/user/login");
    private JsonWebTokenProvider jsonWebTokenProvider;

    public JsonWebTokenAuthorizationFilter(JsonWebTokenProvider jsonWebTokenProvider) {
        this.jsonWebTokenProvider = jsonWebTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String accessToken = jsonWebTokenProvider.resolveAccessToken(request);
        log.info("AccessToken : {}", accessToken);

        // 요청 URI 확인 및 토큰 검증
        if(!PERMIT_URI.contains(requestUri) && jsonWebTokenProvider.validateToken(accessToken)) {
            // 토큰에 담긴 회원정보를 추출해 시큐리티에서 사용할 인증 객체 반환
            Authentication authentication = jsonWebTokenProvider.getAuthentication(accessToken);

            // 시큐리티 컨텍스트 홀더에 사용자 인증 객체를 셋팅
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}