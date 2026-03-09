package com.example.mobil.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.R
import com.example.mobil.model.Product

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val openCartButton = findViewById<Button>(R.id.openCartButton)

        val products = listOf(
            Product(
                1,
                "Футболка",
                1500.0,
                "Натуральный мягкий хлопок, отлично подойдет для повседневной носки.",
                "https://files.indiwd.com/app/products/220/gallery/66d88fcb47d9a.jpg",
                "clothing"
            ),
            Product(
                2,
                "Рюкзак",
                3000.0,
                "Легкий, вместительный, практичный.",
                "https://cdn1.bosco.ru/upload/iblock/44f/44f9187e8aeedc0cf45216a3d2e04783_502_660.jpg",
                "accessories"
            ),
            Product(
                3,
                "Пуховик мужской",
                6000.0,
                "Подойдет даже для самых суровых морозов.",
                "https://ae04.alicdn.com/kf/S5c7174da05214d629d5c33677646b73ab.jpg_480x480.jpg",
                "clothing"
            ),
            Product(
                4,
                "Сумка для ноутбука",
                2500.0,
                "Стильная сумка, для вашего ноутбука и документов.",
                "https://avatars.mds.yandex.net/get-mpic/16060605/2a00000196d39573fb84108daf146dfd8c59/orig",
                "accessories"
            ),
            Product(
                5,
                "Женский пиджак",
                3500.0,
                "элегантный, стильный пиджак. Только натуральные ткани.",
                "https://lamcdn.net/wonderzine.com/post_image-image/fk4-9RgrUSqmLa31RY3bNw.png",
                "clothing"
            ),
            Product(
                6,
                "Брюки женские",
                3000.0,
                "Отличный офисный вариант",
                "https://byme.ru/images/detailed/125/ab3c7122791b11ec97785820b1d8b32c_bdce622179c511ec97785820b1d8b32c.jpg",
                "clothing"
            ),
            Product(
                7,
                "Футболка женская",
                1500.0,
                "Комфортная летняя футболка. Отличный вариант для пляжного отдыха.",
                "https://s7.stc.all.kpcdn.net/woman/wp-content/uploads/2023/04/belye-zhenskie-futbolki-uniqlo.com_.png",
                "clothing"
            ),
            Product(
                8,
                "Браслет серебряный",
                7000.0,
                "Минималистичный и стильный.",
                "https://g5.sunlight.net/media/products/07e1a6820f46f8636e92a998c313b51c4a07a1ac.jpg",
                "accessories"
            )
        )

        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 2)
        recyclerView.adapter = ProductAdapter(products)

        openCartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}