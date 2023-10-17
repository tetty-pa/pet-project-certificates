package com.epam.esm.infrastucture.grpc

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
import com.epam.esm.grpcService.TagServiceGrpc
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Component

@Component
class TagGrpcService(
    private val service: TagService,
    private val converter: TagConverter
) : TagServiceGrpc.TagServiceImplBase() {

    override fun getAll(
        request: GetAllTagRequest,
        responseObserver: StreamObserver<GetAllTagResponse>
    ) {
        val tags = service
            .getAll(page = request.page, size = request.size)
            .map { converter.tagToProto(it) }
            .collectList()
            .block()

        val build =
            GetAllTagResponse.newBuilder().addAllTagList(tags).build()
        responseObserver.onNext(build)
        responseObserver.onCompleted()
    }

    override fun getById(
        request: GetByIdTagRequest,
        responseObserver: StreamObserver<GetByIdTagResponse>
    ) {
        val protoTag =
            converter.tagToProto(service.getById(request.tagId).block()!!)

        val build =
            GetByIdTagResponse.newBuilder().setTag(protoTag).build()
        responseObserver.onNext(build)
        responseObserver.onCompleted()
    }

    override fun create(
        request: CreateTagRequest,
        responseObserver: StreamObserver<CreateTagResponse>
    ) {
        val tag = Tag(null, request.tag.name)

        val protoTag =
            converter.tagToProto(service.create(tag).block()!!)

        val build =
            CreateTagResponse.newBuilder().setTag(protoTag).build()
        responseObserver.onNext(build)
        responseObserver.onCompleted()
    }

    override fun deleteById(
        request: DeleteByIdTagRequest,
        responseObserver: StreamObserver<DeleteByIdTagResponse>
    ) {
        service.deleteById(request.tagId).block()

        val build =
            DeleteByIdTagResponse.getDefaultInstance()
        responseObserver.onNext(build)
        responseObserver.onCompleted()
    }
}
