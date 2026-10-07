package com.example.mobil.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart ORDER BY productId, sizeId")
    fun observeCart(): Flow<List<CartEntity>>

    @Query(
        """
        SELECT * FROM cart
        WHERE productId = :productId
        AND sizeId = :sizeId
        LIMIT 1
        """
    )
    suspend fun getItem(
        productId: String,
        sizeId: String
    ): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(item: CartEntity)

    @Query(
        """
        DELETE FROM cart
        WHERE productId = :productId
        AND sizeId = :sizeId
        """
    )
    suspend fun delete(
        productId: String,
        sizeId: String
    )

    @Query("DELETE FROM cart")
    suspend fun clear()

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart")
    fun observeTotalCount(): Flow<Int>
}