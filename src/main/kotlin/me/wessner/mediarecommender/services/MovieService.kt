package me.wessner.mediarecommender.services

import me.wessner.mediarecommender.models.Movie
import me.wessner.mediarecommender.repositories.MovieRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class MovieService(val repository: MovieRepository) {

    val top3Movies = Flux.just(
            Movie("The Shawshank Redemption"),
            Movie("Godfather"),
            Movie("The dark Knight"))

    fun list(): Flux<Movie> {
        return repository.findAll()
    }

    fun create(toInsert: Mono<Movie>): Mono<Movie> {
        return toInsert.flatMap { game -> repository.save(game) }
    }

    fun getByName(titleOptional: Optional<String>): Mono<Movie> {
        return titleOptional
                .map { title -> repository.findByTitle(title) }
                .orElseGet { Mono.empty() }
    }
}