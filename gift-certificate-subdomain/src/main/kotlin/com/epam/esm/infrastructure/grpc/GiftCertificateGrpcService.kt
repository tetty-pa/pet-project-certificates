package com.epam.esm.infrastructure.grpc

import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.CreateGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.DeleteByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.DeleteByIdGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetAllGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.GetByIdGiftCertificateResponse
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateRequest
import com.epam.esm.GiftCertificateOuterClass.UpdateGiftCertificateResponse
import com.epam.esm.infrastructure.converter.proto.GiftCertificateConverter
import com.epam.esm.application.service.GiftCertificateServiceInPort
import com.epam.esm.grpcService.GiftCertificateServiceGrpc
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Component

@Component
class GiftCertificateGrpcService(
    private val service: GiftCertificateServiceInPort,
    private val converter: GiftCertificateConverter
) : GiftCertificateServiceGrpc.GiftCertificateServiceImplBase() {

    override fun getAll(
        request: GetAllGiftCertificateRequest,
        responseObserver: StreamObserver<GetAllGiftCertificateResponse>
    ) {
        val giftCertificates =
            service.getAll(page = request.page, size = request.size)
                .map { converter.domainToProto(it) }
                .collectList()
                .block()

        val build =
            GetAllGiftCertificateResponse.newBuilder().addAllGiftCertificates(giftCertificates).build()
        responseObserver.onNext(build)
        responseObserver.onCompleted()
    }

    override fun getById(
        request: GetByIdGiftCertificateRequest,
        responseObserver: StreamObserver<GetByIdGiftCertificateResponse>
    ) {
        val protoGiftCertificate =
            converter.domainToProto(service.getById(request.giftCertificateId).block()!!)

        val build =
            GetByIdGiftCertificateResponse.newBuilder().setGiftCertificate(protoGiftCertificate).build()
        responseObserver.onNext(build)
        responseObserver.onCompleted()
    }

    override fun create(
        request: CreateGiftCertificateRequest,
        responseObserver: StreamObserver<CreateGiftCertificateResponse>
    ) {
        val giftCertificate = converter.protoToDomain(request.giftCertificate)

        val protoGiftCertificate =
            converter.domainToProto(service.create(giftCertificate).block()!!)

        val build =
            CreateGiftCertificateResponse.newBuilder().setGiftCertificate(protoGiftCertificate).build()
        responseObserver.onNext(build)
        responseObserver.onCompleted()
    }

    override fun update(
        request: UpdateGiftCertificateRequest,
        responseObserver: StreamObserver<UpdateGiftCertificateResponse>
    ) {
        val giftCertificate =
            converter
                .protoToDomain(request.giftCertificate)
                .apply { id = request.id }
        val protoGiftCertificate =
            converter.domainToProto(service.update(giftCertificate).block()!!)

        val build =
            UpdateGiftCertificateResponse.newBuilder().setGiftCertificate(protoGiftCertificate).build()
        responseObserver.onNext(build)
        responseObserver.onCompleted()
    }

    override fun deleteById(
        request: DeleteByIdGiftCertificateRequest,
        responseObserver: StreamObserver<DeleteByIdGiftCertificateResponse>
    ) {
        service.deleteById(request.giftCertificateId).block()

        val build =
            DeleteByIdGiftCertificateResponse.getDefaultInstance()
        responseObserver.onNext(build)
        responseObserver.onCompleted()
    }
}
