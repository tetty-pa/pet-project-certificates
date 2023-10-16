package web.grpc.reactor

import com.epam.esm.TagOuterClass.CreateTagRequest
import com.epam.esm.TagOuterClass.CreateTagResponse
import com.epam.esm.TagOuterClass.DeleteByIdTagRequest
import com.epam.esm.TagOuterClass.DeleteByIdTagResponse
import com.epam.esm.TagOuterClass.GetAllTagRequest
import com.epam.esm.TagOuterClass.GetAllTagResponse
import com.epam.esm.TagOuterClass.GetByIdTagRequest
import com.epam.esm.TagOuterClass.GetByIdTagResponse
import com.epam.esm.WebApplication
import com.epam.esm.infrastucture.converter.proto.TagConverter
import com.epam.esm.application.repository.TagRepositoryOutPort
import com.epam.esm.application.service.TagService
import com.epam.esm.domain.Tag
import com.epam.esm.grpcService.ReactorTagServiceGrpc
import io.grpc.ManagedChannel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest(classes = [WebApplication::class])
class ReactorTagGrpcServiceImplTest {
    @Autowired
    lateinit var channel: ManagedChannel

    @Autowired
    private lateinit var tagConverter: TagConverter

    @Autowired
    private lateinit var tagService: TagService

    @Autowired
    private lateinit var tagRepositoryOutPort: TagRepositoryOutPort

    private lateinit var stub: ReactorTagServiceGrpc.ReactorTagServiceStub

    @BeforeEach
    fun startClient() {
        stub = ReactorTagServiceGrpc.newReactorStub(channel)
    }

    @AfterEach
    fun cleanDatabase() {
        val tag =
            tagRepositoryOutPort.findByName(TEST_TAG.name).block()
        tagRepositoryOutPort.deleteById(tag?.id ?: "").block()
    }

    @Test
    fun getAll() {
        val tags =
            tagService.getAll(page = PAGE.pageNumber, size = PAGE.pageSize).map {
                tagConverter.tagToProto(it)
            }.collectList().block()


        val expected =
            GetAllTagResponse
                .newBuilder()
                .addAllTagList(tags)
                .build()


        val request =
            Mono.just(
                GetAllTagRequest.newBuilder()
                    .setPage(1).setSize(2).build()
            )
        val actual = stub.getAll(request)

        StepVerifier.create(actual)
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun getById() {
        val addedTag =
            tagService.create(TEST_TAG).block()!!

        val expected =
            GetByIdTagResponse
                .newBuilder()
                .setTag(tagConverter.tagToProto(TEST_TAG))
                .build()

        val request =
            Mono.just(
                GetByIdTagRequest
                    .newBuilder()
                    .setTagId(addedTag.id)
                    .build()
            )
        val actual = stub.getById(request)

        StepVerifier.create(actual)
            .expectNext(expected)
            .verifyComplete()
        tagService.deleteById(addedTag.id ?: "").block()
    }

    @Test
    fun create() {
        val expected =
            CreateTagResponse
                .newBuilder()
                .setTag(tagConverter.tagToProto(TEST_TAG))
                .build()

        val request =
            Mono.just(
                CreateTagRequest
                    .newBuilder()
                    .setTag(tagConverter.tagToProto(TEST_TAG))
                    .build()
            )
        val actual = stub.create(request)

        StepVerifier.create(actual)
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun deleteById() {
        val addedTag = tagService.create(TEST_TAG).block()!!

        val request =
            Mono.just(DeleteByIdTagRequest.newBuilder().setTagId(addedTag.id).build())

        val actual = stub.deleteById(request)

        StepVerifier.create(actual)
            .expectNext(DeleteByIdTagResponse.getDefaultInstance())
            .verifyComplete()
    }

    companion object {
        val TEST_TAG = Tag(null, "newTestTag")
        val PAGE: PageRequest = PageRequest.of(1, 2)
    }
}
