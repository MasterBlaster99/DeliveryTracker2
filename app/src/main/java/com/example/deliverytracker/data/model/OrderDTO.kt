package com.example.deliverytracker.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.deliverytracker.utils.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.sql.Struct

@Entity(tableName = Constants.TABLE_NAME)
@Parcelize
data class OrderDTO(
    @SerializedName("orderlist")
    val ordersList: ArrayList<Order>? = null): Parcelable

@Parcelize
data class Order(@SerializedName("order_id")
                 val orderId: String? = null,
                 @SerializedName("order_no")
                 val orderNumber: String? = null,
                 @SerializedName("customer_name")
                 val customerName: String? = null,
                 @SerializedName("latitude")
                 val latitude: String? = null,
                 @SerializedName("longitude")
                 val longitude: String? = null,
                 @SerializedName("address")
                 val address: String? = null,
                 @SerializedName("delivery_cost")
                 val deliveryCost: String? = null): Parcelable {
}

//For Room
@Entity(tableName = Constants.TABLE_NAME)
@Parcelize
data class OrderEntity(@SerializedName("order_id")
                 @PrimaryKey(autoGenerate = true)
                 @ColumnInfo(name = "id")
                 var orderId: Int = 0,
                 @ColumnInfo(name = "order_no")
                 @SerializedName("order_no")
                 val orderNumber: String? = null,
                 @SerializedName("customer_name")
                 @ColumnInfo(name = "customer_name")
                 val customerName: String? = null,
                 @SerializedName("latitude")
                 @ColumnInfo(name = "latitude")
                 val latitude: String? = null,
                 @SerializedName("longitude")
                 @ColumnInfo(name = "longitude")
                 val longitude: String? = null,
                 @SerializedName("address")
                 @ColumnInfo(name = "address")
                 val address: String? = null,
                 @SerializedName("delivery_cost")
                 @ColumnInfo(name = "delivery_cost")
                 val deliveryCost: String? = null,
                 @ColumnInfo(name = "image_path")
                 val imagePath: String? = null): Parcelable {
}

