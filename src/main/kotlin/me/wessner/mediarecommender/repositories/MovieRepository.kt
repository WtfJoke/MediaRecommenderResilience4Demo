package me.wessner.mediarecommender.repositories

import me.wessner.mediarecommender.models.Movie
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface MovieRepository : ReactiveMongoRepository<Movie, Long> {
    fun findByTitle(title: String): Mono<Movie>
}