package com.keumbang.api.auth_server.grpc.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // 요청 로깅
        logMessage("Call received", call.getMethodDescriptor().getFullMethodName());
        logMessage("Headers", headers.toString());

        // 원래 호출 계속 진행
        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);

        // 응답 로깅
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            @Override
            public void onComplete() {
                logMessage("Call completed", call.getMethodDescriptor().getFullMethodName());
                super.onComplete();
            }

            @Override
            public void onCancel() {
                logMessage("Call cancelled", call.getMethodDescriptor().getFullMethodName());
                super.onCancel();
            }

            @Override
            public void onMessage(ReqT message) {
                logMessage("Received message", message.toString());
                super.onMessage(message);
            }
        };
    }

    private void logMessage(String title, String message) {
        log.info("{} [{}]", title, message);
    }
}
