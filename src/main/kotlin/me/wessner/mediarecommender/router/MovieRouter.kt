package me.wessner.mediarecommender.router

import me.wessner.mediarecommender.handler.MovieHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class MovieRouter(internal val movieHandler: MovieHandler) {

    @Bean
    fun moviesRouter() = router {
        (accept(MediaType.APPLICATION_JSON) and "/movies").nest {
            GET("", movieHandler::list)
            GET("/get", movieHandler::get)
            POST("", movieHandler::create)
        }
    }
}