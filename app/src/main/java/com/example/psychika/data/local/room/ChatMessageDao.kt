package com.example.psychika.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.psychika.data.entity.DailyAveragePrediction

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages")
    fun getAllMessages(): LiveData<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages WHERE date = :date")
    fun getAllMessagesByDate(date: String): LiveData<List<ChatMessageEntity>>

    @Query("SELECT date, AVG(predict) AS averagePredict FROM chat_messages GROUP BY date ORDER BY date DESC")
    fun getAllDateMessages(): LiveData<List<DailyAveragePrediction>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun deleteAllMessages()
}