package me.wessner.resilience4jDemo.router

import me.wessner.resilience4jDemo.handler.VideoGameHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

@Configuration
class VideoGameRouter(private val gamesHandler: VideoGameHandler) {

    @Bean
    fun gameRouter() = router {
        (accept(APPLICATION_JSON) and "/videoGameBackend").nest {
            GET("/success", gamesHandler::success)
            GET("/fail", gamesHandler::fail)
            GET("/games", gamesHandler::list)
            GET("/games/get", gamesHandler::get)
            POST("/games", gamesHandler::create)
        }
    }

}