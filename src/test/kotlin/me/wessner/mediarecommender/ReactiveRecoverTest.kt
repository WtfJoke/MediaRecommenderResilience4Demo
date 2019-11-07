package me.wessner.mediarecommender

import com.mongodb.internal.connection.tlschannel.util.Util.assertTrue
import io.vavr.collection.Stream
import me.wessner.mediarecommender.models.VideoGame
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class ReactiveRecoverTest(@Autowired val webClient: WebTestClient) {

    private val GAME_BACKEND = "videoGameBackend"

    @Test
    fun shouldNeverFailOnRecovery() {
        Stream.rangeClosed(1, 10).forEach { _ ->
            val exchange = webClient.get().uri("/$GAME_BACKEND/games/recover").exchange()
            exchange.expectStatus().isOk
            val size = exchange.expectBodyList(VideoGame::class.java)
                    .returnResult().responseBody!!.size
            assertTrue(size > 0 || size == 3)
        }
    }
}