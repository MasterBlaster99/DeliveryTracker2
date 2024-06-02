package com.example.deliverytracker.presentation.orderList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deliverytracker.data.model.Order
import com.example.deliverytracker.data.model.OrderDTO
import com.example.deliverytracker.data.model.OrderEntity
import com.example.deliverytracker.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val repository: AppRepository): ViewModel() {

    private val _ordersResponse: MutableLiveData<Result<OrderDTO>> = MutableLiveData()
    val ordersResponse: LiveData<Result<OrderDTO>> get() = _ordersResponse

    fun getOrders() = viewModelScope.launch(Dispatchers.IO) {
        repository.getOrders().collect {
            Log.d("dataPrint","${it.getOrNull()?.ordersList}")
            Log.w("dataPrint","${it.getOrNull()?.ordersList}")
            Log.e("dataPrint","${it.getOrNull()?.ordersList}")
            _ordersResponse.postValue(it)
        }
    }
}