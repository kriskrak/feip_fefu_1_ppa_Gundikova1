package com.example.mobil

import com.example.mobil.domain.CatalogLogic
import com.example.mobil.model.CatalogData
import com.example.mobil.model.Category
import com.example.mobil.model.Product
import com.example.mobil.model.ProductSize
import org.junit.Assert
import org.junit.Test

class CatalogLogicTest {

    private val jeans =
        Category(
            id = "jeans",
            name = "Джинсы"
        )

    private val shirts =
        Category(
            id = "shirts",
            name = "Рубашки"
        )

    private val newJeans =
        createProduct(
            id = "1",
            categoryId = "jeans",
            tags = listOf("New")
        )

    private val oldJeans =
        createProduct(
            id = "2",
            categoryId = "jeans",
            tags = emptyList()
        )

    private val newShirt =
        createProduct(
            id = "3",
            categoryId = "shirts",
            tags = listOf("New")
        )

    private val catalog =
        CatalogData(
            categories =
                listOf(
                    jeans,
                    shirts
                ),
            items =
                listOf(
                    newJeans,
                    oldJeans,
                    newShirt
                )
        )

    @Test
    fun newProductsAreFilteredFromAllCategories() {

        val result =
            CatalogLogic.filterProducts(
                "Новинки",
                catalog
            )

        Assert.assertEquals(
            listOf(
                newJeans,
                newShirt
            ),
            result
        )
    }

    @Test
    fun productsAreFilteredByCategory() {

        val result =
            CatalogLogic.filterProducts(
                "Джинсы",
                catalog
            )

        Assert.assertEquals(
            listOf(
                newJeans,
                oldJeans
            ),
            result
        )
    }

    @Test
    fun unknownCategoryReturnsEmptyList() {

        val result =
            CatalogLogic.filterProducts(
                "Неизвестная категория",
                catalog
            )

        Assert.assertTrue(
            result.isEmpty()
        )
    }

    @Test
    fun priceIsFormattedInRubles() {

        Assert.assertEquals(
            "12 500 ₽",
            CatalogLogic.formatPrice(
                1_250_000
            )
        )
    }

    @Test
    fun totalPriceUsesQuantity() {

        Assert.assertEquals(
            300_000,
            CatalogLogic.calculateTotal(
                priceInKopecks = 100_000,
                quantity = 3
            )
        )
    }

    @Test
    fun blankNameIsInvalid() {

        Assert.assertFalse(
            CatalogLogic.isValidName(
                "   "
            )
        )
    }

    @Test
    fun nonBlankNameIsValid() {

        Assert.assertTrue(
            CatalogLogic.isValidName(
                "Анна"
            )
        )
    }

    @Test
    fun invalidEmailIsRejected() {

        Assert.assertFalse(
            CatalogLogic.isSimpleEmailValid(
                "abc"
            )
        )
    }

    @Test
    fun validEmailIsAccepted() {

        Assert.assertTrue(
            CatalogLogic.isSimpleEmailValid(
                "test@mail.ru"
            )
        )
    }

    private fun createProduct(
        id: String,
        categoryId: String,
        tags: List<String>
    ): Product {

        return Product(
            id = id,
            name = "Товар $id",
            shortDescription = "",
            longDescription = "",
            priceInKopecks = 100_000,
            imageUrl = "",
            tags = tags,
            sizes =
                listOf(
                    ProductSize(
                        id = "m",
                        name = "M"
                    )
                ),
            categoryId = categoryId,
            material = "",
            weight = "",
            season = "",
            countryOfOrigin = ""
        )
    }
}