package ru.myproevent.ui.fragments

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.KeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentAccountBinding
import ru.myproevent.domain.model.ProfileDto
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.account.AccountPresenter
import ru.myproevent.ui.presenters.account.AccountView
import ru.myproevent.ui.presenters.main.MainView
import ru.myproevent.ui.presenters.main.Menu
import ru.myproevent.ui.views.KeyboardAwareTextInputEditText


class AccountFragment : BaseMvpFragment(), AccountView, BackButtonListener {
    private var _view: FragmentAccountBinding? = null
    private val view get() = _view!!

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainView).selectItem(Menu.SETTINGS)
        _view = FragmentAccountBinding.inflate(inflater, container, false).apply {
            defaultKeyListener = nameEdit.keyListener
            setEditListeners(nameInput, nameEdit)
            setEditListeners(phoneInput, phoneEdit)
            setEditListeners(dateOfBirthInput, dateOfBirthEdit)
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
        view.nameEdit.text = SpannableStringBuilder(profileDto.fullName);
    }

    override fun makeProfileEditable() {
        // TODO:
        Toast.makeText(context, "makeProfileEditable()", Toast.LENGTH_LONG).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}