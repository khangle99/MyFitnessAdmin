package com.khangle.myfitnessadmin.extension

import android.graphics.Color
import android.widget.EditText

//fun EditText.setEnable(isEnable: Boolean) {
//    if(isEnable) {
//        this.setFocusable(true);
//        this.setEnabled(true);
//        this.setCursorVisible(true);
//        this.setKeyListener(null);
//        this.setBackgroundColor(Color.BLUE);
//    } else {
//        this.setFocusable(false);
//        this.setEnabled(false);
//        this.setCursorVisible(false);
//        this.setKeyListener(null);
//        this.setBackgroundColor(Color.TRANSPARENT);
//    }
//}

fun EditText.setReadOnly(value: Boolean) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.setCursorVisible(!value)
    this.isEnabled = !value
   // this.setBackgroundColor(if(value) Color.TRANSPARENT else Color.BLUE);
}


