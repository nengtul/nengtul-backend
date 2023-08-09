package kr.zb.nengtul.chat.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import java.util.Objects;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import kr.zb.nengtul.global.jwt.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompTokenInterceptor implements ChannelInterceptor {

    private static final String EMAIL_CLAIM = "email";
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService userDetailService;
    private static final String PREFIX = "Bearer ";
    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String token = accessor.getFirstNativeHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(PREFIX.length());
            if (jwtTokenProvider.isTokenValid(token)) {
                Authentication auth = getAuthentication(token);
                Objects.requireNonNull(accessor.getSessionAttributes()).put("user", auth.getName());
            }
        }

        return message;
    }

    @Transactional
    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = userDetailService.loadUserByUsername(getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
        return decodedJWT.getClaim(EMAIL_CLAIM).asString();
    }
}