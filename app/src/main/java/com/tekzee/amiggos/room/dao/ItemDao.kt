package com.tekzee.amiggos.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tekzee.amiggos.room.entity.Menu

@Dao
interface ItemDao {

    @Query("SELECT * from item where venue_id = :venueid")
    fun getItemCount(venueid:String): LiveData<List<Menu>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Menu)

    @Query("SELECT * from item where id = :id")
    suspend fun getItemDetails(id: String): Menu

    @Query("SELECT * from item where item.id = :id")
    suspend fun checkItemExist(id: String) : Menu

    @Query("UPDATE item SET quantity = :quantity WHERE id = :id")
    suspend fun updateCount(quantity: String,id: String)

    @Query("DELETE FROM item where id= :itemid")
    suspend fun deleteItem(itemid: Int): Int

    @Query("DELETE FROM item")
    suspend fun clearCart()

//    @Query("SELECT * from item")
//    suspend fun getCartTotal(): LiveData<List<Menu>>
}