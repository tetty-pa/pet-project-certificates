package web.redis

import com.epam.esm.WebApplication
import com.epam.esm.application.repository.TagRedisRepositoryOutPort
import com.epam.esm.domain.Tag
import com.mongodb.client.result.DeleteResult
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest(classes = [WebApplication::class])
class TagRedisRepositoryTest {

    @Autowired
    private lateinit var tagRedisRepositoryOutPort: TagRedisRepositoryOutPort

    @Test
    fun findById() {
        val tag = tagRedisRepositoryOutPort.save(TEST_TAG).block()!!
        val actual: Mono<Tag> = tagRedisRepositoryOutPort.findById(TEST_TAG.id ?: "")
        StepVerifier.create(actual)
            .expectNext(tag)
            .verifyComplete()
    }

    @Test
    fun save() {
        val actual: Mono<Tag> = tagRedisRepositoryOutPort.save(TEST_TAG)
        StepVerifier.create(actual)
            .expectNext(TEST_TAG)
            .verifyComplete()
    }

    @Test
    fun deleteById() {
        tagRedisRepositoryOutPort.save(TEST_TAG).block()!!
        val actual: Mono<DeleteResult> = tagRedisRepositoryOutPort.deleteById(TEST_TAG.id!!)
        StepVerifier.create(actual)
            .expectNext(DeleteResult.acknowledged(1))
            .verifyComplete()
    }

    companion object {
        val TEST_TAG = Tag("1", "1")

    }
}
