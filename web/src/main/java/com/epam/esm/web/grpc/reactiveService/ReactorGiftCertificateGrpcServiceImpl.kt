package com.epam.esm.web.grpc.reactiveService

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
import com.epam.esm.grpcService.ReactorGiftCertificateServiceGrpc
import com.epam.esm.service.GiftCertificateService
import com.epam.esm.web.converter.GiftCertificateConverter
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ReactorGiftCertificateGrpcServiceImpl(
    private val service: GiftCertificateService,
    private val converter: GiftCertificateConverter
) : ReactorGiftCertificateServiceGrpc.GiftCertificateServiceImplBase() {

    override fun getAll(request: Mono<GetAllGiftCertificateRequest>): Mono<GetAllGiftCertificateResponse> {
        return request
            .flatMapMany { service.getAll(page = it.page, size = it.size) }
            .map { certificate -> converter.entityToProto(certificate) }
            .collectList()
            .map { GetAllGiftCertificateResponse.newBuilder().addAllGiftCertificates(it).build() }
    }

    override fun getById(request: Mono<GetByIdGiftCertificateRequest>): Mono<GetByIdGiftCertificateResponse> {
        return request
            .flatMap { service.getById(it.giftCertificateId) }
            .map { converter.entityToProto(it) }
            .map { GetByIdGiftCertificateResponse.newBuilder().setGiftCertificate(it).build() }
    }

    override fun create(request: Mono<CreateGiftCertificateRequest>): Mono<CreateGiftCertificateResponse> {
        return request
            .flatMap { service.create(converter.protoToEntity(it.giftCertificate)) }
            .map { converter.entityToProto(it) }
            .map { CreateGiftCertificateResponse.newBuilder().setGiftCertificate(it).build() }
    }

    override fun update(request: Mono<UpdateGiftCertificateRequest>): Mono<UpdateGiftCertificateResponse> {
        return request
            .flatMap { service.update(converter.protoToEntity(it.giftCertificate)) }
            .map { converter.entityToProto(it) }
            .map { UpdateGiftCertificateResponse.newBuilder().setGiftCertificate(it).build() }
    }

    override fun deleteById(request: Mono<DeleteByIdGiftCertificateRequest>): Mono<DeleteByIdGiftCertificateResponse> {
        return request
            .flatMap { service.deleteById(it.giftCertificateId) }
            .map { DeleteByIdGiftCertificateResponse.getDefaultInstance() }
    }
}
