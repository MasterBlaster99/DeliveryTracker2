package com.example.deliverytracker.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.deliverytracker.data.model.Order
import com.example.deliverytracker.data.model.OrderEntity

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOrder(order: OrderEntity): Long

    @Query("SELECT * FROM orders")
    suspend fun getAllEntities(): List<OrderEntity>

}