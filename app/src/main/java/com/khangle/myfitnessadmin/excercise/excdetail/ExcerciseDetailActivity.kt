package com.khangle.myfitnessadmin.excercise.excdetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import coil.load
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.khangle.myfitnessadmin.base.ComposableBaseActivity
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.Difficulty
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.RESULT_BACK_RQ
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.extension.setReadOnly
import com.khangle.myfitnessadmin.model.BodyStat
import com.khangle.myfitnessadmin.model.Excercise
import dagger.hilt.android.AndroidEntryPoint
import com.google.gson.JsonElement
import com.khangle.myfitnessadmin.extension.slideActivity
import com.khangle.myfitnessadmin.extension.slideActivityForResult
import com.khangle.myfitnessadmin.suggestpack.daydetail.DayDetailActivity
import org.w3c.dom.Text


@RequiresApi(Build.VERSION_CODES.N)
@AndroidEntryPoint
class ExcerciseDetailActivity : ComposableBaseActivity() {
    val viewmodel: ExcerciseDetailVM by viewModels()

    lateinit var stepLinearLayout: LinearLayout
    lateinit var achievementLinearLayout: LinearLayout
    lateinit var levelLayout: LinearLayout
    lateinit var nameEditText: EditText
    lateinit var difficultySpinner: Spinner
    lateinit var equipmentEditText: EditText

    lateinit var caloFactorEditText: EditText
    lateinit var addToPackBtn: Button

    lateinit var damEditText: EditText
    lateinit var beoEditText: EditText
    lateinit var tinhbotEditText: EditText
    lateinit var khoangEditText: EditText

    lateinit var catId: String

    lateinit var progressBar: ProgressBar
    lateinit var selectedDifficulty: Difficulty

    var excercise: Excercise? = null
    lateinit var addMoreStepBtn: Button
    lateinit var addMoreLevel: Button
    lateinit var addAchievementBtn: Button
    private var previousClickStepImageView: ImageView? = null
    private val stepTicketList = mutableListOf<View>()
    // lay info step de gui len tu 2 mang nay
    private val stepEditTextList = mutableListOf<EditText>()
    private val stepPicImageViewList = mutableListOf<ImageView>() // uri dc luu thong qua tag
    //lay index ra bang cach click vao ui xong kiem tra for each 2 mang tren
    private var achievementTicketList = mutableListOf<View>()
    private var levelTicketList = mutableListOf<View>()


    private var isPickImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise_detail)
        setupUI()
        achievementLinearLayout = findViewById(R.id.achievementLayout)
        levelLayout = findViewById(R.id.levelLayout)
        stepLinearLayout = findViewById(R.id.stepLayout)
        addAchievementBtn = findViewById(R.id.addAchievement)
        addAchievementBtn.setOnClickListener { // tam thoi de sau khi co ui quan ly body stat roi moi tiep
            onAddAchievement(true)
        }
        addMoreStepBtn = findViewById(R.id.addMoreStep)
        addMoreStepBtn.setOnClickListener {
            onAddMoreStepClick(true)
        }

        addMoreLevel = findViewById(R.id.addMoreLevel)
        addMoreLevel.setOnClickListener {
            onAddLevel(true)
        }

        val excerciserReceive = intent.extras?.getParcelable<Excercise>("excercise")
        if (excerciserReceive != null) {
            excercise = excerciserReceive
            viewmodel.bodyStatList.observe(this) {

                loadExcercise(excerciserReceive)
            }

        }
        catId = intent.extras?.getString("catId") ?: ""
        val stateRaw = intent.extras?.getInt("state") // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)

        viewmodel.getBodyStatList()


    }

    private fun onAddAchievement(isDeletable: Boolean): View {
        val achieveTicket = layoutInflater.inflate(R.layout.view_achievement_ticket, null)
        if (viewmodel.bodyStatList.value != null) {
            val array = viewmodel.bodyStatList.value!!.map {
                it.name
            }
            val adapter = ArrayAdapter(baseContext,android.R.layout.simple_spinner_item, array)
            val unitTV = achieveTicket.findViewById<TextView>(R.id.unitTV)
            val spinner = achieveTicket.findViewById<Spinner>(R.id.bodyStatSpinner)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected( parent: AdapterView<*>?,
                                             view: View?,
                                             position: Int,
                                             id: Long,) {
                    unitTV.text = viewmodel.bodyStatList.value!![position].unit

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
        val deleteBtn = achieveTicket.findViewById<Chip>(R.id.deleteAchieve)
        deleteBtn.visibility = if (isDeletable) View.VISIBLE else View.INVISIBLE
        deleteBtn.setOnClickListener {
            achievementLinearLayout.removeView(achieveTicket)
            achievementTicketList.remove(achieveTicket)
        }
        achievementTicketList.add(achieveTicket)
        achievementLinearLayout.addView(achieveTicket,achievementLinearLayout.childCount)
        return achieveTicket
    }

    private fun onAddLevel(isDeletable: Boolean): View {
        val levelTicket = layoutInflater.inflate(R.layout.view_level_ticket, null)

        val deleteBtn = levelTicket.findViewById<Chip>(R.id.removeBtn)
        deleteBtn.visibility = if (isDeletable) View.VISIBLE else View.INVISIBLE
        deleteBtn.setOnClickListener {
            levelLayout.removeView(levelTicket)
            levelTicketList.remove(levelTicket)
        }
        levelTicketList.add(levelTicket)
        levelLayout.addView(levelTicket,levelLayout.childCount)
        return levelTicket
    }

    private fun onAddMoreStepClick(isDeletable: Boolean): View {
        val stepTicket = layoutInflater.inflate(R.layout.view_tutorial_ticket, null)
        stepTicketList.add(stepTicket)
        val editText = stepTicket.findViewById<EditText>(R.id.stepTutorial)
        stepEditTextList.add(editText)
        val imageView = stepTicket.findViewById<ImageView>(R.id.stepPic)
        stepPicImageViewList.add(imageView)
        imageView.setOnClickListener {
            isPickImage = true
            previousClickStepImageView = it as ImageView
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "select picture"),
                99
            )
        }
        imageView.tag = ""

        val deleteBtn = stepTicket.findViewById<Chip>(R.id.deleteStepbtn)
        deleteBtn.visibility = if (isDeletable) View.VISIBLE else View.INVISIBLE
        deleteBtn.setOnClickListener {
            // get index
            val index = stepTicketList.indexOf(stepTicket)
            stepPicImageViewList.removeAt(index)
            stepEditTextList.removeAt(index)
            stepTicketList.removeAt(index)
            stepLinearLayout.removeView(stepTicket)

        }


        stepLinearLayout.addView(stepTicket,stepLinearLayout.childCount)
        return stepTicket
    }


    private fun setupUI() {
        nameEditText = findViewById(R.id.excName)
        addToPackBtn = findViewById(R.id.addToPackage)
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

        beoEditText = findViewById(R.id.beoET)
        khoangEditText = findViewById(R.id.vitET)
        damEditText = findViewById(R.id.damET)
        tinhbotEditText = findViewById(R.id.tinhBotET)
        caloFactorEditText = findViewById(R.id.caloFactor)

        progressBar = findViewById(R.id.excDetailProgress)
        progressBar.visibility = View.GONE

        if (intent.extras?.getBoolean("isViewOnly",false) == true) {
            addToPackBtn.visibility = View.INVISIBLE
        }
        addToPackBtn.setOnClickListener {
            val intent = Intent(this, DayDetailActivity::class.java)
            intent.putExtras(bundleOf("state" to UseState.ADD.raw, "categoryId" to excercise?.categoryId, "excId" to excercise?.id, "excName" to excercise?.name))
            slideActivityForResult(intent,97) // code 97 add to package
        }
    }


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

        if (requestCode == 97) {
            // hien ui bao ket qua add package
           if (resultCode == RELOAD_RS) {
               Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
           } else {
               Toast.makeText(this,"Cancel Add to Package", Toast.LENGTH_SHORT).show()
           }

        }
    }


    private fun loadExcercise(excercise: Excercise) {
        nameEditText.setText(excercise.name)

        selectedDifficulty = Difficulty.fromInt(excercise.difficulty)
        difficultySpinner.setSelection( excercise.difficulty)

        equipmentEditText.setText(excercise.equipment)

        caloFactorEditText.setText(excercise.caloFactor.toString())
        val nutriFactorStr = excercise.nutriFactor
        val split: Array<String> = nutriFactorStr.split("-").toTypedArray()
        val dam = split[0].toInt()
        val beo = split[1].toInt()
        val tinhbot = split[2].toInt()
        val khoang = split[3].toInt()
        beoEditText.setText(beo.toString())
        damEditText.setText(dam.toString())
        tinhbotEditText.setText(tinhbot.toString())
        khoangEditText.setText(khoang.toString())

        // load step tu cac array tutorial for de new view voi moi picurl
        excercise.tutorialWithPic.forEach { tutoText, urlString ->
            val view = onAddMoreStepClick(false)
            view.findViewById<EditText>(R.id.stepTutorial).setText(tutoText)
            view.findViewById<ImageView>(R.id.stepPic)?.let {
                it.load(urlString) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                 }
                it.tag = urlString
            }
        }

        // laod level ticket
        excercise.levelJSON.forEach { levelTitle, timeArray ->
            val view = onAddLevel(false)
            view.findViewById<EditText>(R.id.levelTitle).setText(levelTitle)
            view.findViewById<EditText>(R.id.excNoTurn).setText(timeArray[0].toString())
            view.findViewById<EditText>(R.id.excNoSec).setText(timeArray[1].toString())
            view.findViewById<EditText>(R.id.excNoGap).setText(timeArray[2].toString())
        }

        //load achievement tu json string map -> sang view voi moi entries
        val spinnerValues = viewmodel.bodyStatList.value
        val fromJson = Gson().fromJson(excercise.achieveEnsure, JsonObject::class.java)
        for ((key, value) in fromJson.entrySet()) {
           val view = onAddAchievement(false)
            view.findViewById<EditText>(R.id.promiseValue).setText(value.asString)
            // if not found show toast message
            val indexOf = spinnerValues?.indexOfFirst {
                it.name == key
            }
            if (indexOf != null && indexOf >= 0) {
                view.findViewById<Spinner>(R.id.bodyStatSpinner).setSelection(indexOf)
            } else {
                Toast.makeText(this, "Ensure stat has been renamed, please change", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun validateInput(): Boolean { // false khi fail
        if (nameEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            nameEditText.setError("Kh??ng ???????c ????? tr???ng t??n")
            return false
        }

        if (equipmentEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            equipmentEditText.setError("Kh??ng ???????c ????? tr???ng equipment")
            return false
        }

//        if (state == UseState.ADD && pickedUriStringList == null) {
//            Toast.makeText(baseContext, "Ch??a ch???n ???nh", Toast.LENGTH_SHORT).show()
//            return false
//        }

        // validate misc: factor, timing
        if (caloFactorEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            caloFactorEditText.setError("Kh??ng ???????c ????? tr???ng factor")
            return false
        }

        if (beoEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            beoEditText.setError("Kh??ng ???????c ????? tr???ng noGap")
            return false
        }
        if (damEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            damEditText.setError("Kh??ng ???????c ????? tr???ng noGap")
            return false
        }
        if (tinhbotEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            tinhbotEditText.setError("Kh??ng ???????c ????? tr???ng noGap")
            return false
        }
        if (khoangEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            khoangEditText.setError("Kh??ng ???????c ????? tr???ng noGap")
            return false
        }

        // validate achievement
        val hasEmptyPromiseField = achievementTicketList.any {
            it.findViewById<EditText>(R.id.promiseValue).text.toString().equals("")
        }
        if (hasEmptyPromiseField) {
            Toast.makeText(baseContext,"There is empty achievement value",Toast.LENGTH_SHORT).show()
            return false
        }

        // validate level
        val hasEmptyLevelField = levelTicketList.any {
            it.findViewById<EditText>(R.id.levelTitle).text.toString().equals("")
        }
        if (hasEmptyLevelField) {
            Toast.makeText(baseContext,"There is empty level title value",Toast.LENGTH_SHORT).show()
            return false
        }

        // validate step
        val hasEmptyImageView = stepPicImageViewList.any {
            it.tag?.toString().equals("")
        }


        if (hasEmptyImageView) {
            Toast.makeText(baseContext,"There is empty step image",Toast.LENGTH_SHORT).show()
            return false
        }

        val hasEmpyStepText = stepEditTextList.any {
            it.text.toString().equals("")
        }

        if (hasEmpyStepText) {
            Toast.makeText(baseContext,"There is empty step text",Toast.LENGTH_SHORT).show()
            return false
        }

        // kiem tra achievement ton tai it nhat 1, step info ton tai it nhat 1
        if (stepEditTextList.count() == 0 || stepPicImageViewList.count() == 0) {
            Toast.makeText(baseContext,"Need at leat one step",Toast.LENGTH_SHORT).show()
            return false
        }

        if (achievementTicketList.count() == 0) {
            Toast.makeText(baseContext,"Need at leat one achievement",Toast.LENGTH_SHORT).show()
            return false
        }

        // kiem tra trung achievement
        val checkList = mutableListOf<String>()
        for ( ticket in achievementTicketList) {
            val statName = ticket.findViewById<Spinner>(R.id.bodyStatSpinner).selectedItem.toString()
            if (checkList.contains(statName)) {
                Toast.makeText(baseContext,"Repeat Achievement",Toast.LENGTH_SHORT).show()
                return false
            } else {
                checkList.add(statName)
            }
        }

        return true
    }

    private fun extractAchievementJSONString(): String {
        val achievementMap = mutableMapOf<String, Any>()
        achievementTicketList.forEach {
            val statName = it.findViewById<Spinner>(R.id.bodyStatSpinner).selectedItem.toString()
            val value = it.findViewById<EditText>(R.id.promiseValue).text.toString()
            achievementMap.put(statName,value)
        }
        return Gson().toJson(achievementMap)
    }

    private fun extractLevelMap(): Map<String, Array<Int>> {
        val levelMap = mutableMapOf<String, Array<Int>>()
        levelTicketList.forEach {
            val levelTitle = it.findViewById<EditText>(R.id.levelTitle).text.toString()
            val noTurn = it.findViewById<EditText>(R.id.excNoTurn).text.toString().toInt()
            val noSec = it.findViewById<EditText>(R.id.excNoSec).text.toString().toInt()
            val noGap = it.findViewById<EditText>(R.id.excNoGap).text.toString().toInt()
            levelMap.put(levelTitle, arrayOf(noTurn, noSec, noGap))
        }
        return levelMap
    }

    private fun extractStepInfoMap(): Map<String, String> {
        val stepInfoMap = mutableMapOf<String, String>()
        stepTicketList.forEach {
            val tutorialText = it.findViewById<EditText>(R.id.stepTutorial).text.toString()
            val index = stepTicketList.indexOf(it)
            val uriString = stepPicImageViewList[index].tag.toString()
            stepInfoMap.put(tutorialText, uriString)
        }
        return stepInfoMap
    }

    override fun onAdded() {
        if (!validateInput()) return
        progressBar.visibility = View.VISIBLE
        val name = nameEditText.text.toString()
        val equip = equipmentEditText.text.toString()
        val diff = selectedDifficulty.raw

        val caloFactor = caloFactorEditText.text.toString().toFloat()
        val dam = damEditText.text.toString().toInt()
        val beo = beoEditText.text.toString().toInt()
        val khoang = khoangEditText.text.toString().toInt()
        val tinhbot = tinhbotEditText.text.toString().toInt()
        val nutriStr = "$dam-$beo-$tinhbot-$khoang"
        // get step info
        val tutorialStringList = mutableListOf<String>()
        stepEditTextList.forEach {
            tutorialStringList.add(it.text.toString())
        }
        val uriList = mutableListOf<Uri>()
        stepPicImageViewList.forEach {
            uriList.add(Uri.parse(it.tag.toString()))
        }
        // get achievement list
        val achievementJSON = extractAchievementJSONString()

        // get map level
        val levelMap = extractLevelMap()


        // get stepMap
        val tutorialArray = stepEditTextList.map {
            it.text.toString()
        }
        val uriStringList = stepPicImageViewList.map {
            it.tag.toString()
        }


        val excercise = Excercise("",name,diff,equip,caloFactor, tutorialArray, levelMap, listOf(),achievementJSON,0,
            mapOf(),catId, nutriStr)

        viewmodel.createExcercise(
            catId,
            excercise,
            uriStringList
        ) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Th??m th??nh c??ng v???i id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(
                    baseContext,
                    "L???i khi th??m error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("", "onAdded: ${message.error}")
            }
            setResult(RELOAD_RS)
            finish()
        }
    }

    override fun onUpdated() {
        if (!validateInput()) return
        progressBar.visibility = View.VISIBLE
        excercise!!.name = nameEditText.text.toString()
        excercise!!.equipment = equipmentEditText.text.toString()
        excercise!!.difficulty = selectedDifficulty.raw
        excercise!!.caloFactor = caloFactorEditText.text.toString().toFloat()
        val dam = damEditText.text.toString().toInt()
        val beo = beoEditText.text.toString().toInt()
        val khoang = khoangEditText.text.toString().toInt()
        val tinhbot = tinhbotEditText.text.toString().toInt()
        val nutriStr = "$dam-$beo-$tinhbot-$khoang"
        excercise!!.nutriFactor = nutriStr
        var uriStringList: List<String>? = null

        if (isPickImage) {
            uriStringList =  stepPicImageViewList.map {
                it.tag.toString()
            }
        }

        val tutorialArray = stepEditTextList.map {
            it.text.toString()
        }

        val levelMap = extractLevelMap()
        excercise!!.levelJSON = levelMap

        excercise!!.tutorial = tutorialArray
        val achievementJSON = extractAchievementJSONString()
        excercise!!.achieveEnsure = achievementJSON


        viewmodel.updateExcercise(
            catId,
            excercise!!.id,
            excercise!!,
            uriStringList
        ) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Update th??nh c??ng v???i id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    baseContext,
                    "L???i khi update error: ${message.error}",
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
                    "Delete th??nh c??ng v???i id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    baseContext,
                    "L???i khi delete error: ${message.error}",
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
                caloFactorEditText.setReadOnly(false)

                beoEditText.setReadOnly(false)
                damEditText.setReadOnly(false)
                tinhbotEditText.setReadOnly(false)
                khoangEditText.setReadOnly(false)
                invalidateAchievement(true)
                invalidateLevel(true)
                invalidateStepInfo(true)
//                tutorialEditText.setReadOnly(false)
//                pickImage.visibility = View.VISIBLE
                addToPackBtn.visibility = View.INVISIBLE
                addAchievementBtn.visibility = View.VISIBLE
                addMoreStepBtn.visibility = View.VISIBLE
            }
            else -> {
                nameEditText.setReadOnly(true)
                difficultySpinner.isEnabled = false
                equipmentEditText.setReadOnly(true)
                caloFactorEditText.setReadOnly(true)

                beoEditText.setReadOnly(true)
                damEditText.setReadOnly(true)
                tinhbotEditText.setReadOnly(true)
                khoangEditText.setReadOnly(true)
                invalidateAchievement(false)
                invalidateLevel(false)
                invalidateStepInfo(false)
                addToPackBtn.visibility = View.VISIBLE
                addAchievementBtn.visibility = View.INVISIBLE
                addMoreStepBtn.visibility = View.INVISIBLE
//                tutorialEditText.setReadOnly(true)
//                pickImage.visibility = View.INVISIBLE
            }
        }
    }

    private fun invalidateAchievement(isEnable: Boolean) {
        achievementTicketList.forEach {
            it.findViewById<Chip>(R.id.deleteAchieve).visibility = if (isEnable) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun invalidateLevel(isEnable: Boolean) {
        levelTicketList.forEach {
            it.findViewById<Chip>(R.id.removeBtn).visibility = if (isEnable) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun invalidateStepInfo(isEnable: Boolean) {
        stepTicketList.forEach {
            it.findViewById<Chip>(R.id.deleteStepbtn).visibility = if (isEnable) View.VISIBLE else View.INVISIBLE
        }
    }
}