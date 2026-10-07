package com.example.mobil.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        CatalogCacheEntity::class,
        CartEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun catalogCacheDao(): CatalogCacheDao

    abstract fun cartDao(): CartDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 =
            object : Migration(1, 2) {

                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {

                    db.execSQL(
                        """
                        CREATE TABLE IF NOT EXISTS cart (
                            productId TEXT NOT NULL,
                            sizeId TEXT NOT NULL,
                            quantity INTEGER NOT NULL,
                            PRIMARY KEY(productId, sizeId)
                        )
                        """.trimIndent()
                    )
                }
            }

        fun getInstance(
            context: Context
        ): AppDatabase {

            return INSTANCE
                ?: synchronized(this) {

                    val instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "mobil_database"
                        )
                            .addMigrations(
                                MIGRATION_1_2
                            )
                            .build()

                    INSTANCE = instance

                    instance
                }
        }
    }
}