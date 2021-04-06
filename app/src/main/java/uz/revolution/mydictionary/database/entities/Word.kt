package uz.revolution.mydictionary.database.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "word")
class Word:Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var categoryID: Int? = null

    var word: String? = null

    var translation: String? = null

    var imagePath: String? = null

    var liked: Boolean? = null

    @Ignore
    constructor(
        id: Int?,
        categoryID: Int?,
        word: String?,
        translation: String?,
        imagePath: String?,
        liked: Boolean?
    ) {
        this.id = id
        this.categoryID = categoryID
        this.word = word
        this.translation = translation
        this.imagePath = imagePath
        this.liked = liked
    }

    @Ignore
    constructor(
        categoryID: Int?,
        word: String?,
        translation: String?,
        imagePath: String?,
        liked: Boolean?
    ) {
        this.categoryID = categoryID
        this.word = word
        this.translation = translation
        this.imagePath = imagePath
        this.liked = liked
    }

    constructor()
}