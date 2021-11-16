package ru.myproevent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.myproevent.databinding.DialogProEventDatePickerBinding


class ProEventDatePickerDialog(var onDateSetListener: ((year: Int, month: Int, dayOfMonth: Int) -> Unit)? = null) :
    DialogFragment() {
    private var _view: DialogProEventDatePickerBinding? = null
    private val view get() = _view!!

    companion object {
        private const val YEAR_ARG = "YEAR"
        private const val MONTH_ARG = "MONTH"
        private const val DAY_ARG = "DAY"

        fun newInstance(year: Int, month: Int, dayOfMonth: Int): ProEventDatePickerDialog {
            val proEventDatePickerDialog = ProEventDatePickerDialog()
            val args = Bundle()
            args.putInt(YEAR_ARG, year)
            args.putInt(MONTH_ARG, month)
            args.putInt(DAY_ARG, dayOfMonth)
            proEventDatePickerDialog.arguments = args
            return proEventDatePickerDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = DialogProEventDatePickerBinding.inflate(inflater, container, false)
        return view.apply {
            with(view.datePicker) {
                with(requireArguments()) {
                    updateDate(getInt(YEAR_ARG), getInt(MONTH_ARG), getInt(DAY_ARG))
                }
                cancelButton.setOnClickListener { dismiss() }
                applyButton.setOnClickListener {
                    onDateSetListener?.let { it(year, month, dayOfMonth) }
                    dismiss()
                }
            }
        }.root
    }
}