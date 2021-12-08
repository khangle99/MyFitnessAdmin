package com.khangle.myfitnessadmin.nutrition.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.khangle.myfitnessadmin.base.BaseActivity
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.RESULT_BACK_RQ
import com.khangle.myfitnessadmin.common.SwipeToDeleteCallback
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.extension.slideActivityForResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BodyStatActiviy : BaseActivity() {
    val viewmodel: BodyStatVM by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: BodyStatAdapter
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_stat)
        setupUI()
        viewmodel.bodyStatList.observe(this) {
            progressBar.visibility = View.INVISIBLE
            if(it.isEmpty()) {
                Toast.makeText(baseContext, "Empty List", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(it)
        }
        viewmodel.getBodyStatList()
    }

    private fun setupUI() {
        recyclerView = findViewById(R.id.bodyStatRecycleview)
        progressBar = findViewById(R.id.nutCatProgress)
        adapter = BodyStatAdapter{
            val frag =  AddStatDialogFragment(viewmodel)
            frag.arguments = bundleOf("bodyStat" to it)
           frag.show(supportFragmentManager,"showAddStat")

        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        findViewById<ExtendedFloatingActionButton>(R.id.addBodyStat).setOnClickListener {
            AddStatDialogFragment(viewmodel).show(supportFragmentManager,"showAddStat")
        }

        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val callback = object : SwipeToDeleteCallback(baseContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.currentList[position]
                progressBar.visibility = View.VISIBLE
                viewmodel.deleteBodyStat(item) {

                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(baseContext, "Deleted Session", Toast.LENGTH_SHORT).show()
                    viewmodel.getBodyStatList()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RESULT_BACK_RQ && resultCode == RELOAD_RS) {
            viewmodel.getBodyStatList()
        }
    }
}