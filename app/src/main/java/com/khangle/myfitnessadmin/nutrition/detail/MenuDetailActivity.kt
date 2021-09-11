package com.khangle.myfitnessadmin.nutrition.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.khangle.myfitnessadmin.base.ComposableBaseActivity
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.excercise.excdetail.ImageRecyclerviewAdapter
import com.khangle.myfitnessadmin.extension.setReadOnly
import com.khangle.myfitnessadmin.model.Menu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuDetailActivity : ComposableBaseActivity() {
    val viewmodel: MenuDetailVM by viewModels()
    lateinit var nameEditText: EditText
    lateinit var breakfastEditText: EditText
    lateinit var lunchEditText: EditText
    lateinit var dinnerEditText: EditText
    lateinit var snackEditText: EditText
    lateinit var otherEditText: EditText
    lateinit var imageRecyclerView: RecyclerView
    lateinit var imageListAdapter: ImageRecyclerviewAdapter
    lateinit var catId: String
    lateinit var pickImageBtn: Button
    lateinit var progressBar: ProgressBar
    var pickedUriStringList: List<String>? = null
    var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_detail)
        setupUI()
        val menuReceive = intent.extras?.getParcelable<Menu>("menu")
        if (menuReceive != null) {
            menu = menuReceive
            loadMenu(menuReceive)
        }
        catId = intent.extras?.getString("catId") ?: ""
        val stateRaw = intent.extras?.getInt("state") // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)
    }

    private fun setupUI() {
        nameEditText = findViewById(R.id.menuName)
        breakfastEditText = findViewById(R.id.breakfast)
        lunchEditText = findViewById(R.id.lunch)
        dinnerEditText = findViewById(R.id.dinner)
        snackEditText = findViewById(R.id.snack)
        otherEditText = findViewById(R.id.other)
        imageRecyclerView = findViewById(R.id.imageList)
        pickImageBtn = findViewById(R.id.pickImage)
        progressBar = findViewById(R.id.menuDetailProgress)
        progressBar.visibility = View.GONE
        pickImageBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "select pictures"),
                99
            )
        }
        imageListAdapter = ImageRecyclerviewAdapter()
        imageRecyclerView.adapter = imageListAdapter
        val flexboxLayoutManager = FlexboxLayoutManager(baseContext)
        imageRecyclerView.layoutManager = LinearLayoutManager(baseContext, RecyclerView.HORIZONTAL,false)
    }
    private fun loadMenu(menu: Menu) {
        nameEditText.setText(menu.name)
        breakfastEditText.setText(menu.breakfast)
        lunchEditText.setText(menu.lunch)
        dinnerEditText.setText(menu.dinner)
        otherEditText.setText(menu.other)
        snackEditText.setText(menu.snack)
        imageListAdapter.applyUrlList(menu.picUrls)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK) {
                val uriList = getUriListFromData(data)
                pickedUriStringList = uriList.map { it.toString()  }
                imageListAdapter.applyUrlList(pickedUriStringList!!)
            } else {
                Toast.makeText(baseContext, "Cancel Pick Image", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun validateInput(): Boolean { // false khi fail
        if (nameEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            nameEditText.setError("Không được để trống tên")
            return false
        }
        if (breakfastEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            breakfastEditText.setError("Không được để trống buổi sáng ")
            return false
        }
        if (lunchEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            lunchEditText.setError("Không được để trống buổi trưa ")
            return false
        }
        if (dinnerEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            dinnerEditText.setError("Không được để trống buổi tối")
            return false
        }
        // khong check other
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
        val breakfast = breakfastEditText.text.toString()
        val lunch = lunchEditText.text.toString()
        val dinner = dinnerEditText.text.toString()
        val snack = snackEditText.text.toString()
        val other = otherEditText.text.toString()
        val menu =  Menu("",name,breakfast,lunch,dinner,snack,other, listOf(), 0)
        viewmodel.createMenu(
            catId,
            menu,
            pickedUriStringList!!
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
        menu!!.name = nameEditText.text.toString()
        menu!!.breakfast = breakfastEditText.text.toString()
        menu!!.lunch = lunchEditText.text.toString()
        menu!!.dinner = dinnerEditText.text.toString()
        menu!!.other = otherEditText.text.toString()
        menu!!.snack = snackEditText.text.toString()
        viewmodel.updateMenu(
            catId,
            menu!!.id,
            menu!!,
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
        viewmodel.deleteMenu(catId, menu!!.id) { message ->
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

    override fun invalidateView() {
        when (state) {
            UseState.EDIT, UseState.ADD -> {
                nameEditText.setReadOnly(false)
                breakfastEditText.setReadOnly(false)
                lunchEditText.setReadOnly(false)
                dinnerEditText.setReadOnly(false)
                otherEditText.setReadOnly(false)
                snackEditText.setReadOnly(false)
                pickImageBtn.visibility = View.VISIBLE
            }
            else -> {
                nameEditText.setReadOnly(true)
                breakfastEditText.setReadOnly(true)
                lunchEditText.setReadOnly(true)
                dinnerEditText.setReadOnly(true)
                otherEditText.setReadOnly(true)
                snackEditText.setReadOnly(true)
                pickImageBtn.visibility = View.INVISIBLE
            }
        }
    }

    override fun getManageObjectName(): String {
        return "Menu"
    }
}