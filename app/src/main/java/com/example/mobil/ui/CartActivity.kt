package com.example.mobil.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.R
import com.example.mobil.data.ApiClient
import com.example.mobil.data.AppDatabase
import com.example.mobil.data.CartRepository
import com.example.mobil.data.CatalogRepository
import com.example.mobil.model.CartDisplayItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var cartRepository: CartRepository

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cart)

        val recyclerView =
            findViewById<RecyclerView>(
                R.id.cartRecyclerView
            )

        val emptyText =
            findViewById<TextView>(
                R.id.emptyCartText
            )

        val totalText =
            findViewById<TextView>(
                R.id.totalPriceText
            )

        val clearButton =
            findViewById<Button>(
                R.id.clearCartButton
            )

        val checkoutButton =
            findViewById<Button>(
                R.id.checkoutButton
            )

        val database =
            AppDatabase.getInstance(
                applicationContext
            )

        cartRepository =
            CartRepository(
                database.cartDao()
            )

        val catalogRepository =
            CatalogRepository(
                api = ApiClient.api,
                cacheDao =
                    database.catalogCacheDao()
            )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        val adapter =
            CartAdapter(
                items = emptyList(),

                onIncrease = { item ->

                    lifecycleScope.launch {
                        cartRepository.increase(
                            item.cartEntity
                        )
                    }
                },

                onDecrease = { item ->

                    lifecycleScope.launch {
                        cartRepository.decrease(
                            item.cartEntity
                        )
                    }
                },

                onDelete = { item ->

                    lifecycleScope.launch {
                        cartRepository.delete(
                            item.cartEntity
                        )
                    }
                }
            )

        recyclerView.adapter =
            adapter

        lifecycleScope.launch {

            cartRepository
                .observeCart()
                .collectLatest { cartEntities ->

                    val catalog =
                        catalogRepository
                            .getCachedCatalog()

                    val displayItems =
                        if (catalog == null) {

                            emptyList()

                        } else {

                            cartEntities.mapNotNull { entity ->

                                val product =
                                    catalog.items.find {
                                        it.id ==
                                                entity.productId
                                    }
                                        ?: return@mapNotNull null

                                val sizeName =
                                    product.sizes.find {
                                        it.id ==
                                                entity.sizeId
                                    }?.name
                                        ?: entity.sizeId

                                CartDisplayItem(
                                    cartEntity = entity,
                                    product = product,
                                    sizeName = sizeName
                                )
                            }
                        }

                    adapter.submitItems(
                        displayItems
                    )

                    val isEmpty =
                        displayItems.isEmpty()

                    emptyText.visibility =
                        if (isEmpty) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }

                    recyclerView.visibility =
                        if (isEmpty) {
                            View.GONE
                        } else {
                            View.VISIBLE
                        }

                    clearButton.isEnabled =
                        !isEmpty

                    checkoutButton.isEnabled =
                        !isEmpty

                    val totalInKopecks =
                        displayItems.sumOf {
                            it.product.priceInKopecks *
                                    it.cartEntity.quantity
                        }

                    val rubles =
                        totalInKopecks / 100

                    totalText.text =
                        "Итого: ${
                            "%,d".format(rubles)
                                .replace(',', ' ')
                        } ₽"
                }
        }

        clearButton.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Очистить корзину?")
                .setMessage(
                    "Все товары будут удалены из корзины."
                )
                .setNegativeButton(
                    "Отмена",
                    null
                )
                .setPositiveButton(
                    "Очистить"
                ) { _, _ ->

                    lifecycleScope.launch {
                        cartRepository.clear()
                    }
                }
                .show()
        }

        checkoutButton.setOnClickListener {
            showCheckoutDialog()
        }
    }

    private fun showCheckoutDialog() {

        val view =
            LayoutInflater.from(this)
                .inflate(
                    R.layout.dialog_checkout,
                    null
                )

        val nameInput =
            view.findViewById<EditText>(
                R.id.nameInput
            )

        val emailInput =
            view.findViewById<EditText>(
                R.id.emailInput
            )

        val commentInput =
            view.findViewById<EditText>(
                R.id.commentInput
            )

        val dialog =
            AlertDialog.Builder(this)
                .setTitle("Оформление заказа")
                .setView(view)
                .setNegativeButton(
                    "Отмена",
                    null
                )
                .setPositiveButton(
                    "Оформить",
                    null
                )
                .create()

        dialog.setOnShowListener {

            val confirmButton =
                dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
                )

            confirmButton.isEnabled = false

            fun validateForm() {

                val name =
                    nameInput.text
                        .toString()
                        .trim()

                val email =
                    emailInput.text
                        .toString()
                        .trim()

                val isNameValid =
                    name.isNotEmpty()

                val isEmailValid =
                    email.isNotEmpty() &&
                            Patterns.EMAIL_ADDRESS
                                .matcher(email)
                                .matches()

                confirmButton.isEnabled =
                    isNameValid &&
                            isEmailValid
            }

            val watcher =
                object : TextWatcher {

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) = Unit

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        validateForm()
                    }

                    override fun afterTextChanged(
                        s: Editable?
                    ) = Unit
                }

            nameInput.addTextChangedListener(
                watcher
            )

            emailInput.addTextChangedListener(
                watcher
            )

            confirmButton.setOnClickListener {

                val name =
                    nameInput.text
                        .toString()
                        .trim()

                val email =
                    emailInput.text
                        .toString()
                        .trim()

                val comment =
                    commentInput.text
                        .toString()
                        .trim()

                lifecycleScope.launch {

                    cartRepository.clear()

                    dialog.dismiss()

                    showSuccessDialog(
                        name = name,
                        email = email,
                        comment = comment
                    )
                }
            }
        }

        dialog.show()
    }

    private fun showSuccessDialog(
        name: String,
        email: String,
        comment: String
    ) {

        val message =
            buildString {

                append(
                    "Спасибо, $name!\n\n"
                )

                append(
                    "Заказ успешно оформлен.\n"
                )

                append(
                    "Подтверждение будет отправлено на $email."
                )

                if (comment.isNotBlank()) {

                    append(
                        "\n\nКомментарий: $comment"
                    )
                }
            }

        AlertDialog.Builder(this)
            .setTitle("Заказ оформлен")
            .setMessage(message)
            .setPositiveButton(
                "Готово",
                null
            )
            .show()
    }
}