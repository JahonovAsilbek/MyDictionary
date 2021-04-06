package uz.revolution.mydictionary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.revolution.mydictionary.database.dao.DictionaryDao
import uz.revolution.mydictionary.database.entities.Category
import uz.revolution.mydictionary.database.entities.Word

@Database(entities = [Word::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DictionaryDao

    companion object {
        @Volatile
        private var database: AppDatabase? = null

        fun init(context: Context) {
            synchronized(this) {
                if (database == null) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "dictionary.db"
                    ).fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build()
                }
            }
        }
    }

    object get {
        fun getDatabase(): AppDatabase {
            return database!!
        }
    }
}