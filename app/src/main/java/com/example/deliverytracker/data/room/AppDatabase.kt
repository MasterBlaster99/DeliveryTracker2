package com.example.deliverytracker.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.deliverytracker.data.model.Order
import com.example.deliverytracker.data.model.OrderDTO
import com.example.deliverytracker.data.model.OrderEntity

@Database(
    entities = [OrderEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entityDao(): OrderDao
}