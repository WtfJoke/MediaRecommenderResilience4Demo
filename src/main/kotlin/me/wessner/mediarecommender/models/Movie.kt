package me.wessner.mediarecommender.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Movie(@Id var id: String, val title: String) {
    constructor(title: String) : this(UUID.randomUUID().toString(), title)
    constructor(existingMovie: Movie) : this(existingMovie.title)
}