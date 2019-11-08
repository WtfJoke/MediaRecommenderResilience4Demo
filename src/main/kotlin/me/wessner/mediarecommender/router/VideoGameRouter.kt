package me.wessner.mediarecommender.router

import me.wessner.mediarecommender.handler.VideoGameHandler
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
            "/games".nest {
                GET("", gamesHandler::list)
                POST("", gamesHandler::create)
                GET("/get", gamesHandler::get)
                GET("/recover", gamesHandler::recover)
            }
        }
    }

}