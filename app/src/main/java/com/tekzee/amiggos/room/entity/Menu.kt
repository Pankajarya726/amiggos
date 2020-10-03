package com.tekzee.amiggos.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item")
class Menu(
    val ageRestriction: String = "",
    val cost: Int = 0,
    val description: String = "",
    @PrimaryKey @ColumnInfo(name = "id") val id: Int = 0,
    val isGuestInvities: Int = 0,
    val menuImage: String = "",
    val method: String = "",
    val name: String = "",
    val offerType: Int = 0,
    var quantity: Int = 0,
    val price: Double = 0.0
):Serializable {

    override fun toString(): String {
        return "Menu(ageRestriction='$ageRestriction', cost=$cost, description='$description', id=$id, isGuestInvities=$isGuestInvities, menuImage='$menuImage', method='$method', name='$name', offerType=$offerType, quantity=$quantity, price=$price)"
    }
}