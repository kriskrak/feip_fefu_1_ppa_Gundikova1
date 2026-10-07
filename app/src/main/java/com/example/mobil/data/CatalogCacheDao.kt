package com.example.mobil.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatalogCacheDao {

    @Query("SELECT * FROM catalog_cache WHERE id = 1 LIMIT 1")
    suspend fun getCatalog(): CatalogCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCatalog(
        catalog: CatalogCacheEntity
    )
}