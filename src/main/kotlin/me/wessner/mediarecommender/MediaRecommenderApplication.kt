package me.wessner.mediarecommender

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MediaRecommenderApplication

fun main(args: Array<String>) {
	runApplication<MediaRecommenderApplication>(*args)
}
