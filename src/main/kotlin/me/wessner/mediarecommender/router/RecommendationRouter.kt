package me.wessner.mediarecommender.router

import me.wessner.mediarecommender.handler.RecommendationHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class RecommendationRouter(internal val recommendationHandler: RecommendationHandler) {

    @Bean
    fun recommendationsRouter() = router {
        (accept(MediaType.APPLICATION_JSON) and "/recommendations").nest {
            GET("", recommendationHandler::list)
        }
    }
}