package com.keumbang.api.auth_server.grpc.config;

import com.keumbang.api.auth_server.grpc.interceptor.LoggingInterceptor;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {
    @Bean
    @GrpcGlobalServerInterceptor
    public ServerInterceptor loggingInterceptor() {
        return new LoggingInterceptor(); // 작성한 LoggingInterceptor를 사용
    }
}
