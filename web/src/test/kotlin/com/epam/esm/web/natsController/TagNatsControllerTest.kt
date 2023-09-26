package com.epam.esm.web.natsController

import com.epam.esm.NatsSubject
import com.epam.esm.TagOuterClass.CreateTagRequest
import com.epam.esm.TagOuterClass.CreateTagResponse
import com.epam.esm.TagOuterClass.CreateTagRequest
import com.epam.esm.TagOuterClass.DeleteByIdTagRequest
import com.epam.esm.TagOuterClass.GetAllTagRequest
import com.epam.esm.TagOuterClass.GetAllTagResponse
import com.epam.esm.TagOuterClass.GetByIdTagRequest
import com.epam.esm.TagOuterClass.GetByIdTagResponse
import com.epam.esm.model.entity.Tag
import com.epam.esm.repository.TagRepository
import com.epam.esm.web.converter.TagConverter
import io.nats.client.Connection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.time.Duration

@SpringBootTest
class TagNatsControllerTest {

    @Autowired
    lateinit var natsConnection: Connection

    @Autowired
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var tagConverter: TagConverter

    @Test
    fun addTagTest() {
        val expected = CreateTagRequest.newBuilder()
            .setTag(tagConverter.tagToProto(TEST_TAG))
            .build()

        val future =
            natsConnection.requestWithTimeout(
                NatsSubject.ADD_TAG_SUBJECT,
                expected.toByteArray(),
                Duration.ofMillis(100000)
            )

        val actual =
            CreateTagResponse
                .parseFrom(future.get().data)
        assertThat(expected.tag).isEqualTo(actual.tag)

        val findByName = tagRepository.findByName(actual.tag.name).block()
        findByName?.let { tagRepository.deleteById(findByName.id) }
    }

    @Test
    fun getAllTagsTest() {
        val protoFromDb = tagRepository.findAll(PAGE)
            .map { tag -> tagConverter.tagToProto(tag) }.collectList().block()

        val expected = GetAllTagResponse.newBuilder()
            .addAllTagList(protoFromDb)
            .build()

        val request =
            GetAllTagRequest.newBuilder().setPage(PAGE.pageNumber).setSize(PAGE.pageSize).build()
        val future = natsConnection.requestWithTimeout(
            NatsSubject.GET_ALL_TAGS_SUBJECT,
            request.toByteArray(),
            Duration.ofMillis(100000)
        )
        val actual = GetAllTagResponse.parseFrom(future.get().data)
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun getByIdTagTest() {
        val addedTag = tagRepository.save(TEST_TAG).block()!!

        val dbTag = tagRepository.findById(addedTag.id).block() ?: TEST_TAG
        val protoTag = tagConverter.tagToProto(dbTag)
        val expected = GetByIdTagResponse.newBuilder().setTag(protoTag).build()

        val request = GetByIdTagRequest.newBuilder().setTagId(addedTag.id).build()
        val future = natsConnection.requestWithTimeout(
            NatsSubject.GET_TAG_BY_ID_SUBJECT,
            request.toByteArray(),
            Duration.ofMillis(100000)
        )
        val actual =
            GetByIdTagResponse.parseFrom(future.get().data)
        assertThat(expected).isEqualTo(actual)
        tagRepository.deleteById(addedTag.id).block()
    }

    @Test
    fun deleteByIdTagTest() {
        val tagsSizeBefore = tagRepository.findAll(Pageable.unpaged()).collectList().block()
        val addedTag = tagRepository.save(TEST_TAG).block()!!

        val request = DeleteByIdTagRequest.newBuilder().setTagId(addedTag.id).build()
        val future = natsConnection.requestWithTimeout(
            NatsSubject.DELETE_TAG_BY_ID_SUBJECT,
            request.toByteArray(),
            Duration.ofMillis(1000000)
        )
        future.get().data
        val tagsSizeAfter = tagRepository.findAll(Pageable.unpaged()).collectList().block()
        assertThat(tagsSizeBefore).isEqualTo(tagsSizeAfter)
    }

    companion object {
        val TEST_TAG = Tag("newTestTag")
        val PAGE: PageRequest = PageRequest.of(1, 2)
    }
}
