package com.example.mobil.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.mobil.R
import com.example.mobil.model.Product
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import androidx.lifecycle.lifecycleScope
import com.example.mobil.data.AppDatabase
import com.example.mobil.data.CartRepository
import kotlinx.coroutines.launch

class ProductDetailsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var product: Product
    private var selectedSizeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productJson = requireArguments().getString(ARG_PRODUCT)
            ?: error("Product is missing")

        product = Gson().fromJson(
            productJson,
            Product::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.bottom_sheet_product_details,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val image = view.findViewById<ImageView>(R.id.detailsImage)
        val name = view.findViewById<TextView>(R.id.detailsName)
        val description = view.findViewById<TextView>(R.id.detailsDescription)
        val price = view.findViewById<TextView>(R.id.detailsPrice)

        val tagsContainer =
            view.findViewById<LinearLayout>(R.id.tagsContainer)

        val sizesGroup =
            view.findViewById<RadioGroup>(R.id.sizesGroup)

        val addToCartButton =
            view.findViewById<Button>(R.id.addToCartButton)

        val infoButton =
            view.findViewById<ImageButton>(R.id.infoButton)

        val closeButton =
            view.findViewById<ImageButton>(R.id.closeButton)

        Glide.with(this)
            .load(product.imageUrl)
            .into(image)

        name.text = product.name
        description.text = product.longDescription

        val priceInRubles = product.priceInKopecks / 100

        price.text =
            "%,d ₽".format(priceInRubles)
                .replace(',', ' ')

        setupTags(tagsContainer)
        setupSizes(sizesGroup)

        closeButton.setOnClickListener {
            dismiss()
        }

        infoButton.setOnClickListener {
            showProductInfo()
        }

        addToCartButton.setOnClickListener {

            val sizeId = selectedSizeId

            if (sizeId == null) {

                Toast.makeText(
                    requireContext(),
                    "Выберите размер",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val database =
                AppDatabase.getInstance(
                    requireContext()
                )

            val cartRepository =
                CartRepository(
                    database.cartDao()
                )

            viewLifecycleOwner.lifecycleScope.launch {

                cartRepository.add(
                    productId = product.id,
                    sizeId = sizeId
                )

                Toast.makeText(
                    requireContext(),
                    "Товар добавлен в корзину",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupTags(container: LinearLayout) {
        container.removeAllViews()

        product.tags.forEach { tag ->
            val textView = TextView(requireContext())

            textView.text = tag
            textView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )

            textView.textSize = 12f

            textView.setPadding(
                20,
                8,
                20,
                8
            )

            textView.setBackgroundResource(
                R.drawable.bg_product_tag
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.marginEnd = 8
            textView.layoutParams = params

            container.addView(textView)
        }
    }

    private fun setupSizes(group: RadioGroup) {
        group.removeAllViews()

        product.sizes.forEach { size ->
            val radioButton = RadioButton(requireContext())

            radioButton.text = size.name
            radioButton.id = View.generateViewId()

            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedSizeId = size.id
                }
            }

            group.addView(radioButton)
        }
    }

    private fun showProductInfo() {
        val message = """
            Материал: ${product.material}
            
            Вес: ${product.weight}
            
            Сезон: ${product.season}
            
            Страна производства: ${product.countryOfOrigin}
        """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle("Характеристики")
            .setMessage(message)
            .setPositiveButton("Закрыть", null)
            .show()
    }

    companion object {

        private const val ARG_PRODUCT = "product"

        fun newInstance(
            product: Product
        ): ProductDetailsBottomSheet {

            return ProductDetailsBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(
                        ARG_PRODUCT,
                        Gson().toJson(product)
                    )
                }
            }
        }
    }
}