package com.example.KaneStream.config;

import com.example.KaneStream.domain.user.service.UserDetailService;
import com.example.KaneStream.util.JWTService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    private final JWTService jwtService;
    private final ApplicationContext context;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message,@NonNull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                assert accessor != null;

                if(StompCommand.SEND.equals(accessor.getCommand())){
                    String authHeader= accessor.getFirstNativeHeader("Authorization");

                    String token = null;
                    String username = null;

                    if(authHeader!=null && authHeader.startsWith("Bearer ")) {
                        token = authHeader.substring(7);
                        username = jwtService.extractUsername(token);
                    }

                    if(username != null) {
                        UserDetails userDetails= context.getBean(UserDetailService.class).loadUserByUsername(username);
                        if(jwtService.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                                    userDetails,null,userDetails.getAuthorities()
                            );
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            accessor.setUser(authenticationToken);
                        }

                    }

                }


                return message;
            }
        });
    }
}
