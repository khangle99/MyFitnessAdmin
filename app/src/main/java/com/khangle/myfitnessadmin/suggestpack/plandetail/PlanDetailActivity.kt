package com.khangle.myfitnessadmin.suggestpack.plandetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.base.BaseActivity
import com.khangle.myfitnessadmin.base.ComposableBaseActivity
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.RESULT_BACK_RQ
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.excercise.excdetail.ExcerciseDetailActivity
import com.khangle.myfitnessadmin.excercise.exclist.ExcerciseListAdapter
import com.khangle.myfitnessadmin.excercise.exclist.ExcerciseListVM
import com.khangle.myfitnessadmin.extension.setReadOnly
import com.khangle.myfitnessadmin.extension.slideActivityForResult
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import com.khangle.myfitnessadmin.model.Plan
import com.khangle.myfitnessadmin.suggestpack.daydetail.DayDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanDetailActivity : ComposableBaseActivity() {
    lateinit var excerciseList: RecyclerView
    lateinit var categoryName: EditText
    val viewmodel: PlanDetailViewModel by viewModels()
    lateinit var currentCategoryId: String
    lateinit var adapter: PlanDayAdapter
    lateinit var addExcerciseBtn: ExtendedFloatingActionButton
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_detail)

        setupUI()
        val plan = intent.extras?.getParcelable<Plan>("plan")
        if (plan != null) {
            currentCategoryId = plan.id
            loadListForCategory(plan)
        } else {
            progressBar.visibility = View.INVISIBLE
        }
        val stateRaw = intent.extras?.getInt("state") // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)
        viewmodel.dayList.observe(this) {
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
        categoryName = findViewById(R.id.categoryName)
        addExcerciseBtn = findViewById(R.id.addExcercise)
        setupEvent()
        adapter = PlanDayAdapter {
            // mo den man hinh bai tap (tai su dung)
            val intent = Intent(this, DayDetailActivity::class.java)
            if (it.exc != null) {
                val bundle = bundleOf("planDay" to it, "categoryId" to currentCategoryId, "excId" to it.excId)
                intent.putExtras(bundle)
            } else {
                val bundle = bundleOf("isDeleted" to true, "categoryId" to currentCategoryId, "excId" to it.excId)
                intent.putExtras(bundle)
            }

            slideActivityForResult(intent, RESULT_BACK_RQ)
        }
        excerciseList.adapter = adapter
        excerciseList.layoutManager = LinearLayoutManager(this)
    }

    private fun setupEvent() {
        // khong add bai tap vao package o man hinh nay, add ngay man hinh excercise
//        addExcerciseBtn.setOnClickListener {
//            // di den man hinh chon bt
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 99) {
//            if (resultCode == Activity.RESULT_OK) {
//                val uri = data!!.data!!
//                categoryPhoto.setImageURI(uri)
//                pickedUri = uri
//            } else {
//                Toast.makeText(baseContext, "Cancel Pick Image", Toast.LENGTH_SHORT).show()
//            }
//        }
        if (requestCode == RESULT_BACK_RQ && resultCode == RELOAD_RS) {
            viewmodel.loadList(currentCategoryId)
        }
    }

    private fun loadListForCategory(plan: Plan) {
        viewmodel.loadList(plan.id)
        categoryName.setText(plan.description)
    }

    private fun validateInput(): Boolean { // false khi fail
        if (categoryName.getText().toString().trim { it <= ' ' }.length == 0) {
            categoryName.setError("Không được để trống tên")
            return false
        }
//        if (state == UseState.ADD && pickedUri == null) {
//            Toast.makeText(baseContext, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show()
//            return false
//        }
        return true
    }

    override fun invalidateView() {
        when (state) {
            UseState.EDIT -> {
                addExcerciseBtn.isVisible = true
                categoryName.setReadOnly(false)
            }
            UseState.ADD -> {
                addExcerciseBtn.isVisible = false
                categoryName.setReadOnly(false)
            }
            else -> {
                categoryName.setReadOnly(true)
                addExcerciseBtn.isVisible = false
            }
        }
    }

    override fun onAdded() {
        if (!validateInput()) return
        progressBar.visibility = View.VISIBLE
        viewmodel.createPlan(
            categoryName.text.toString()
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
        progressBar.visibility = View.VISIBLE
        viewmodel.updatePlan(
            currentCategoryId,
            categoryName.text.toString()
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
        viewmodel.deletePlan(currentCategoryId) { message ->
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
        return "Excercise Plan"
    }
}