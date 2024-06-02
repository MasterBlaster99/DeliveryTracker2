package com.example.deliverytracker.presentation.orderDetails

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.deliverytracker.R
import com.example.deliverytracker.data.model.Order
import com.example.deliverytracker.data.model.OrderEntity
import com.example.deliverytracker.databinding.ActivityMainBinding
import com.example.deliverytracker.databinding.ActivityOrderDetailsBinding
import com.example.deliverytracker.presentation.orderList.OrdersViewModel
import com.example.deliverytracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsActivity : AppCompatActivity() {
    private var binding: ActivityOrderDetailsBinding? = null
    var order: Order? = null
    private val viewModel by viewModels<OrderDetailsViewModel>()
    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Constants.RESULT_IMAGE_CAPTURED) {
            binding?.ivItem?.setImageURI(result.data?.getStringExtra(Constants.IMAGE_FILE_PATH)?.toUri())
            binding?.ivItem?.visibility = View.VISIBLE
        } else {
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getIntentData()
        setUpViews()
        setListeners()

    }

    private fun getIntentData() {
        order = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.ORDER,Order::class.java)!!
        } else {
            intent.getParcelableExtra<Order>(Constants.ORDER)
        }
    }

    private fun setUpViews() {
        binding?.ordersToolbar?.title?.text = getString(R.string.order_details)
        binding?.tvOrderNo?.text = order?.orderNumber
        binding?.tvCustName?.text = order?.customerName
        binding?.tvPrice?.text = order?.deliveryCost
        binding?.tvAddress?.text = order?.address
    }

    private fun setListeners() {
        binding?.ivAdd?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                launcher.launch(Intent(this,PhotoActivity::class.java))
                // Permission is already granted, handle the camera functionality
            } else {
                // Request the camera permission
                requestCameraPermission()
            }
        }
        binding?.rbDamaged?.setOnCheckedChangeListener { compoundButton, b ->
            if(b) {
                binding?.groupFinalAmt?.visibility = View.VISIBLE
            } else {
                binding?.groupFinalAmt?.visibility = View.GONE
            }
        }
        binding?.rbPerfect?.setOnCheckedChangeListener { compoundButton, b ->
            if(b) {
                binding?.groupFinalAmt?.visibility = View.GONE
            } else {
                binding?.groupFinalAmt?.visibility = View.VISIBLE
            }
        }
        binding?.btnSubmit?.setOnClickListener {
            if(validateOrder()) {
                val orderEntity = OrderEntity(orderNumber = order?.orderNumber, customerName = order?.customerName, latitude = order?.latitude, longitude = order?.longitude, address = order?.address, deliveryCost = order?.deliveryCost)
                viewModel.addOrder(orderEntity)
            }
            else {

            }
        }
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Show an explanation to the user why the permission is needed
            Toast.makeText(this, "Camera permission is needed to access the camera", Toast.LENGTH_LONG).show()
        }
        // Request the camera permission
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), Constants.CAMERA_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.CAMERA_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted, handle the camera functionality
                    launcher.launch(Intent(this,PhotoActivity::class.java))
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(this, "Please allow camera permission to proceed further", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun validateOrder(): Boolean {
        if(binding?.ivItem?.visibility != View.VISIBLE) {
            Toast.makeText(this@OrderDetailsActivity,"Please add item photo",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding?.rbDamaged?.isChecked==false && binding?.rbPerfect?.isChecked == false) {
            Toast.makeText(this@OrderDetailsActivity,"Please select product condition",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding?.groupFinalAmt?.visibility==View.VISIBLE) {
            if(binding?.etFinalAmount?.text?.isEmpty()==true) {
                Toast.makeText(this@OrderDetailsActivity,"Please enter final amount",Toast.LENGTH_SHORT).show()
                return false
            }
            val enteredAmount: Double = binding?.etFinalAmount?.text?.toString()?.toDouble()?:0.0
            val actualAmount: Double = order?.deliveryCost?.toDouble()?:0.0
            if(enteredAmount>actualAmount) {
                Toast.makeText(this@OrderDetailsActivity,"Final amount should not be greater than actual amount",Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }

}