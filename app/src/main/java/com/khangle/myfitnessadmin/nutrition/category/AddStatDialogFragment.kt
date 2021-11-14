package com.khangle.myfitnessadmin.nutrition.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.khangle.myfitnessadmin.R
import dagger.hilt.android.AndroidEntryPoint


class AddStatDialogFragment(private val bodyStatVM: BodyStatVM) : DialogFragment() {

    lateinit var nameEditText: EditText
    lateinit var dataTypeSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_stat_dialog, container, false)
        nameEditText = view.findViewById(R.id.nameEditText)
        dataTypeSpinner = view.findViewById(R.id.dataTypeSpinner)
        setupSpinner()
        view.findViewById<Button>(R.id.cancelBtn).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.saveBtn).setOnClickListener {
            if (!nameEditText.text.toString().equals("")) {
                postBodyStat()
                dismiss()
            } else {
                nameEditText.setError("Loi")
            }

        }

        return view
    }

    private fun setupSpinner() {
        val dataList = arrayOf("Float", "Integer")
        val dataTypeAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, dataList)
        dataTypeSpinner.adapter = dataTypeAdapter
    }

    private fun postBodyStat() {
        val activity = activity as BodyStatActiviy
        activity.progressBar.visibility = View.VISIBLE
        bodyStatVM.postBodyStat(nameEditText.text.toString(),dataTypeSpinner.selectedItem.toString()) {
            bodyStatVM.getBodyStatList()

        }

    }



}