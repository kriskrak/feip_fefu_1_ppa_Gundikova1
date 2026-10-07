package com.example.mobil.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.R
import com.example.mobil.data.ApiClient
import com.example.mobil.data.AppDatabase
import com.example.mobil.data.CatalogRepository
import com.example.mobil.model.CatalogData
import com.example.mobil.model.Product
import com.example.mobil.viewmodel.CatalogViewModel
import com.example.mobil.viewmodel.CatalogViewModelFactory
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.lifecycleScope
import com.example.mobil.data.CartRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var selectedCategory = "Новинки"

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        selectedCategory =
            savedInstanceState
                ?.getString(KEY_SELECTED_CATEGORY)
                ?: "Новинки"

        val recyclerView =
            findViewById<RecyclerView>(
                R.id.recyclerView
            )

        val openCartButton =
            findViewById<Button>(
                R.id.openCartButton
            )

        val categoryContainer =
            findViewById<LinearLayout>(
                R.id.categoryContainer
            )

        val progressBar =
            findViewById<ProgressBar>(
                R.id.progressBar
            )

        val errorContainer =
            findViewById<LinearLayout>(
                R.id.errorContainer
            )

        val retryButton =
            findViewById<Button>(
                R.id.retryButton
            )

        val database =
            AppDatabase.getInstance(
                applicationContext
            )

        val cartRepository =
            CartRepository(
                database.cartDao()
            )

        val repository =
            CatalogRepository(
                api = ApiClient.api,
                cacheDao = database.catalogCacheDao()
            )

        val viewModel =
            ViewModelProvider(
                this,
                CatalogViewModelFactory(repository)
            )[CatalogViewModel::class.java]

        recyclerView.layoutManager =
            GridLayoutManager(
                this,
                2
            )

        viewModel.isLoading.observe(this) { isLoading ->

            progressBar.visibility =
                if (isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        viewModel.isError.observe(this) { isError ->

            errorContainer.visibility =
                if (isError) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            recyclerView.visibility =
                if (isError) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }

        viewModel.isOffline.observe(this) { isOffline ->

            if (
                isOffline &&
                viewModel.catalog.value != null
            ) {

                Snackbar.make(
                    recyclerView,
                    "Нет сети. Показаны сохранённые данные",
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorView(openCartButton)
                    .show()
            }
        }

        viewModel.catalog.observe(this) { catalog ->

            showCatalog(
                catalog = catalog,
                recyclerView = recyclerView,
                categoryContainer = categoryContainer
            )
        }

        retryButton.setOnClickListener {
            viewModel.loadCatalog()
        }

        lifecycleScope.launch {

            cartRepository
                .observeTotalCount()
                .collectLatest { count ->

                    openCartButton.text =
                        if (count > 0) {
                            "Корзина ($count)"
                        } else {
                            "Корзина"
                        }
                }
        }
        openCartButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CartActivity::class.java
                )
            )
        }

        viewModel.loadCatalog()
    }


    private fun showCatalog(
        catalog: CatalogData,
        recyclerView: RecyclerView,
        categoryContainer: LinearLayout
    ) {

        categoryContainer.removeAllViews()

        val categories =
            buildList {

                add("Новинки")

                addAll(
                    catalog.categories.map {
                        it.name
                    }
                )
            }

        fun showProducts(
            products: List<Product>
        ) {

            recyclerView.adapter =
                ProductAdapter(
                    products
                ) { product ->

                    ProductDetailsBottomSheet
                        .newInstance(product)
                        .show(
                            supportFragmentManager,
                            "ProductDetails"
                        )
                }
        }

        showProducts(
            filterProducts(
                selectedCategory,
                catalog
            )
        )

        categories.forEach { categoryName ->

            val button =
                Button(this)

            button.text =
                categoryName

            button.isAllCaps =
                false

            button.setOnClickListener {

                selectedCategory =
                    categoryName

                showProducts(
                    filterProducts(
                        categoryName,
                        catalog
                    )
                )
            }

            categoryContainer.addView(
                button
            )
        }
    }

    override fun onSaveInstanceState(
        outState: Bundle
    ) {

        super.onSaveInstanceState(
            outState
        )

        outState.putString(
            KEY_SELECTED_CATEGORY,
            selectedCategory
        )
    }

    private fun filterProducts(
        categoryName: String,
        catalog: CatalogData
    ): List<Product> {

        return if (
            categoryName == "Новинки"
        ) {

            catalog.items.filter { product ->
                "New" in product.tags
            }

        } else {

            val category =
                catalog.categories.find {
                    it.name == categoryName
                }

            if (category == null) {

                emptyList()

            } else {

                catalog.items.filter { product ->
                    product.categoryId ==
                            category.id
                }
            }
        }
    }

    companion object {

        private const val
                KEY_SELECTED_CATEGORY =
            "selectedCategory"
    }
}