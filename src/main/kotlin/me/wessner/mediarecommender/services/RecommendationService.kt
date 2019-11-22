package me.wessner.mediarecommender.services

import me.wessner.mediarecommender.models.Recommendation
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.random.Random

@Service
class RecommendationService(val movieService: MovieService, val videoGameService: VideoGameService) {

    fun list(): Mono<Recommendation> {
        val moviesFlux = movieService.list()
        val gamesFlux = videoGameService.list()
        val maximumEntries = 3

        val recommendedMovies = moviesFlux
                .count()
                .flatMap { size ->
                    Flux.range(0, maximumEntries.coerceAtMost(Random.nextInt(1, size.toInt())))
                            .flatMap { i -> moviesFlux.collectList().map { movies -> movies[i] } }
                            .collectList()
                }
        val recommendedGames = gamesFlux
                .count()
                .flatMap { size ->
                    Flux.range(0, maximumEntries.coerceAtMost(Random.nextInt(1, size.toInt())))
                            .flatMap { i -> gamesFlux.collectList().map { games -> games[i] } }
                            .collectList()
                }


        return Mono.zip(recommendedMovies, recommendedGames).map { movieGameTuple -> Recommendation(movieGameTuple.t1, movieGameTuple.t2) }
    }
}