package com.developerxy.wildlegion.screens

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.Menu
import android.view.MenuItem
import com.developerxy.wildlegion.R
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*

abstract class CrudActivity : BackgroundActivity() {

    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private var deleteItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?, layout: Int) {
        super.onCreate(savedInstanceState, layout)
        setupBottomSheet()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crud_menu, menu)
        deleteItem = menu?.findItem(R.id.action_delete)

        val isEditing = intent.getBooleanExtra("isEditing", false)
        if (!isEditing)
            hideDeleteMenuItem()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.action_delete -> {
            onDeleteMenuItemPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupBottomSheet() {
        val bottomSheetLayout = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetLayout.tvDetails.text = getBottomSheetMessage()
        bottomSheetLayout.cancelButton
                .setOnClickListener { mBottomSheetDialog.dismiss() }
        bottomSheetLayout.deleteButton
                .setOnClickListener {
                    mBottomSheetDialog.dismiss()
                    onDeleteConfirmed()
                }

        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog.setContentView(bottomSheetLayout)
    }

    private fun onDeleteMenuItemPressed() {
        mBottomSheetDialog.show()
    }

    fun hideDeleteMenuItem() {
        deleteItem?.isVisible = false
    }

    abstract fun getBottomSheetMessage(): String
    abstract fun onDeleteConfirmed()
}