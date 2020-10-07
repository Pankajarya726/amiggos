package com.tekzee.amiggos.base.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tekzee.amiggos.room.dao.ItemDao
import com.tekzee.amiggos.room.entity.Menu

class ItemRepository(private val itemDao: ItemDao) {


    suspend fun insert(item: Menu) {
        itemDao.insert(item)
    }

    suspend fun getItemDetail(id: String): Menu {
        return itemDao.getItemDetails(id)
    }

    suspend fun getItemCount(venueid:String): LiveData<List<Menu>> {
        return itemDao.getItemCount(venueid)
    }

    suspend fun checkItemExists(id: String): Menu {
         return itemDao.checkItemExist(id)
    }

    suspend fun updateCount(quantity: String, id: String) {
        return itemDao.updateCount(quantity,id)
    }

    suspend fun getTotalCartAmount(venueid:String): LiveData<List<Menu>> {
        return itemDao.getItemCount(venueid)
//        return MutableLiveData()
    }

    suspend fun deleteitem(id: Int): Int{
        return itemDao.deleteItem(id)
    }

    suspend fun clearCart() {
        return itemDao.clearCart()
    }
}