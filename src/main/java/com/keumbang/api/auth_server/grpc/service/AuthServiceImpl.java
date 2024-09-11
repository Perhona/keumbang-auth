package com.keumbang.api.auth_server.grpc.service;

import com.keumbang.api.auth_server.auth.service.TokenService;
import com.keumbang.api.auth_server.common.exception.ErrorCode;
import com.keumbang.api.auth_server.common.exception.JwtAuthenticationException;
import com.keumbang.api.auth_server.grpc.AuthServiceGrpc;
import com.keumbang.api.auth_server.grpc.ValidateTokenRequest;
import com.keumbang.api.auth_server.grpc.ValidateTokenResponse;
import com.keumbang.api.auth_server.user.entity.User;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@RequiredArgsConstructor
@GrpcService
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final TokenService tokenService;
    private final String SUCCESS_MESSAGE = "Authentication success";

    @Override
    public void validateToken(ValidateTokenRequest request, StreamObserver<ValidateTokenResponse> responseObserver) {
        String accessToken = request.getAccessToken();

        try {
            sendSuccessResponse(responseObserver, tokenService.validateAccessToken(accessToken));
        } catch (JwtAuthenticationException e) {    // JWT 관련 예외 처리
            sendErrorResponse(responseObserver, e.getErrorCode());
        } catch (Exception e) {                     // 기타 예외 처리
            sendErrorResponse(responseObserver, ErrorCode.INTERNAL_SERVER_ERROR);
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

    private void sendErrorResponse(StreamObserver<ValidateTokenResponse> responseObserver, ErrorCode errorCode) {
        ValidateTokenResponse response = ValidateTokenResponse.newBuilder()
                .setIsValid(false)
                .setErrorCode(errorCode.name())
                .setMessage(errorCode.getMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
