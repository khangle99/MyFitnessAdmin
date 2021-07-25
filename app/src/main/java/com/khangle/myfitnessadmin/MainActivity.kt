package com.khangle.myfitnessadmin

import android.content.Intent
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.khangle.myfitnessadmin.excercise.category.ExcerciseCategoryActivity
import com.khangle.myfitnessadmin.extension.slideActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<CardView>(R.id.excerciseBtn).setOnClickListener {
            slideActivity(Intent(this, ExcerciseCategoryActivity::class.java))
        }

        findViewById<CardView>(R.id.nutritionBtn).setOnClickListener {

        }
    }


}
