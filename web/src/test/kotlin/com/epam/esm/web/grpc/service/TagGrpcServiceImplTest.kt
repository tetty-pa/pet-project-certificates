package com.epam.esm.web.grpc.service

import com.epam.esm.TagOuterClass.CreateTagRequest
import com.epam.esm.TagOuterClass.DeleteByIdTagRequest
import com.epam.esm.TagOuterClass.GetAllTagRequest
import com.epam.esm.TagOuterClass.GetByIdTagRequest
import com.epam.esm.grpcService.TagServiceGrpc
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

@SpringBootTest
class TagGrpcServiceImplTest(
    @Value("\${grpc.server.port}")
    var grpcPort: Int
) {
    @Autowired
    private lateinit var tagConverter: TagConverter

    @Autowired
    private lateinit var tagService: TagService

    @Autowired
    private lateinit var tagRepository: TagRepository

    private lateinit var stub: TagServiceGrpc.TagServiceBlockingStub

    private lateinit var channel: ManagedChannel

    @BeforeEach
    fun startClient() {
        channel = ManagedChannelBuilder
            .forTarget("localhost:$grpcPort")
            .usePlaintext()
            .build()
        stub = TagServiceGrpc.newBlockingStub(channel)
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
            GetAllTagRequest.newBuilder()
                .setPage(1).setSize(2).build()

        val actual = stub.getAll(request)

        assertThat(expected).isEqualTo(actual.tagListList)
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
            GetByIdTagRequest
                .newBuilder()
                .setTagId(addedTag.id)
                .build()

        val actual = stub.getById(request)

        assertThat(expected).isEqualTo(actual.tag)
        tagService.deleteById(addedTag.id).block()
    }

    @Test
    fun create() {
        val expected =
            CreateTagRequest
                .newBuilder()
                .setTag(tagConverter.tagToProto(TEST_TAG))
                .build()

        val actual = stub.create(expected)

        assertThat(expected.tag).isEqualTo(actual.tag)
    }

    @Test
    fun deleteById() {
        val tagSizeBefore =
            tagService.getAll(0, 111110).collectList().block()
        val addedTag = tagService.create(TEST_TAG).block()!!

        val request =
            DeleteByIdTagRequest.newBuilder().setTagId(addedTag.id).build()

        stub.deleteById(request)

        val tagSizeAfter = tagService.getAll(0, 11110).collectList().block()
        assertThat(tagSizeBefore).isEqualTo(tagSizeAfter)
    }

    companion object {
        val TEST_TAG = Tag("newTestTag")
        val PAGE: PageRequest = PageRequest.of(1, 2)
    }
}
