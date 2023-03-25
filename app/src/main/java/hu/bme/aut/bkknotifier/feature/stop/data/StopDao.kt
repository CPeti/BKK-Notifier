package hu.bme.aut.bkknotifier.feature.stop.data

import androidx.room.*

@Dao
interface StopDao {
    @Query("SELECT * FROM stop")
    fun getAll(): List<Stop>

    @Query("SELECT * FROM stop WHERE id=:stopId")
    fun getStop(stopId: String): Stop

    @Insert
    fun insert(stop: Stop)

    @Delete
    fun delete(stop: Stop)

    @Update
    fun update(stop: Stop)

    @Query("DELETE FROM stop")
    fun deleteAll()
}