package com.xeubiart;

import com.xeubiart.account.gRPC.AccountGRPC;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {
    "com.xeubiart",
    "com.xeubiart.identity.config"
})
public class AppApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(AppApplication.class, args);
        System.out.println("✅ Spring Boot iniciado!");
    }
}
