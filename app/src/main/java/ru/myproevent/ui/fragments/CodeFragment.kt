package ru.myproevent.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentCodeBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.code.CodePresenter
import ru.myproevent.ui.presenters.code.CodeView
import ru.myproevent.ui.views.KeyboardAwareTextInputEditText
import java.lang.StringBuilder

class CodeFragment : MvpAppCompatFragment(), CodeView, BackButtonListener {
    private var _view: FragmentCodeBinding? = null
    private val view get() = _view!!

    private inner class SingleDigitEditKeyListener(
        private val prevViewInRow: KeyboardAwareTextInputEditText?,
        private val nextViewInRow: KeyboardAwareTextInputEditText?
    ) : View.OnKeyListener {
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        nextViewInRow?.requestFocus() ?: return false
                        return true
                    }
                    KeyEvent.KEYCODE_DEL -> {
                        prevViewInRow?.requestFocus()
                        prevViewInRow?.setSelection(prevViewInRow.length())
                        return false
                    }
                }
            }
            return false
        }
    }

    private inner class SingleDigitEditTextWatcher(
        private val nextViewInRow: KeyboardAwareTextInputEditText?
    ) : TextWatcher {
        private var lastValue = ""
        private var isFirstValue = true
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
            lastValue = s.toString()
        }

        override fun afterTextChanged(s: Editable) {
            if (isFirstValue) {
                isFirstValue = false
                nextViewInRow?.requestFocus()
            }
            var newValue = ""
            if (s.toString().length > 1) {
                newValue = if (s[0].toString() == lastValue) {
                    s.subSequence(1, 2).toString()
                } else {
                    s.subSequence(0, 1).toString()
                }
                s.replace(0, s.length, lastValue)
            }
            if (newValue.isNotEmpty()) {
                nextViewInRow?.requestFocus()
                nextViewInRow?.text = SpannableStringBuilder(newValue)
            }
        }
    }

    private val numberStringBuilder = StringBuilder()

    private fun getVerificationCode() = with(view) {
        numberStringBuilder.clear()
        numberStringBuilder.append(digit1Edit.text)
        numberStringBuilder.append(digit2Edit.text)
        numberStringBuilder.append(digit3Edit.text)
        numberStringBuilder.append(digit4Edit.text)
        return@with numberStringBuilder.toString().toIntOrNull()
    }

    private val presenter by moxyPresenter {
        CodePresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = CodeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = FragmentCodeBinding.inflate(inflater, container, false).apply {
            codeExplanation.text = String.format(getString(R.string.code_explanation_text), presenter.getEmail())
            continueRegistration.setOnClickListener {
                getVerificationCode()?.let { code -> presenter.continueRegistration(code) } ?: run {
                    Toast.makeText(
                        ProEventApp.instance.applicationContext,
                        "Код не может быть пустым или содержать не числовые символы",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            authorize.setOnClickListener { presenter.authorize() }
            ohNowIRemember.setOnClickListener { authorize.performClick() }
            ohNowIRememberHitArea.setOnClickListener { authorize.performClick() }
            back.setOnClickListener { presenter.backPressed() }
            // TODO: отрефакторить - вынести это в кастомные вьюхи
            digit1Edit.selectionChangedListener =
                { _, _ -> digit1Edit.setSelection(digit1Edit.length()) }
            digit1Edit.addTextChangedListener(SingleDigitEditTextWatcher(digit2Edit))
            digit2Edit.selectionChangedListener =
                { _, _ -> digit2Edit.setSelection(digit2Edit.length()) }
            digit2Edit.addTextChangedListener(SingleDigitEditTextWatcher(digit3Edit))
            digit3Edit.selectionChangedListener =
                { _, _ -> digit3Edit.setSelection(digit3Edit.length()) }
            digit3Edit.addTextChangedListener(SingleDigitEditTextWatcher(digit4Edit))
            digit4Edit.selectionChangedListener =
                { _, _ -> digit4Edit.setSelection(digit4Edit.length()) }
            digit4Edit.addTextChangedListener(SingleDigitEditTextWatcher(null))
            digit4Edit.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    digit4Edit.hideKeyBoard()
                }
            }
            digit1Edit.setOnKeyListener(SingleDigitEditKeyListener(null, digit2Edit))
            digit2Edit.setOnKeyListener(SingleDigitEditKeyListener(digit1Edit, digit3Edit))
            digit3Edit.setOnKeyListener(SingleDigitEditKeyListener(digit2Edit, digit4Edit))
            digit4Edit.setOnKeyListener(SingleDigitEditKeyListener(digit3Edit, null))
            back.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN ->
                        with((v as MaterialButton)) {
                            setTextColor(ProEventApp.instance.getColor(R.color.PE_bright_red))
                            val colorState = ColorStateList(
                                arrayOf(
                                    intArrayOf(-android.R.attr.state_focused),
                                ),
                                intArrayOf(
                                    requireContext().getColor(R.color.PE_bright_red),
                                )
                            )
                            back.strokeColor = colorState
                        }
                    MotionEvent.ACTION_UP -> {
                        (v as MaterialButton).setTextColor(ProEventApp.instance.getColor(R.color.PE_peach_04))
                        val colorState = ColorStateList(
                            arrayOf(
                                intArrayOf(-android.R.attr.state_focused),
                            ),
                            intArrayOf(
                                requireContext().getColor(R.color.PE_peach_04),
                            )
                        )
                        back.strokeColor = colorState
                        v.performClick()
                    }
                }
                return@setOnTouchListener false
            }
        }
        return view.root
    }

    override fun onResume() {
        super.onResume()
        // Почему-то если нажать "Уже есть аккаунт",
        // а потом с помощью кнопки back вернуться на этот экран,
        // то digit4Edit получает фокус
        view.digit4Edit.clearFocus()
    }

    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}