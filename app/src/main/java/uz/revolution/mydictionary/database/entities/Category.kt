package uz.revolution.mydictionary.database.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "category")
class Category {

    @PrimaryKey(autoGenerate = true)
    var id:Int?=null

    var categoryName:String?=null

    @Ignore
    constructor(id: Int?, categoryName: String?) {
        this.id = id
        this.categoryName = categoryName
    }

    @Ignore
    constructor(categoryName: String?) {
        this.categoryName = categoryName
    }

    constructor()
}