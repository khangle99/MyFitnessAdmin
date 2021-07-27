package com.khangle.myfitnessadmin.nutrition.menuList

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.khangle.myfitnessadmin.BaseActivity
import com.khangle.myfitnessadmin.ComposableBaseActivity
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.RESULT_BACK_RQ
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.excercise.excdetail.ExcerciseDetailActivity
import com.khangle.myfitnessadmin.excercise.exclist.ExcerciseListAdapter
import com.khangle.myfitnessadmin.excercise.exclist.ExcerciseListVM
import com.khangle.myfitnessadmin.extension.setReadOnly
import com.khangle.myfitnessadmin.extension.slideActivityForResult
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import com.khangle.myfitnessadmin.model.NutritionCategory
import com.khangle.myfitnessadmin.nutrition.detail.MenuDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuListActivity : ComposableBaseActivity() {
    lateinit var menuList: RecyclerView
    lateinit var categoryPhoto: ImageView
    lateinit var categoryName: EditText
    val viewmodel: MenuListVM by viewModels()
    var pickedUri: Uri? = null
    lateinit var currentCategoryId: String
    lateinit var adapter: MenuListAdapter
    lateinit var addMenuBtn: ExtendedFloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)
        setupUI()
        val category = intent.extras?.getParcelable<NutritionCategory>("category")
        if (category != null) {
            currentCategoryId = category.id
            loadListForCategory(category)
        }
        val stateRaw = intent.extras?.getInt("state") // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)
        viewmodel.menuList.observe(this) {
            if (it.isEmpty()) {
                Toast.makeText(baseContext, "Empty List", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(it)
        }

    }

    private fun setupUI() {
        menuList = findViewById(R.id.menuRecycler)
        categoryPhoto = findViewById(R.id.nutCategoryPhoto)
        categoryName = findViewById(R.id.nutCategoryName)
        addMenuBtn = findViewById(R.id.addMenu)
        setupEvent()
        adapter = MenuListAdapter {
            val intent = Intent(this, MenuDetailActivity::class.java)
            val bundle = bundleOf("menu" to it, "catId" to currentCategoryId)
            intent.putExtras(bundle)
            slideActivityForResult(intent, RESULT_BACK_RQ)
        }
        menuList.adapter = adapter
        menuList.layoutManager = LinearLayoutManager(this)
    }

    private fun setupEvent() {
        addMenuBtn.setOnClickListener {
            // logic khi o che do add thi them parent nay truoc khi
            val intent = Intent(this, MenuDetailActivity::class.java)
            intent.putExtras(bundleOf("catId" to currentCategoryId, "state" to UseState.ADD.raw))
            slideActivityForResult(intent, RESULT_BACK_RQ)
        }
        categoryPhoto.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "select picture"),
                99
            )
        }
    }

    private fun loadListForCategory(category: NutritionCategory) {
        viewmodel.loadList(category.id)
        categoryPhoto.load(category.photoUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
        }
        categoryName.setText(category.name)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data!!.data!!
                categoryPhoto.setImageURI(uri)
                pickedUri = uri
            } else {
                Toast.makeText(baseContext, "Cancel Pick Image", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == RESULT_BACK_RQ && resultCode == RELOAD_RS) {
            viewmodel.loadList(currentCategoryId)
        }
    }

    private fun validateInput(): Boolean { // false khi fail
        if (categoryName.getText().toString().trim { it <= ' ' }.length == 0) {
            categoryName.setError("Không được để trống tên")
            return false
        }
        if (state == UseState.ADD && pickedUri == null) {
            Toast.makeText(baseContext, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onAdded() {
        if (!validateInput()) return
        viewmodel.createNutritionCategory(
            categoryName.text.toString(),
            pickedUri!!.toString()
        ) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Thêm thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RELOAD_RS)
                finish()
            } else {
                Toast.makeText(
                    baseContext,
                    "Lỗi khi thêm error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onUpdated() {
        if (!validateInput()) return
        viewmodel.updateNutritionCategory(
            currentCategoryId,
            categoryName.text.toString(),
            pickedUri?.toString()
        ) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Update thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RELOAD_RS)
                finish()
            } else {
                Toast.makeText(
                    baseContext,
                    "Lỗi khi update error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDeleted() {
        viewmodel.deleteNutritionCategory(currentCategoryId) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Delete thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RELOAD_RS)
                finish()
            } else {
                Toast.makeText(
                    baseContext,
                    "Lỗi khi delete error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun invalidateView() {
        when (state) {
            UseState.EDIT -> {
                addMenuBtn.isVisible = true
                categoryName.setReadOnly(false)
                categoryPhoto.isClickable = true
            }
            UseState.ADD -> {
                addMenuBtn.isVisible = false
                categoryName.setReadOnly(false)
                categoryPhoto.isClickable = true
            }
            else -> {
                addMenuBtn.isVisible = false
                categoryName.setReadOnly(true)
                categoryPhoto.isClickable = false
            }
        }
    }

    override fun getManageObjectName(): String {
        return "Nutrition Category"
    }

}