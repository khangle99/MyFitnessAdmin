package com.khangle.myfitnessadmin.suggestpack.daydetail

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.google.android.material.chip.Chip
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.base.ComposableBaseActivity
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.excercise.excdetail.ExcerciseDetailActivity
import com.khangle.myfitnessadmin.extension.slideActivity
import com.khangle.myfitnessadmin.model.PlanDay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayDetailActivity : ComposableBaseActivity() {

    private lateinit var excNameTextView: TextView
    private lateinit var viewDetailBtn: Chip
    private lateinit var packageSpinner: Spinner
    private lateinit var daySpinner: Spinner
    private var selectedPlanId: String? = null
    private var selectedDay: String? = null
    private  var planDay: PlanDay? = null
    private val dayList = listOf("Sunday", "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
    private var isDeleted = false
    val viewModel: DayDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_detail)

        setupUI()
        val stateRaw = intent.extras?.getInt("state", 0) // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)

        if (state == UseState.ADD) {
            excNameTextView.text = intent.extras?.getString("excName")

        } else {
            isDeleted = intent.extras!!.getBoolean("isDeleted", false)
            if (isDeleted) {
                viewDetailBtn.visibility = View.INVISIBLE
                val spannableString = SpannableString("This Excercise has been deleted by admin! Please manually remove this reference")
                spannableString.setSpan(
                    ForegroundColorSpan(Color.RED),
                    0, // start
                    spannableString.length , // end
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                excNameTextView.text = spannableString
            } else {
                planDay = intent.extras?.getParcelable("planDay")!!
                loadPlanDay(planDay!!)
            }
            viewDetailBtn.setOnClickListener {
                val intent = Intent(this, ExcerciseDetailActivity::class.java)
                intent.putExtras(bundleOf("excercise" to planDay?.exc, "isViewOnly" to true))
                slideActivity(intent)
            }
        }
        setupPackageSpinner()
    }

    private fun loadPlanDay(planDay: PlanDay) {
        excNameTextView.setText(planDay.exc?.name)
        daySpinner.setSelection(planDay.day.toInt())
    }

    private fun setupUI() {
        excNameTextView = findViewById(R.id.excName)
        packageSpinner = findViewById(R.id.packageSpinner)
        daySpinner = findViewById(R.id.dayinWeekSpinner)
        viewDetailBtn = findViewById(R.id.viewExcDetailBtn)


        setupDaySpinner()
    }

    private fun setupDaySpinner() {
        daySpinner.adapter = ArrayAdapter(this,R.layout.item_spinner, dayList)
        daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedDay = position.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun setupPackageSpinner() {
        // lay toan bo plan list load vao spinner (package list)
        viewModel.getPlanList()
        viewModel.planList.observe(this) {

            val nameList = it.map { it.description }
            packageSpinner.adapter = ArrayAdapter(this,R.layout.item_spinner, nameList)
            selectedPlanId = viewModel.planList.value!![0].id
            // load selected item neu dc chon tu man hinh select
            var index = 0
            if (state == UseState.VIEW) {
                index = nameList.indexOf(planDay?.exc?.name)
            }
            packageSpinner.setSelection(index)
        }
        packageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedPlanId = viewModel.planList.value!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    override fun onAdded() {
        if (!validateInput()) return
        val categoryId = intent.extras?.getString("categoryId")!!
        val excId = intent.extras?.getString("excId")!!
        viewModel.updatePlanDay(selectedPlanId!!,categoryId,excId, selectedDay!!, "9") { message -> // 9 is create new day
            if (message.id != null) {
                Toast.makeText(
                    this,
                    "Thêm thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RELOAD_RS)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Lỗi khi thêm error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onUpdated() {
        if (isDeleted) {
            changeState(UseState.VIEW)
            return
        }
        if (!validateInput()) return
        val categoryId = intent.extras?.getString("categoryId")!!
        val excId = intent.extras?.getString("excId")!!

        viewModel.updatePlanDay(selectedPlanId!!,planDay!!.categoryId,excId, selectedDay!!,planDay!!.day) { message ->
            if (message.id != null) {
                Toast.makeText(
                    this,
                    "Thêm thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RELOAD_RS)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Lỗi khi thêm error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDeleted() {
        viewModel.deletePlanDay(selectedPlanId!!,selectedDay!!) { message ->
            if (message.id != null) {
                Toast.makeText(
                    this,
                    "Delete thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RELOAD_RS)
                finish()
            } else {
                Toast.makeText(
                   this,
                    "Lỗi khi delete error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getManageObjectName(): String {
        return "Detail Day"
    }

    override fun invalidateView() {
        when (state) {
            UseState.EDIT, UseState.ADD -> {
                excNameTextView.isEnabled = true
                packageSpinner.isEnabled = true
                daySpinner.isEnabled = true

            }
            else -> {
                excNameTextView.isEnabled = false
                packageSpinner.isEnabled = false
                daySpinner.isEnabled = false

            }
        }

        if (state == UseState.ADD) {
            viewDetailBtn.visibility = View.INVISIBLE
        } else {
            viewDetailBtn.visibility = View.VISIBLE
        }
        if (isDeleted) {
            viewDetailBtn.visibility = View.INVISIBLE
            excNameTextView.isEnabled = false
            packageSpinner.isEnabled = false
            daySpinner.isEnabled = false
        }
    }

    private fun validateInput(): Boolean { // false khi fail
//        if (noGapEditText.toString().trim { it <= ' ' }.length == 0) {
//            noGapEditText.setError("Không được để trống Gap")
//            return false
//        }
//        if (noSecEditText.getText().toString().trim { it <= ' ' }.length == 0) {
//            noSecEditText.setError("Không được để trống Sec")
//            return false
//        }
//        if (noTurnEditText.getText().toString().trim { it <= ' ' }.length == 0) {
//            noTurnEditText.setError("Không được để trống no Turn")
//            return false
//        }
        return true
    }
}

