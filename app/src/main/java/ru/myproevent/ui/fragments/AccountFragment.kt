package ru.myproevent.ui.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.KeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentAccountBinding
import ru.myproevent.domain.model.ProfileDto
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.account.AccountPresenter
import ru.myproevent.ui.presenters.account.AccountView
import ru.myproevent.ui.presenters.main.MainView
import ru.myproevent.ui.presenters.main.Menu
import ru.myproevent.ui.views.KeyboardAwareTextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AccountFragment : BaseMvpFragment(), AccountView, BackButtonListener {
    private var _view: FragmentAccountBinding? = null
    private val view get() = _view!!

    val calendar: Calendar = Calendar.getInstance()
    var currYear: Int = calendar.get(Calendar.YEAR)
    var currMonth: Int = calendar.get(Calendar.MONTH)
    var currDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

    // TODO: рефакторинг: сделать свой DatePickerDialog, так как использование готового DatePickerDialog требует Android Nougat
    @RequiresApi(Build.VERSION_CODES.N)
    private val DateEditClickListener = View.OnClickListener {
        var pickerYear = currYear
        var pickerMonth = currMonth
        var pickerDay = currDay
        if (!view.dateOfBirthEdit.text.isNullOrEmpty()) {
            val pickerDate = GregorianCalendar().apply {
                time =
                    SimpleDateFormat(getString(R.string.dateFormat)).parse(view.dateOfBirthEdit.text.toString())
            }
            pickerYear = pickerDate.get(Calendar.YEAR)
            pickerMonth = pickerDate.get(Calendar.MONTH)
            pickerDay = pickerDate.get(Calendar.DATE)
        }
        DatePickerDialog(
            requireContext(),
            AlertDialog.THEME_HOLO_LIGHT,
            null,
            pickerYear,
            pickerMonth,
            pickerDay
        ).apply {
            setOnDateSetListener { _, year, month, dayOfMonth ->
                val gregorianCalendar = GregorianCalendar(
                    year,
                    month,
                    dayOfMonth
                )
                view.dateOfBirthEdit.text = SpannableStringBuilder(
                    // TODO: для вывода сделать local date format
                    SimpleDateFormat(getString(R.string.dateFormat)).apply {
                        calendar = gregorianCalendar
                    }.format(
                        gregorianCalendar.time
                    )
                )
            }
        }.show()
    }

    private fun showKeyBoard(view: View) {
        val imm: InputMethodManager =
            requireContext().getSystemService(InputMethodManager::class.java)
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override val presenter by moxyPresenter {
        AccountPresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    private fun setEditListeners(
        textInput: TextInputLayout,
        textEdit: KeyboardAwareTextInputEditText
    ) {
        textEdit.keyListener = null
        textInput.setEndIconOnClickListener {
            textEdit.keyListener = defaultKeyListener
            textEdit.requestFocus()
            showKeyBoard(textEdit)
            textEdit.text?.let { it1 -> textEdit.setSelection(it1.length) }
            textInput.endIconMode = END_ICON_NONE
            view.save.visibility = VISIBLE
        }
    }

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var defaultKeyListener: KeyListener

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainView).selectItem(Menu.SETTINGS)
        _view = FragmentAccountBinding.inflate(inflater, container, false).apply {
            defaultKeyListener = nameEdit.keyListener
            setEditListeners(nameInput, nameEdit)
            setEditListeners(phoneInput, phoneEdit)
            dateOfBirthEdit.keyListener = null
            dateOfBirthEdit.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    dateOfBirthEdit.performClick()
                }
            }
            dateOfBirthInput.setEndIconOnClickListener {
                dateOfBirthEdit.requestFocus()
                dateOfBirthEdit.setOnClickListener(DateEditClickListener)
                dateOfBirthEdit.performClick()
                dateOfBirthInput.endIconMode = END_ICON_NONE
                view.save.visibility = VISIBLE
            }
            setEditListeners(positionInput, positionEdit)
            setEditListeners(roleInput, roleEdit)
            save.setOnClickListener {
                presenter.saveProfile(
                    nameEdit.text.toString(),
                    phoneEdit.text.toString(),
                    dateOfBirthEdit.text.toString(),
                    positionEdit.text.toString(),
                    roleEdit.text.toString()
                )
            }
            titleButton.setOnClickListener { presenter.backPressed() }
        }
        presenter.getProfile()
        return view.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }

    override fun showProfile(profileDto: ProfileDto) {
        with(view) {
            with(profileDto) {
                fullName?.let { nameEdit.text = SpannableStringBuilder(it) }
                msisdn?.let { phoneEdit.text = SpannableStringBuilder(it) }
                birthdate?.let { dateOfBirthEdit.text = SpannableStringBuilder(it) }
                position?.let { positionEdit.text = SpannableStringBuilder(it) }
                description?.let { roleEdit.text = SpannableStringBuilder(it) }
            }
        }
    }

    override fun makeProfileEditable() {
        // TODO:
        Toast.makeText(context, "makeProfileEditable()", Toast.LENGTH_LONG).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}