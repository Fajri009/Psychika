package com.example.psychika.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChatMessageEntity::class], version = 1)
abstract class ChatMessageDatabase: RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: ChatMessageDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ChatMessageDatabase {
            if (INSTANCE == null) {
                synchronized(ChatMessageDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ChatMessageDatabase::class.java, "favorite_user_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE as ChatMessageDatabase
        }
    }
}