package com.example.mobil.ui

import androidx.lifecycle.ViewModelProvider
import com.example.mobil.viewmodel.CatalogViewModel
import com.example.mobil.viewmodel.CatalogViewModelFactory
import android.view.View
import android.widget.ProgressBar
import android.widget.LinearLayout
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.R
import com.example.mobil.data.CatalogRepository


class MainActivity : AppCompatActivity() {
    private var selectedCategory = "Новинки"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectedCategory =
            savedInstanceState?.getString("selectedCategory") ?: "Новинки"


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val openCartButton = findViewById<Button>(R.id.openCartButton)

        val categoryContainer = findViewById<LinearLayout>(R.id.categoryContainer)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val errorContainer = findViewById<LinearLayout>(R.id.errorContainer)
        val retryButton = findViewById<Button>(R.id.retryButton)

        val repository = CatalogRepository(this)

        val viewModel = ViewModelProvider(
            this,
            CatalogViewModelFactory(repository)
        )[CatalogViewModel::class.java]

        fun loadCatalog() {
            viewModel.loadCatalog()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isError.observe(this) { isError ->
            errorContainer.visibility =
                if (isError) View.VISIBLE else View.GONE

            recyclerView.visibility =
                if (isError) View.GONE else View.VISIBLE
        }

        viewModel.catalog.observe(this) { catalog ->
            categoryContainer.removeAllViews()

            val categories = buildList {
                add("Новинки")
                addAll(catalog.categories.map { it.name })
            }

            recyclerView.layoutManager =
                androidx.recyclerview.widget.GridLayoutManager(this, 2)

            var currentAdapter = ProductAdapter(
                filterProducts(selectedCategory, catalog)
            )

            recyclerView.adapter = currentAdapter

            categories.forEach { categoryName ->
                val button = Button(this)

                button.text = categoryName
                button.isAllCaps = false

                button.setOnClickListener {
                    selectedCategory = categoryName

                    val filteredProducts = filterProducts(
                        categoryName,
                        catalog
                    )

                    currentAdapter = ProductAdapter(filteredProducts)
                    recyclerView.adapter = currentAdapter
                }

                categoryContainer.addView(button)
            }
        }

        retryButton.setOnClickListener {
            loadCatalog()
        }

        loadCatalog()


        openCartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selectedCategory", selectedCategory)
    }

    private fun filterProducts(
        categoryName: String,
        catalog: com.example.mobil.model.CatalogData
    ): List<com.example.mobil.model.Product> {

        return if (categoryName == "Новинки") {
            catalog.items.filter { product ->
                "New" in product.tags
            }
        } else {
            val category = catalog.categories.find { it.name == categoryName }

            if (category == null) {
                emptyList()
            } else {
                catalog.items.filter { product ->
                    product.categoryId == category.id
                }
            }
        }
    }

}