package me.wessner.mediarecommender

import me.wessner.mediarecommender.models.Movie
import me.wessner.mediarecommender.models.VideoGame
import me.wessner.mediarecommender.repositories.MovieRepository
import me.wessner.mediarecommender.repositories.VideoGameRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux


@Component
class MediaPopulator(private val movieRepo: MovieRepository, private val gamesRepo: VideoGameRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val isEmpty = movieRepo.count().block() == 0L
        if (!isEmpty){
            return
        }
        val savedMoviesMono = movieRepo
                .saveAll(listOf(
                        Movie("Inception"),
                        Movie("Pulp Fiction")))

        val savedGamesMono = gamesRepo
                .saveAll(listOf(
                        VideoGame("Rainbow Six: Siege", 18),
                        VideoGame("Uncharted", 16)))

        Flux.concat(savedMoviesMono, savedGamesMono).collectList().block()
    }

}