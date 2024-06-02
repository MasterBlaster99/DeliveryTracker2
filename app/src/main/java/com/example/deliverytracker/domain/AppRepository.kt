package com.example.deliverytracker.domain

import com.example.deliverytracker.data.OrdersApi
import com.example.deliverytracker.data.model.Order
import com.example.deliverytracker.data.model.OrderDTO
import com.example.deliverytracker.data.model.OrderEntity
import com.example.deliverytracker.data.room.OrderDao
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


class AppRepository(private val api: OrdersApi, private val orderDao: OrderDao) {

    suspend fun getOrders() = flow<Result<OrderDTO>> {
       emit(Result.success(api.getOrders()))
    }.catch {
        emit(Result.failure(it))
    }

    suspend fun getOrdersList() = flow<List<OrderEntity>> {
        emit(orderDao.getAllEntities())
    }

    suspend fun addOrder(orderEntity: OrderEntity) {
        orderDao.addOrder(orderEntity)
    }
}