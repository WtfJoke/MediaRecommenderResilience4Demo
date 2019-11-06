package me.wessner.resilience4jDemo

import io.github.resilience4j.circuitbreaker.CircuitBreaker.State
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.vavr.collection.Stream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CircuitBreakerTest {

    private val BACKEND = "videoGameBackend"

    @Autowired
    private lateinit var registry: CircuitBreakerRegistry

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    @Throws(InterruptedException::class)
    fun shouldOpenAndCloseBackendACircuitBreaker() {
        // When
        Stream.rangeClosed(1, 10).forEach { _ -> produceFailure(BACKEND) }

        // Then
        checkHealthStatus(BACKEND, State.OPEN)

        Thread.sleep(1000) // waitDurationInOpenState

        // When
        Stream.rangeClosed(1, 3).forEach { _ -> produceSuccess(BACKEND) }

        checkHealthStatus(BACKEND, State.CLOSED)
    }

    private fun checkHealthStatus(circuitBreakerName: String, state: State) {
        val circuitBreaker = registry.circuitBreaker(circuitBreakerName)
        assertEquals(state, circuitBreaker.state)
    }

    private fun produceFailure(backend: String) {
        val response = restTemplate.getForEntity("/$backend/fail", String::class.java)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
    }

    private fun produceSuccess(backend: String) {
        val response = restTemplate.getForEntity("/$backend/success", String::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
    }
}