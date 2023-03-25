package hu.bme.aut.bkknotifier.feature.stop.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Stop::class], version = 4)
@TypeConverters(value = [Converters::class])
abstract class StopDatabase : RoomDatabase(){
    abstract fun stopDao(): StopDao

    companion object{
        fun getDatabase(applicationContext: Context): StopDatabase {
            return Room.databaseBuilder(
                applicationContext,
                StopDatabase::class.java,
                "stop-db"
            )
            .fallbackToDestructiveMigration()
            .build()
        }
    }
}