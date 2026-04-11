package com.xeubiart.account.gRPC;

import com.google.protobuf.Empty; // Importe o Empty do Google
import com.xeubiart.account.service.AccountService;
import com.xeubiart.identity.service.IdentityService;
import com.xeubiart.proto.UserInfoGrpc;
import com.xeubiart.proto.GetUsernameOutput;
import com.xeubiart.proto.HasProposalOutput;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.UUID;

@GrpcService
@AllArgsConstructor
public class AccountGRPC extends UserInfoGrpc.UserInfoImplBase {
    private final AccountService accountService;

    @Override
    public void getUsername(Empty request, StreamObserver<GetUsernameOutput> responseObserver) {
        // O Interceptor já validou o token e preencheu o contexto!
        UUID accountId = accountService.getAccountIdFromSession();
        // TODO: implement a name field into account
        String username = this.accountService.findById(accountId).getEmail();

        System.out.println("✅ gRPC Auth Success! User ID: " + accountId);

        GetUsernameOutput output = GetUsernameOutput.newBuilder()
                .setUsername(username)
                .build();

        responseObserver.onNext(output);
        responseObserver.onCompleted();
    }

    @Override
    public void hasProposal(Empty request, StreamObserver<HasProposalOutput> responseObserver) {
        System.out.println("✅ gRPC received HasProposal request for ID: " + accountService.getAccountIdFromSession());

        HasProposalOutput output = HasProposalOutput.newBuilder()
                .setHasProposal(true)
                .build();

        responseObserver.onNext(output);
        responseObserver.onCompleted();
    }
}