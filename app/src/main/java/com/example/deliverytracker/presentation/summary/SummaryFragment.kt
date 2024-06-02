package com.example.deliverytracker.presentation.summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.deliverytracker.R
import com.example.deliverytracker.databinding.FragmentSummaryBinding
import com.example.deliverytracker.presentation.orderDetails.OrderDetailsViewModel
import com.example.deliverytracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private var binding: FragmentSummaryBinding? = null
    private val viewModel by viewModels<OrderDetailsViewModel>()
    private var mTotalCash = 0.0
    private var mDeliveryCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSummaryBinding.inflate(layoutInflater)
        binding?.sumToolBar?.title?.text = "Summary"
        registerObserver()
        viewModel.getOrdersList()

        return binding?.root
    }

    private fun registerObserver() {
        viewModel.orderList.observe(requireActivity()) {
            it.forEach { entity ->
                mTotalCash += entity.deliveryCost?.toDouble()?:0.0
            }
            binding?.tvTotalPrice?.text = "$mTotalCash"
            binding?.tvDeliveriesCompleted?.text = "${it.size}/${Constants.TOTAL_DELIVERY_COUNT}"
        }
    }

}