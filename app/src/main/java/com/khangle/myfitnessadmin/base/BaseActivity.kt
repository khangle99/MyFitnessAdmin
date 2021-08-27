package com.khangle.myfitnessadmin.base

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.UseState

open class BaseActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        );
    }
}

abstract class ComposableBaseActivity : BaseActivity() {
    lateinit var state: UseState
    abstract fun onAdded()
    abstract fun onUpdated()
    abstract fun onDeleted()
    abstract fun getManageObjectName(): String
    abstract fun invalidateView()

    fun changeState(state: UseState) {
        this.state = state
        invalidateView()
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        invalidateMenu(menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    private fun invalidateMenu(menu: Menu) {
        when (state) {
            UseState.ADD -> {
                menu.findItem(R.id.save).isVisible = true
                menu.findItem(R.id.edit).isVisible = false
                menu.findItem(R.id.cancel).isVisible = true
                menu.findItem(R.id.delete).isVisible = false
            }
            UseState.EDIT -> {
                menu.findItem(R.id.save).isVisible = true
                menu.findItem(R.id.edit).isVisible = false
                menu.findItem(R.id.cancel).isVisible = true
                menu.findItem(R.id.delete).isVisible = false
            }
            UseState.VIEW -> {
                menu.findItem(R.id.save).isVisible = false
                menu.findItem(R.id.edit).isVisible = true
                menu.findItem(R.id.cancel).isVisible = false
                menu.findItem(R.id.delete).isVisible = true
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (state === UseState.ADD) {
                    onAdded()
                } else { // state Edit
                    onUpdated()
                }
                return true
            }
            R.id.edit -> {
                changeState(UseState.EDIT)
                return true
            }
            R.id.cancel -> {
                if (state === UseState.ADD) {
                    finish()
                } else if (state === UseState.EDIT) {
                    changeState(UseState.VIEW)
                }
                return true
            }
            R.id.delete -> AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Remove ${getManageObjectName()}")
                .setMessage("Are you sure you want to remove this ${getManageObjectName()}?")
                .setPositiveButton("Yes") { _, _ ->
                  onDeleted()
                }
                .setNegativeButton("No", null)
                .show()
        }
        return super.onOptionsItemSelected(item)
    }
}