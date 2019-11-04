package me.wessner.resilience4jDemo.repositories

import me.wessner.resilience4jDemo.models.VideoGame
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Repository
interface VideoGameRepository : ReactiveMongoRepository<VideoGame, Long> {

    fun findByIdAndTitle(id: String, title: String): Mono<VideoGame>

    fun findByTitle(title: String): Flux<VideoGame>
}