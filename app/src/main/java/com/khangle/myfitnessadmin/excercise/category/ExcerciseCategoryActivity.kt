package com.khangle.myfitnessadmin.excercise.category

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import com.khangle.myfitnessadmin.BaseActivity
import com.khangle.myfitnessadmin.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class ExcerciseCategoryActivity : BaseActivity() {
    val viewmodel: ExcerciseCategoryVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise_category)
        viewmodel.categoryList.observe(this) {


        }
        test()
        // viewmodel.getCategoryList()
    }

    private fun test() {

        // kiem tra uri rong sau
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "select a picture"),
            99
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 99) {
            if(resultCode == Activity.RESULT_OK) {

               viewmodel.postCategory("sample", data!!.data.toString())

            }
        }
    }

}