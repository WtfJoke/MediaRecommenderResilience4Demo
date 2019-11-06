package me.wessner.resilience4jDemo.services

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator
import io.vavr.control.Try
import me.wessner.resilience4jDemo.models.VideoGame
import me.wessner.resilience4jDemo.repositories.VideoGameRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.IOException
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeoutException


@Service
class VideoGameService(circuitBreakerRegistry: CircuitBreakerRegistry, val repository: VideoGameRepository) {

    var failEverySecondCallCount = 0
    val top3Games = Flux.just(
            VideoGame("Fifa 20", 0),
            VideoGame("The Legend Of Zelda: Link's Awakening", 0),
            VideoGame("Borderlands 3", 18))

    private val circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50f)
            .slowCallRateThreshold(50f)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .slowCallDurationThreshold(Duration.ofSeconds(2))
            .permittedNumberOfCallsInHalfOpenState(3)
            .minimumNumberOfCalls(10)
            .slidingWindowType(SlidingWindowType.TIME_BASED)
            .slidingWindowSize(5)
            .recordExceptions(IOException::class.java, TimeoutException::class.java)
            .build()
    private val circuitBreaker: CircuitBreaker = circuitBreakerRegistry.circuitBreaker("videoGameBackend", circuitBreakerConfig)


    fun success(): String {
        return CircuitBreaker.decorateSupplier(circuitBreaker, this::successInternal).get()
    }

    fun fail(): String {
        return CircuitBreaker.decorateSupplier(circuitBreaker, this::failInternal).get()
    }

    fun recover(): Flux<VideoGame> {
        return Try.ofSupplier { Flux.just(VideoGame(failEverySecondCall(), 0)) }
                .recover { throwable -> top3Games }.get()
    }

    fun list(): Flux<VideoGame> {
        return repository.findAll()
    }

    fun createFromMono(toInsert: Mono<VideoGame>): Mono<VideoGame> {
        return createFromMonoInternal(toInsert).transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
    }

    fun getByName(titleOptional: Optional<String>): Mono<VideoGame> {
        return titleOptional
                .map { title -> repository.findByTitle(title) }
                .orElseGet { Mono.empty() }
    }

    private fun createFromMonoInternal(toInsert: Mono<VideoGame>): Mono<VideoGame> {
        return toInsert.flatMap { game -> repository.save(game) }
    }

    private fun successInternal(): String {
        return "R6"
    }

    private fun failInternal(): String {
        throw IOException("Failure while processing video game store")
    }

    private fun failEverySecondCall(): String {
        if (failEverySecondCallCount++ % 2 == 1) {
            failInternal()
        }
        return successInternal()
    }

}