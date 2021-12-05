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
import com.khangle.myfitnessadmin.model.BodyStat
import dagger.hilt.android.AndroidEntryPoint


class AddStatDialogFragment(private val bodyStatVM: BodyStatVM) : DialogFragment() {

    lateinit var nameEditText: EditText
    lateinit var unitEditText: EditText
    lateinit var dataTypeSpinner: Spinner
    private var isUpdate = false
    private var bodyId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_stat_dialog, container, false)
        nameEditText = view.findViewById(R.id.nameEditText)
        unitEditText = view.findViewById(R.id.unitEditText)
        dataTypeSpinner = view.findViewById(R.id.dataTypeSpinner)
        setupSpinner()

        val bodyStat =  arguments?.getParcelable<BodyStat>("bodyStat")
        bodyStat?.let { stat ->
            isUpdate = true
            bodyId = stat.id
            nameEditText.setText(stat.name)
            unitEditText.setText(stat.unit)
            val index = bodyStatVM.bodyStatList.value?.indexOfFirst { it.unit.equals(stat.unit) } ?: 0
            dataTypeSpinner.setSelection(index)
        }

        view.findViewById<Button>(R.id.cancelBtn).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.saveBtn).setOnClickListener {
            var isError = false;
            if (nameEditText.text.toString().equals("")) {
                isError = true
                nameEditText.setError("Loi")
            }

            if (unitEditText.text.toString().equals("")) {
                isError = true
                unitEditText.setError("Loi")
            }

            if (!isError) {
                if (isUpdate) {
                    updateBodyStat()
                } else {
                    postBodyStat()
                }

                dismiss()
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
        bodyStatVM.postBodyStat(nameEditText.text.toString(),dataTypeSpinner.selectedItem.toString(), unitEditText.text.toString()) {
            bodyStatVM.getBodyStatList()

        }

    }

    private fun updateBodyStat() {
        val activity = activity as BodyStatActiviy
        activity.progressBar.visibility = View.VISIBLE
        bodyId?.let {
            bodyStatVM.updateBodyStat(it,nameEditText.text.toString(),dataTypeSpinner.selectedItem.toString(), unitEditText.text.toString()) {
                bodyStatVM.getBodyStatList()

            }
        }


    }



}