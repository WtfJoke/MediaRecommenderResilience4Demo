package me.wessner.mediarecommender.handler

import me.wessner.mediarecommender.models.Movie
import me.wessner.mediarecommender.services.MovieService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@RestController
class MovieHandler(val movieService: MovieService) {

    fun list(request: ServerRequest) = ServerResponse.ok().body(BodyInserters.fromPublisher(movieService.list(), Movie::class.java))

    fun get(request: ServerRequest) = ServerResponse.ok().body(BodyInserters.fromPublisher(movieService.getByName(request.queryParam("title")), Movie::class.java))

    fun create(request: ServerRequest): Mono<ServerResponse> {
        val insertableGameMono = request.bodyToMono(Movie::class.java).map { p -> Movie(p) }

        return ServerResponse.status(HttpStatus.CREATED)
                .body(BodyInserters.fromPublisher(movieService.create(insertableGameMono), Movie::class.java))
    }
}