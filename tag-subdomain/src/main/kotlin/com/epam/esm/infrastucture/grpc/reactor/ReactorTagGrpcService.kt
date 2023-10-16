package com.epam.esm.infrastucture.grpc.reactor

import com.epam.esm.TagOuterClass.CreateTagRequest
import com.epam.esm.TagOuterClass.CreateTagResponse
import com.epam.esm.TagOuterClass.DeleteByIdTagRequest
import com.epam.esm.TagOuterClass.DeleteByIdTagResponse
import com.epam.esm.TagOuterClass.GetAllTagRequest
import com.epam.esm.TagOuterClass.GetAllTagResponse
import com.epam.esm.TagOuterClass.GetByIdTagRequest
import com.epam.esm.TagOuterClass.GetByIdTagResponse
import com.epam.esm.infrastucture.converter.proto.TagConverter
import com.epam.esm.application.service.TagService
import com.epam.esm.domain.Tag
import com.epam.esm.grpcService.ReactorTagServiceGrpc
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ReactorTagGrpcService(
    private val service: TagService,
    private val converter: TagConverter
) : ReactorTagServiceGrpc.TagServiceImplBase() {
    override fun getAll(request: Mono<GetAllTagRequest>): Mono<GetAllTagResponse> {
        return request
            .flatMapMany { service.getAll(page = it.page, size = it.size) }
            .map { tag -> converter.tagToProto(tag) }
            .collectList()
            .map { GetAllTagResponse.newBuilder().addAllTagList(it).build() }
    }

    override fun getById(request: Mono<GetByIdTagRequest>): Mono<GetByIdTagResponse> {
        return request
            .flatMap { service.getById(it.tagId) }
            .map { converter.tagToProto(it) }
            .map { GetByIdTagResponse.newBuilder().setTag(it).build() }
    }

    override fun create(request: Mono<CreateTagRequest>): Mono<CreateTagResponse> {
        return request
            .flatMap { service.create(Tag(null, it.tag.name)) }
            .map { converter.tagToProto(it) }
            .map { CreateTagResponse.newBuilder().setTag(it).build() }
    }

    override fun deleteById(request: Mono<DeleteByIdTagRequest>): Mono<DeleteByIdTagResponse> {
        return request
            .flatMap { service.deleteById(it.tagId) }
            .map { DeleteByIdTagResponse.getDefaultInstance() }
    }
}
