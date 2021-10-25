package ru.myproevent.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.*
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

    private fun getVerificationCode() = with(view) {
        digit1Edit.text.toString().toInt() * 1000 +
                digit2Edit.text.toString().toInt() * 100 +
                digit3Edit.text.toString().toInt() * 10 +
                digit4Edit.text.toString().toInt()
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
            continueRegistration.setOnClickListener {
                presenter.continueRegistration(
                    getVerificationCode()
                )
            }
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

    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}