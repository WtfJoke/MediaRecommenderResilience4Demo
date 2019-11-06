package me.wessner.mediarecommender.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class VideoGame(@Id var id: String, val title: String, val ageRestriction: Int){
    constructor(existingGame: VideoGame) : this(existingGame.title, existingGame.ageRestriction)
    constructor(title: String, ageRestriction: Int) : this(UUID.randomUUID().toString(), title, ageRestriction)
}