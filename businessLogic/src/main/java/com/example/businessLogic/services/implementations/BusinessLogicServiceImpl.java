package com.example.businessLogic.services.implementations;

import com.example.businessLogic.models.Client;
import com.example.businessLogic.models.Lessor;
import com.example.businessLogic.repositories.ClientRepository;
import com.example.businessLogic.repositories.LessorRepository;
import com.example.businesslogic.proto.BusinessLogicServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class BusinessLogicServiceImpl extends BusinessLogicServiceGrpc.BusinessLogicServiceImplBase {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LessorRepository lessorRepository;

    @Override
    public void sendUser(com.example.businesslogic.proto.UserDto dto, StreamObserver<com.example.businesslogic.proto.ServiceReply> responseObserver) {
        com.example.businesslogic.proto.ServiceReply.Builder builder = com.example.businesslogic.proto.ServiceReply.newBuilder();
        try {
            if (dto.getRole().toString().equals("CLIENT")) {
                Client client = new Client(dto.getName(), dto.getEmail(), dto.getPhone());
                clientRepository.save(client);
            } else {
                Lessor lessor = new Lessor(dto.getName(), dto.getEmail(), dto.getPhone());
                lessorRepository.save(lessor);
            }
            builder.setSuccess(true);
        } catch (Exception e) {
            builder.setSuccess(false);
        } finally {
            System.out.println("----------------gRPC call processed----------------");
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

}
