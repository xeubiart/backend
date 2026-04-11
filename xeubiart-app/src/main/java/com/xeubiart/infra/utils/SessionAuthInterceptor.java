package com.xeubiart.infra.utils;

import io.grpc.*;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import java.util.Base64;

@GrpcGlobalServerInterceptor
@AllArgsConstructor
public class SessionAuthInterceptor implements ServerInterceptor {
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    private static final Metadata.Key<String> SESSION_KEY =
            Metadata.Key.of("x-session-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        System.out.println("🔍 Interceptor called: " + call.getMethodDescriptor().getFullMethodName());
        System.out.println("🔍 All metadata keys: " + headers.keys());
        System.out.println("🔍 session-id value: " + headers.get(SESSION_KEY));

        String rawCookie = headers.get(SESSION_KEY);

        if (rawCookie != null) {
            try {
                String decodedId = new String(Base64.getDecoder().decode(rawCookie));
                Session session = sessionRepository.findById(decodedId);

                if (session != null) {
                    SecurityContext context = session.getAttribute("SPRING_SECURITY_CONTEXT");

                    if (context != null) {
                        SecurityContextHolder.setContext(context);
                        System.out.println("🔍 Interceptor auth: " + context.getAuthentication());
                        System.out.println("🔍 Interceptor name: " + context.getAuthentication().getName());

                    }
                }
            } catch (IllegalArgumentException e) {
                // invalid base64, skip
            }
        }

        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {
            @Override
            public void onComplete() {
                SecurityContextHolder.clearContext();
                super.onComplete();
            }

            @Override
            public void onCancel() {
                SecurityContextHolder.clearContext();
                super.onCancel();
            }
        };
    }
}