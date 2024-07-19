package dev.borisochieng.malltiverse.data.remote.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CurrentPrice(
    @SerializedName("NGN") val currency: List<Any?>
) : Serializable
