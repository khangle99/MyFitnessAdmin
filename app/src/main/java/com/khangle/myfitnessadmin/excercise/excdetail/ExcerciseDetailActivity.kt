package com.khangle.myfitnessadmin.excercise.excdetail

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.constraintlayout.solver.state.State
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.chip.Chip
import com.khangle.myfitnessadmin.base.ComposableBaseActivity
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.Difficulty
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.RESULT_BACK_RQ
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.extension.setReadOnly
import com.khangle.myfitnessadmin.model.Excercise
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExcerciseDetailActivity : ComposableBaseActivity() {
    val viewmodel: ExcerciseDetailVM by viewModels()

    lateinit var container: LinearLayout
    lateinit var nameEditText: EditText
    lateinit var difficultySpinner: Spinner
    lateinit var equipmentEditText: EditText
//    lateinit var tutorialEditText: EditText
//    lateinit var imageRecyclerView: RecyclerView
//    lateinit var imageListAdapter: ImageRecyclerviewAdapter
    lateinit var catId: String
//    lateinit var pickImage: Button
    lateinit var progressBar: ProgressBar
    lateinit var selectedDifficulty: Difficulty
    var pickedUriStringList: List<String>? = null
    var excercise: Excercise? = null
    lateinit var addMoreStepBtn: Button
    lateinit var addAchievementBtn: Button
    private var previousClickStepImageView: ImageView? = null
    private val stepTicketList = mutableListOf<View>()
    // lay info step de gui len tu 2 mang nay
    private val stepEditTextList = mutableListOf<EditText>()
    private val stepPicImageViewList = mutableListOf<ImageView>() // uri dc luu thong qua tag
    //lay index ra bang cach click vao ui xong kiem tra for each 2 mang tren
    private fun indexOf(editText: EditText): Int {
        for ((index, edit) in stepEditTextList.withIndex()) {
            if (edit === editText) {
                return index
            }
        }
        return -1
    }

    private fun indexOf(imageView: ImageView): Int {
        for ((index, imgView) in stepPicImageViewList.withIndex()) {
            if (imgView === imageView) {
                return index
            }
        }
        return -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise_detail)
        setupUI()
        container = findViewById(R.id.detailContainer)
        addAchievementBtn = findViewById(R.id.addAchievement)
        addAchievementBtn.setOnClickListener { // tam thoi de sau khi co ui quan ly body stat roi moi tiep
            onAddAchievement()
        }
        addMoreStepBtn = findViewById(R.id.addMoreStep)
        addMoreStepBtn.setOnClickListener {
            onAddMoreStepClick()
        }

        val excerciserReceive = intent.extras?.getParcelable<Excercise>("excercise")
        if (excerciserReceive != null) {
            excercise = excerciserReceive
            loadExcercise(excerciserReceive)
        }
        catId = intent.extras?.getString("catId") ?: ""
        val stateRaw = intent.extras?.getInt("state") // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)
    }

    private fun onAddAchievement() {
        TODO("Not yet implemented")
    }

    private fun onAddMoreStepClick() {
        val stepTicket = layoutInflater.inflate(R.layout.view_tutorial_ticket, null)
        stepTicketList.add(stepTicket)
        val editText = stepTicket.findViewById<EditText>(R.id.stepTutorial)
        stepEditTextList.add(editText)
        val imageView = stepTicket.findViewById<ImageView>(R.id.stepPic)
        stepPicImageViewList.add(imageView)
        imageView.setOnClickListener {
            previousClickStepImageView = it as ImageView
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "select picture"),
                99
            )
        }

        val deleteBtn = stepTicket.findViewById<Chip>(R.id.deleteStepbtn)
        deleteBtn.setOnClickListener {
            // get index
            val index = stepTicketList.indexOf(stepTicket)
            stepPicImageViewList.removeAt(index)
            stepEditTextList.removeAt(index)
            stepTicketList.removeAt(index)
            container.removeView(stepTicket)
        }

        container.addView(stepTicket,container.childCount - 1)

    }


    private fun setupUI() {
        nameEditText = findViewById(R.id.excName)
        difficultySpinner = findViewById(R.id.excDifficulty)
        difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedDifficulty = Difficulty.fromInt(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
        val difficultyStringList = Difficulty.values().map { it.name }
        difficultySpinner.adapter = ArrayAdapter(baseContext,R.layout.item_spinner, difficultyStringList)
        equipmentEditText = findViewById(R.id.excEquipment)
       // tutorialEditText = findViewById(R.id.excTutorial)
//        imageRecyclerView = findViewById(R.id.imageList)
//        pickImage = findViewById<Button>(R.id.pickImage)
        progressBar = findViewById(R.id.excDetailProgress)
        progressBar.visibility = View.GONE
//        pickImage.setOnClickListener {
//                val intent = Intent()
//                intent.type = "image/*"
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                intent.action = Intent.ACTION_GET_CONTENT
//                startActivityForResult(
//                    Intent.createChooser(intent, "select pictures"),
//                    99
//                )
//            }
//        imageListAdapter = ImageRecyclerviewAdapter()
//        imageRecyclerView.adapter = imageListAdapter
//        imageRecyclerView.layoutManager = LinearLayoutManager(baseContext,RecyclerView.HORIZONTAL, false)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 99) {
//            if (resultCode == Activity.RESULT_OK) {
//                val uriList = getUriListFromData(data)
//                pickedUriStringList = uriList.map { it.toString()  }
////                imageListAdapter.applyUrlList(pickedUriStringList!!)
//            } else {
//                Toast.makeText(baseContext, "Cancel Pick Image", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data!!.data!!
                previousClickStepImageView?.setImageURI(uri)
                previousClickStepImageView?.tag = uri.toString()
            } else {
                Toast.makeText(baseContext, "Cancel Pick Image", Toast.LENGTH_SHORT).show()
            }
        }
//        if (requestCode == RESULT_BACK_RQ && resultCode == RELOAD_RS) {
//            viewmodel.loadList(currentCategoryId)
//        }
    }


    private fun getUriListFromData(data: Intent?): List<Uri> {
        val list = mutableListOf<Uri>()
        if(data!!.clipData != null) {
            val count = data.clipData!!.itemCount
            for (index in 0..count - 1) {
                data.clipData?.getItemAt(index)?.uri?.let { list.add(it) }
            }
        } else {
            val uri = data.data!!
            list.add(uri)
        }
        return list
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.save) {
//            progressBar.visibility = View.VISIBLE
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun loadExcercise(excercise: Excercise) {
        nameEditText.setText(excercise.name)

        selectedDifficulty = Difficulty.fromInt(excercise.difficulty)
        difficultySpinner.setSelection( excercise.difficulty)

        equipmentEditText.setText(excercise.equipment)
//        tutorialEditText.setText(excercise.tutorial)
//        imageListAdapter.applyUrlList(excercise.picSteps)
    }

    private fun validateInput(): Boolean { // false khi fail
        if (nameEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            nameEditText.setError("Không được để trống tên")
            return false
        }
//        if (difficultyEditText.getText().toString().trim { it <= ' ' }.length == 0) {
//            difficultyEditText.setError("Không được để trống difficulty")
//            return false
//        }
        if (equipmentEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            equipmentEditText.setError("Không được để trống equipment")
            return false
        }



        if (state == UseState.ADD && pickedUriStringList == null) {
            Toast.makeText(baseContext, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onAdded() {

        if (!validateInput()) return
        progressBar.visibility = View.VISIBLE
        val name = nameEditText.text.toString()
        val equip = equipmentEditText.text.toString()
        val diff = selectedDifficulty.raw
        // get step info
        val tutorialStringList = mutableListOf<String>()
        stepEditTextList.forEach {
            tutorialStringList.add(it.text.toString())
        }
        val uriList = mutableListOf<Uri>()
        stepPicImageViewList.forEach {
            uriList.add(Uri.parse(it.tag.toString()))
        }
        //



//        val tutor = tutorialEditText.text.toString()
//        val excercise = Excercise("",name,diff,equip,tutor, listOf(), 0)
//        viewmodel.createExcercise(
//            catId,
//            excercise,
//            pickedUriStringList!!
//        ) { message ->
//            if (message.id != null) {
//                Toast.makeText(
//                    baseContext,
//                    "Thêm thành công với id: ${message.id}",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//            } else {
//                Toast.makeText(
//                    baseContext,
//                    "Lỗi khi thêm error: ${message.error}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            setResult(RELOAD_RS)
//            finish()
//        }
    }

    override fun onUpdated() {
        if (!validateInput()) return
        progressBar.visibility = View.VISIBLE
        excercise!!.name = nameEditText.text.toString()
        excercise!!.equipment = equipmentEditText.text.toString()
        excercise!!.difficulty = selectedDifficulty.raw
 //       excercise!!.tutorial = tutorialEditText.text.toString()
        viewmodel.updateExcercise(
            catId,
            excercise!!.id,
            excercise!!,
            pickedUriStringList
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
        viewmodel.deleteExcercise(catId, excercise!!.id) { message ->
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
        return "Excercise"
    }

    override fun invalidateView() {
        when (state) {
            UseState.EDIT, UseState.ADD -> {
                nameEditText.setReadOnly(false)
                difficultySpinner.isEnabled = true
                equipmentEditText.setReadOnly(false)
//                tutorialEditText.setReadOnly(false)
//                pickImage.visibility = View.VISIBLE
            }
            else -> {
                nameEditText.setReadOnly(true)
                difficultySpinner.isEnabled = false
                equipmentEditText.setReadOnly(true)
//                tutorialEditText.setReadOnly(true)
//                pickImage.visibility = View.INVISIBLE
            }
        }
    }
}