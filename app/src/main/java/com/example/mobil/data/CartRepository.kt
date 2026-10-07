package com.example.mobil.data

class CartRepository(
    private val cartDao: CartDao
) {

    fun observeCart() =
        cartDao.observeCart()

    fun observeTotalCount() =
        cartDao.observeTotalCount()

    suspend fun add(
        productId: String,
        sizeId: String
    ) {

        val existing =
            cartDao.getItem(
                productId,
                sizeId
            )

        if (existing == null) {

            cartDao.save(
                CartEntity(
                    productId = productId,
                    sizeId = sizeId,
                    quantity = 1
                )
            )

        } else {

            cartDao.save(
                existing.copy(
                    quantity =
                        existing.quantity + 1
                )
            )
        }
    }

    suspend fun increase(
        item: CartEntity
    ) {

        cartDao.save(
            item.copy(
                quantity =
                    item.quantity + 1
            )
        )
    }

    suspend fun decrease(
        item: CartEntity
    ) {

        if (item.quantity <= 1) {

            cartDao.delete(
                item.productId,
                item.sizeId
            )

        } else {

            cartDao.save(
                item.copy(
                    quantity =
                        item.quantity - 1
                )
            )
        }
    }

    suspend fun delete(
        item: CartEntity
    ) {

        cartDao.delete(
            item.productId,
            item.sizeId
        )
    }

    suspend fun clear() {
        cartDao.clear()
    }
}