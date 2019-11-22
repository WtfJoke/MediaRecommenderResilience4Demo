package me.wessner.mediarecommender.handler

import me.wessner.mediarecommender.models.Recommendation
import me.wessner.mediarecommender.services.RecommendationService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@RestController
class RecommendationHandler(val recommendationService: RecommendationService) {

    fun list(request: ServerRequest) = ServerResponse.ok().body(BodyInserters.fromPublisher(recommendationService.list(), Recommendation::class.java))
}