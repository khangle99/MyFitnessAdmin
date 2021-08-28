package com.khangle.myfitnessadmin.report

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.extension.slideActivity
import com.khangle.myfitnessadmin.report.mostusedexcercise.MostUseExcerciseActivity
import com.khangle.myfitnessadmin.report.mostviewmenu.MostViewMenuActivity

class ReportMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_main)
        findViewById<CardView>(R.id.rp_topExcercise).setOnClickListener {
            slideActivity(Intent(this, MostUseExcerciseActivity::class.java))
        }
        findViewById<CardView>(R.id.rp_most_view_menu).setOnClickListener {
            slideActivity(Intent(this, MostViewMenuActivity::class.java))
        }
    }
}