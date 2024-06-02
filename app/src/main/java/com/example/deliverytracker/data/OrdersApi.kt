package com.example.deliverytracker.data

import com.example.deliverytracker.data.model.OrderDTO
import retrofit2.http.GET

interface OrdersApi {

    @GET("/test_app/orderlist.php")
    suspend fun getOrders(): OrderDTO
}