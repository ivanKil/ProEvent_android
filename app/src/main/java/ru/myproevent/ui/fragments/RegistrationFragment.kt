package ru.myproevent.ui.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentRegistrationBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.registration.RegistrationPresenter
import ru.myproevent.ui.presenters.registration.RegistrationView

class RegistrationFragment : MvpAppCompatFragment(), RegistrationView, BackButtonListener {
    private var _view: FragmentRegistrationBinding? = null
    private val view get() = _view!!

    private val presenter by moxyPresenter {
        RegistrationPresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = FragmentRegistrationBinding.inflate(inflater, container, false).apply {
            licenseText.setOnClickListener{
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                bottomSheetDialog.setContentView(R.layout.dialog_license)
                bottomSheetDialog.show()
            }
            licenseCheckboxHitArea.setOnClickListener { licenseCheckbox.touch() }
            continueRegistration.setOnClickListener { presenter.continueRegistration() }

            val licenseTextIAgree: Spannable = SpannableString(getString(R.string.license_i_agree_with))
            licenseTextIAgree.setSpan(
                ForegroundColorSpan(ProEventApp.instance.getColor(R.color.PE_blue_gray_01)),
                0,
                licenseTextIAgree.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            licenseText.text = licenseTextIAgree

            val licenseTextConditionOne: Spannable = SpannableString(getString(R.string.license_condition_1))
            licenseTextConditionOne.setSpan(
                ForegroundColorSpan(ProEventApp.instance.getColor(R.color.PE_bright_red)),
                0,
                licenseTextConditionOne.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            licenseText.append(licenseTextConditionOne)

            val licenseTextConditionSeparator: Spannable = SpannableString(getString(R.string.license_condition_separator))
            licenseTextConditionSeparator.setSpan(
                ForegroundColorSpan(ProEventApp.instance.getColor(R.color.PE_blue_gray_01)),
                0,
                licenseTextConditionSeparator.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            licenseText.append(licenseTextConditionSeparator)

            val licenseTextConditionTwo: Spannable = SpannableString(getString(R.string.license_condition_2))
            licenseTextConditionTwo.setSpan(
                ForegroundColorSpan(ProEventApp.instance.getColor(R.color.PE_bright_red)),
                0,
                licenseTextConditionTwo.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            licenseText.append(licenseTextConditionTwo)
        }
        return view.root
    }

    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}