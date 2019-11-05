package me.wessner.resilience4jDemo.handler

import me.wessner.resilience4jDemo.models.VideoGame
import me.wessner.resilience4jDemo.services.VideoGameService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import reactor.core.publisher.Mono


@RestController
class VideoGameHandler(val videoGameService: VideoGameService) {

    fun success(request: ServerRequest) = ok().body(fromValue(videoGameService.success()))

    fun fail(request: ServerRequest) = ok().body(fromValue(videoGameService.fail()))

    fun list(request: ServerRequest) = ok().body(fromPublisher(videoGameService.list(),  VideoGame::class.java))

    fun get(request: ServerRequest) = ok().body(fromPublisher(videoGameService.getByName(request.queryParam("title")), VideoGame::class.java))

    fun failOrSuccess(request: ServerRequest) = ok().body(fromValue(videoGameService.recover()))

    fun create(request: ServerRequest): Mono<ServerResponse> {
        val insertableGameMono = request.bodyToMono(VideoGame::class.java).map { p -> VideoGame(p) }

        return status(HttpStatus.CREATED)
                .body(fromPublisher(videoGameService.createFromMono(insertableGameMono), VideoGame::class.java))
    }
}