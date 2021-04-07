package uz.revolution.mydictionary.database.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import uz.revolution.mydictionary.database.entities.Category
import uz.revolution.mydictionary.database.entities.Word

@Dao
interface DictionaryDao {

    // methods for word

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWord(word: Word): Maybe<Long>

    @Query("select * from word where categoryID=:categoryID")
    fun getWordByCategoryID(categoryID: Int): List<Word>

    @Query("select * from word where id=:id")
    fun getWordByID(id:Int):Word

    @Query("select * from word where liked=:liked")
    fun getWordByLiked(liked:Boolean):List<Word>

    @Query("select * from word order by id")
    fun getAllWord():List<Word>

    @Update
    fun updateWord(word: Word): Completable

    @Delete
    fun deleteWord(word: Word): Single<Int>

    @Query("delete from word where categoryID=:categoryID")
    fun deleteAllWordByCategoryID(categoryID: Int):Single<Int>

    // methods for category

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category): Maybe<Long>

    @Query("select categoryName from category where id=:id")
    fun getCategoryNameByID(id: Int):String

    @Query("select id from category where categoryName like :categoryName")
    fun getCategoryIdByName(categoryName: String): Int

    @Query("select * from category order by id")
    fun getAllCategory(): List<Category>

    @Update
    fun updateCategory(category: Category): Completable

    @Delete
    fun deleteCategory(category: Category): Single<Int>
}