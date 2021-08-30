package com.khangle.myfitnessadmin.excercise.exclist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.khangle.myfitnessadmin.base.ComposableBaseActivity
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.RESULT_BACK_RQ
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.excercise.excdetail.ExcerciseDetailActivity
import com.khangle.myfitnessadmin.extension.setReadOnly
import com.khangle.myfitnessadmin.extension.slideActivityForResult
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExcerciseListActivity : ComposableBaseActivity() {
    lateinit var excerciseList: RecyclerView
    lateinit var categoryPhoto: ImageView
    lateinit var categoryName: EditText
    val viewmodel: ExcerciseListVM by viewModels()
    var pickedUri: Uri? = null
    lateinit var currentCategoryId: String
    lateinit var adapter: ExcerciseListAdapter
    lateinit var addExcerciseBtn: ExtendedFloatingActionButton
    lateinit var progressBar: ProgressBar
    lateinit var guideTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise_list)

        setupUI()
        val category = intent.extras?.getParcelable<ExcerciseCategory>("category")
        if (category != null) {
            currentCategoryId = category.id
            loadListForCategory(category)
        } else {
            progressBar.visibility = View.INVISIBLE
        }
        val stateRaw = intent.extras?.getInt("state") // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)
        viewmodel.excerciseList.observe(this) {
            progressBar.visibility = View.INVISIBLE
            if (it.isEmpty()) {
                Toast.makeText(baseContext, "Empty List", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(it)
        }

    }


    private fun setupUI() {
        progressBar = findViewById(R.id.excListProgress)
        excerciseList = findViewById(R.id.excerciseRecycler)
        categoryPhoto = findViewById(R.id.categoryPhoto)
        categoryName = findViewById(R.id.categoryName)
        addExcerciseBtn = findViewById(R.id.addExcercise)
        guideTV = findViewById(R.id.guideTv)
        setupEvent()
        adapter = ExcerciseListAdapter {
            val intent = Intent(this, ExcerciseDetailActivity::class.java)
            val bundle = bundleOf("excercise" to it, "catId" to currentCategoryId)
            intent.putExtras(bundle)
            slideActivityForResult(intent,RESULT_BACK_RQ)
        }
        excerciseList.adapter = adapter
        excerciseList.layoutManager = LinearLayoutManager(this)
    }

    private fun setupEvent() {
        addExcerciseBtn.setOnClickListener {
            // logic khi o che do add thi them parent nay truoc khi
            val intent = Intent(this, ExcerciseDetailActivity::class.java)
            intent.putExtras(bundleOf("catId" to currentCategoryId, "state" to UseState.ADD.raw))
            slideActivityForResult(intent,RESULT_BACK_RQ)
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

    private fun loadListForCategory(category: ExcerciseCategory) {
        viewmodel.loadList(category.id)
        categoryPhoto.load(category.photoUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
        }
        categoryName.setText(category.name)
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

    override fun invalidateView() {
        when (state) {
            UseState.EDIT -> {
                addExcerciseBtn.isVisible = true
                categoryName.setReadOnly(false)
                categoryPhoto.isClickable = true
                guideTV.isVisible = true
            }
            UseState.ADD -> {
                addExcerciseBtn.isVisible = false
                categoryName.setReadOnly(false)
                categoryPhoto.isClickable = true
                guideTV.isVisible = true
            }
            else -> {
                categoryName.setReadOnly(true)
                addExcerciseBtn.isVisible = false
                categoryPhoto.isClickable = false
                guideTV.isVisible = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            progressBar.visibility = View.VISIBLE
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAdded() {
        if (!validateInput()) return
        viewmodel.createExcerciseCategory(
            categoryName.text.toString(),
            pickedUri!!.toString()
        ) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Thêm thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    baseContext,
                    "Lỗi khi thêm error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setResult(RELOAD_RS)
            finish()
        }
    }

    override fun onUpdated() {
        if (!validateInput()) return
        viewmodel.updateCategory(
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
            } else {
                Toast.makeText(
                    baseContext,
                    "Lỗi khi update error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setResult(RELOAD_RS)
            finish()
        }
    }

    override fun onDeleted() {
        progressBar.visibility = View.VISIBLE
        viewmodel.deleteCategory(currentCategoryId) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Delete thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    baseContext,
                    "Lỗi khi delete error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setResult(RELOAD_RS)
            finish()
        }
    }

    override fun getManageObjectName(): String {
        return "Excercise Category"
    }
}