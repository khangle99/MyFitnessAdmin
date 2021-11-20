package com.khangle.myfitnessadmin

import android.content.Intent
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.khangle.myfitnessadmin.base.BaseActivity
import com.khangle.myfitnessadmin.excercise.category.ExcerciseCategoryActivity
import com.khangle.myfitnessadmin.extension.slideActivity
import com.khangle.myfitnessadmin.nutrition.category.BodyStatActiviy
import com.khangle.myfitnessadmin.report.ReportMainActivity
import com.khangle.myfitnessadmin.suggestpack.planlist.SuggestActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<CardView>(R.id.excerciseBtn).setOnClickListener {
            slideActivity(Intent(this, ExcerciseCategoryActivity::class.java))
        }

        findViewById<CardView>(R.id.suggestPack).setOnClickListener {
            slideActivity(Intent(this, SuggestActivity::class.java))
        }

        findViewById<CardView>(R.id.bodyStatBtn).setOnClickListener {
            slideActivity(Intent(this, BodyStatActiviy::class.java))
        }

        findViewById<CardView>(R.id.reportBtn).setOnClickListener {
            slideActivity(Intent(this, ReportMainActivity::class.java))
        }
    }


}
