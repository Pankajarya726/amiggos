package com.tekzee.amiggos.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "item")
class Menu(
    @SerializedName("age_restriction")
    val ageRestriction: String = "",
    @SerializedName("cost")
    val cost: Int = 0,
    @SerializedName("description")
    val description: String = "",
    @PrimaryKey @ColumnInfo(name = "id") val id: Int = 0,
    @SerializedName("is_guest_invities")
    val isGuestInvities: Int = 0,
    @SerializedName("menu_image")
    val menuImage: String = "",
    @SerializedName("method")
    val method: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("is_idproof_verified")
    val is_idproof_verified: String = "",
    @SerializedName("is_idproof_uploaded")
    val is_idproof_uploaded: String = "",
    @SerializedName("is_idproof_notverified_message")
    val is_idproof_notverified_message: String = "",
    @SerializedName("warning_age_restriction")
    val warning_age_restriction: String = "",
    @SerializedName("offer_type")
    val offerType: Int = 0,
    var quantity: Int = 0,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("venue_id")
    val venue_id: Int = 0

):Serializable {

    override fun toString(): String {
        return "Menu(ageRestriction='$ageRestriction', cost=$cost, description='$description', id=$id, isGuestInvities=$isGuestInvities, menuImage='$menuImage', method='$method', name='$name', offerType=$offerType, quantity=$quantity, price=$price, venue_id=$venue_id)"
    }
}