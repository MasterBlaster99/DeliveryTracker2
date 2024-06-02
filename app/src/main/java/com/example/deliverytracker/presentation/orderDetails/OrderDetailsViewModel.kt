package com.example.deliverytracker.presentation.orderDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deliverytracker.data.model.OrderDTO
import com.example.deliverytracker.data.model.OrderEntity
import com.example.deliverytracker.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(private val repository: AppRepository): ViewModel() {

    private val _ordersList: MutableLiveData<List<OrderEntity>> = MutableLiveData()
    val orderList: LiveData<List<OrderEntity>> get() = _ordersList

    fun getOrdersList() = viewModelScope.launch(Dispatchers.IO) {
        repository.getOrdersList().collect {
            _ordersList.postValue(it)
        }
    }

    fun addOrder(orderEntity: OrderEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.addOrder(orderEntity)
    }

}