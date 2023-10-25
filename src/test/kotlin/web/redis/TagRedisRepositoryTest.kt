package web.redis

import com.epam.esm.WebApplication
import com.epam.esm.application.repository.TagCachingRepositoryOutPort
import com.epam.esm.domain.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest(classes = [WebApplication::class])
class TagRedisRepositoryTest {

    @Autowired
    private lateinit var tagCachingRepositoryOutPort: TagCachingRepositoryOutPort

    @Test
    fun findById() {
        val tag = tagCachingRepositoryOutPort.save(TEST_TAG).block()!!
        val actual: Mono<Tag> = tagCachingRepositoryOutPort.findById(TEST_TAG.id ?: "")
        StepVerifier.create(actual)
            .expectNext(tag)
            .verifyComplete()
    }

    @Test
    fun save() {
        val actual: Mono<Tag> = tagCachingRepositoryOutPort.save(TEST_TAG)
        StepVerifier.create(actual)
            .expectNext(TEST_TAG)
            .verifyComplete()
    }

    @Test
    fun deleteById() {
        tagCachingRepositoryOutPort.save(TEST_TAG).block()!!
        tagCachingRepositoryOutPort.deleteById(TEST_TAG.id!!).block()
        val actual = tagCachingRepositoryOutPort.findById(TEST_TAG.id!!)

        StepVerifier.create(actual)
            .expectComplete()
            .verify()
    }

    companion object {
        val TEST_TAG = Tag("1", "1")
    }
}
