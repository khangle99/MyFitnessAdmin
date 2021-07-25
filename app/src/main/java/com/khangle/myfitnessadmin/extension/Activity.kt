package com.khangle.myfitnessadmin.extension

import android.app.Activity
import android.content.Intent
import com.khangle.myfitnessadmin.R

fun Activity.slideActivity(intent: Intent) {
    this.startActivity(intent)
    overridePendingTransition(
        R.anim.slide_in_right,
        R.anim.slide_out_left);
}