package com.epam.esm.web.grpc.reactiveService

import com.epam.esm.TagOuterClass.CreateTagRequest
import com.epam.esm.TagOuterClass.DeleteByIdTagRequest
import com.epam.esm.TagOuterClass.GetAllTagRequest
import com.epam.esm.TagOuterClass.GetByIdTagRequest
import com.epam.esm.grpcService.ReactorTagServiceGrpc
import com.epam.esm.model.entity.Tag
import com.epam.esm.repository.TagRepository
import com.epam.esm.service.TagService
import com.epam.esm.web.converter.TagConverter
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Mono

@SpringBootTest
class ReactorTagGrpcServiceImplTest(
    @Value("\${grpc.server.port}")
    var grpcPort: Int
) {
    @Autowired
    private lateinit var tagConverter: TagConverter

    @Autowired
    private lateinit var tagService: TagService

    @Autowired
    private lateinit var tagRepository: TagRepository

    private lateinit var stub: ReactorTagServiceGrpc.ReactorTagServiceStub

    private lateinit var channel: ManagedChannel

    @BeforeEach
    fun startClient() {
        channel = ManagedChannelBuilder
            .forTarget("localhost:$grpcPort")
            .usePlaintext()
            .build()
        stub = ReactorTagServiceGrpc.newReactorStub(channel)
    }

    @AfterEach
    fun cleanDatabase() {
        val tag =
            tagRepository.findByName(TEST_TAG.name).block()
        tagRepository.deleteById(tag?.id ?: "").block()
    }

    @Test
    fun getAll() {
        val expected =
            tagService.getAll(page = PAGE.pageNumber, size = PAGE.pageSize).map {
                tagConverter.tagToProto(it)
            }.collectList().block()

        val request =
            Mono.just(
                GetAllTagRequest.newBuilder()
                    .setPage(1).setSize(2).build()
            )
        val actual = stub.getAll(request).block()

        assertThat(actual?.tagListList).isEqualTo(expected)
    }

    @Test
    fun getById() {
        val addedTag =
            tagService.create(TEST_TAG).block()!!

        val expected =
            tagService
                .getById(addedTag.id)
                .map {
                    tagConverter
                        .tagToProto(it)
                }.block()!!

        val request =
            Mono.just(
                GetByIdTagRequest
                    .newBuilder()
                    .setTagId(addedTag.id)
                    .build()
            )
        val actual = stub.getById(request).block()

        assertThat(actual?.tag).isEqualTo(expected)
        tagService.deleteById(addedTag.id).block()
    }

    @Test
    fun create() {
        val expected =
            Mono.just(
                CreateTagRequest
                    .newBuilder()
                    .setTag(tagConverter.tagToProto(TEST_TAG))
                    .build()
            )
        val actual = stub.create(expected).block()

        assertThat(actual?.tag).isEqualTo(expected.block()?.tag)
    }

    @Test
    fun deleteById() {
        val tagSizeBefore =
            tagService.getAll(0, 111110).collectList().block()
        val addedTag = tagService.create(TEST_TAG).block()!!

        val request =
            Mono.just(DeleteByIdTagRequest.newBuilder().setTagId(addedTag.id).build())

        stub.deleteById(request).block()

        val tagSizeAfter = tagService.getAll(0, 11110).collectList().block()
        assertThat(tagSizeAfter).isEqualTo(tagSizeBefore)
    }

    companion object {
        val TEST_TAG = Tag("newTestTag")
        val PAGE: PageRequest = PageRequest.of(1, 2)
    }
}
