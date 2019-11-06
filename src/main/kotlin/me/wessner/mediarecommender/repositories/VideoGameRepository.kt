package me.wessner.mediarecommender.repositories

import me.wessner.mediarecommender.models.VideoGame
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono


@Repository
interface VideoGameRepository : ReactiveMongoRepository<VideoGame, String> {
    fun findByTitle(title: String): Mono<VideoGame>
}