package com.example.deliverytracker.presentation.orderList

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.deliverytracker.R
import com.example.deliverytracker.data.model.Order
import com.example.deliverytracker.databinding.FragmentOrdersBinding
import com.example.deliverytracker.presentation.orderDetails.OrderDetailsActivity
import com.example.deliverytracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private var binding: FragmentOrdersBinding? = null
    private val viewModel by viewModels<OrdersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(layoutInflater)
        getOrders()
        registerObserver()

        return binding?.root
    }

    private fun getOrders() {
        viewModel.getOrders()
    }

    private fun registerObserver() {
        viewModel.ordersResponse.observe(requireActivity()) {
            binding?.rvOrders?.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            binding?.rvOrders?.adapter = OrdersAdapter(requireContext()){
                openOrderDetailsScreen(it)
            }.apply {
                if(it.isSuccess) {
                    submitList(it.getOrNull()?.ordersList)
                    Constants.TOTAL_DELIVERY_COUNT = it.getOrNull()?.ordersList?.size?:0
                } else {
                    Toast.makeText(requireContext(),"Something went wrong!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openOrderDetailsScreen(order: Order?) {
        startActivity(Intent(requireActivity(),OrderDetailsActivity::class.java).apply {
            putExtras(Bundle().apply {
                putParcelable(Constants.ORDER,order)
            })
        })
    }

}