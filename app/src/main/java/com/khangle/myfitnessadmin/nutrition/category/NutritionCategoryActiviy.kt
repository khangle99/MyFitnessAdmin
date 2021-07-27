package com.khangle.myfitnessadmin.nutrition.category

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.khangle.myfitnessadmin.BaseActivity
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.RESULT_BACK_RQ
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.excercise.exclist.ExcerciseListActivity
import com.khangle.myfitnessadmin.extension.slideActivityForResult
import com.khangle.myfitnessadmin.nutrition.menuList.MenuListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NutritionCategoryActiviy : BaseActivity() {
    val viewmodel: NutritionCategoryVM by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NutCategoryListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition_category)
        setupUI()
        viewmodel.categoryList.observe(this) {
            if(it.isEmpty()) {
                Toast.makeText(baseContext, "Empty List", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(it)
        }
        viewmodel.getCategoryList()
    }

    private fun setupUI() {
        recyclerView = findViewById(R.id.nutCategoryRecycleview)
        adapter = NutCategoryListAdapter{
            val intent = Intent(this, MenuListActivity::class.java)
            val bundle = bundleOf("category" to it)
            intent.putExtras(bundle)
            slideActivityForResult(intent, RESULT_BACK_RQ)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        findViewById<ExtendedFloatingActionButton>(R.id.addCategory).setOnClickListener {
            val intent = Intent(this, MenuListActivity::class.java)
            intent.putExtras(bundleOf("state" to UseState.ADD.raw))
            slideActivityForResult(intent, RESULT_BACK_RQ)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RESULT_BACK_RQ && resultCode == RELOAD_RS) {
            viewmodel.getCategoryList()
        }
    }
}