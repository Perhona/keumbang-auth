package com.keumbang.api.auth_server.grpc.service;

import com.keumbang.api.auth_server.auth.service.TokenService;
import com.keumbang.api.auth_server.common.exception.JwtAuthenticationException;
import com.keumbang.api.auth_server.grpc.AuthServiceGrpc;
import com.keumbang.api.auth_server.grpc.ValidateTokenRequest;
import com.keumbang.api.auth_server.grpc.ValidateTokenResponse;
import com.keumbang.api.auth_server.user.entity.User;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@RequiredArgsConstructor
@GrpcService
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final TokenService tokenService;
    private final String SUCCESS_MESSAGE = "Access Token 인증에 성공했습니다.";

    @Override
    public void validateToken(ValidateTokenRequest request, StreamObserver<ValidateTokenResponse> responseObserver) {
        String accessToken = request.getAccessToken();

        try {
            sendSuccessResponse(responseObserver, tokenService.validateAccessToken(accessToken));
        } catch (JwtAuthenticationException e) {    // JWT 관련 예외 처리
            sendErrorResponse(responseObserver, Status.UNAUTHENTICATED, e.getErrorCode().getMessage());
        } catch (Exception e) {                     // 기타 예외 처리
            sendErrorResponse(responseObserver, Status.INTERNAL, "시스템 에러가 발생했습니다.");
        }
    }

    private void sendSuccessResponse(StreamObserver<ValidateTokenResponse> responseObserver, User user) {
        ValidateTokenResponse response = ValidateTokenResponse.newBuilder()
                .setIsValid(true)
                .setUserId(user.getId())
                .setAccount(user.getAccount())
                .setMessage(SUCCESS_MESSAGE)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void sendErrorResponse(StreamObserver<ValidateTokenResponse> responseObserver, Status status, String message) {
        responseObserver.onError(status.withDescription(message).asRuntimeException());
    }
}
