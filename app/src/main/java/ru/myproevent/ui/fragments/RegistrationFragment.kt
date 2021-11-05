package ru.myproevent.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.DialogLicenseBinding
import ru.myproevent.databinding.FragmentRegistrationBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.registration.RegistrationPresenter
import ru.myproevent.ui.presenters.registration.RegistrationView
import java.lang.reflect.Field

class RegistrationFragment : BaseMvpFragment(), RegistrationView, BackButtonListener {
    // TODO: вынести в кастомную вьюху
    private val licenceTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> with(v as TextView) {
                setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_600))
                setTextColor(ProEventApp.instance.getColor(R.color.white))
            }
            MotionEvent.ACTION_UP -> with(v as TextView) {
                setBackgroundColor(ProEventApp.instance.getColor(R.color.white))
                setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_800))
                performClick()
            }
        }
        true
    }

    private var _view: FragmentRegistrationBinding? = null
    private val view get() = _view!!

    override val presenter by moxyPresenter {
        RegistrationPresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = FragmentRegistrationBinding.inflate(inflater, container, false).apply {
            licenseText.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                bottomSheetDialog.setContentView(
                    DialogLicenseBinding.inflate(
                        inflater,
                        container,
                        false
                    ).apply {
                        // TODO: вынести в кастомную вьюху
                        license1.setOnTouchListener(licenceTouchListener)
                        license2.setOnTouchListener(licenceTouchListener)
                    }.root
                )
                bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                // https://stackoverflow.com/questions/46861306/how-to-disable-bottomsheetdialogfragment-dragging
                try {
                    val behaviorField: Field =
                        bottomSheetDialog.javaClass.getDeclaredField("behavior")
                    behaviorField.isAccessible = true
                    val behavior = behaviorField.get(bottomSheetDialog) as BottomSheetBehavior<*>
                    behavior.setBottomSheetCallback(object :
                        BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    })
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                bottomSheetDialog.show()
            }
            licenseCheckboxHitArea.setOnClickListener { licenseCheckbox.touch() }
            continueRegistration.setOnClickListener {
                if (passwordEdit.text.toString() != passwordConfirmEdit.text.toString()) {
                    Toast.makeText(ProEventApp.instance, "Пароли не совпадают", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }
                if (emailEdit.text.toString().isEmpty()) {
                    Toast.makeText(
                        ProEventApp.instance,
                        "Поле с email не может быть пустым",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                presenter.continueRegistration(
                    licenseCheckbox.isChecked,
                    emailEdit.text.toString(),
                    passwordEdit.text.toString()
                )
            }
            ohNowIRememberHitArea.setOnClickListener { presenter.signup() }

            val licenseTextIAgree: Spannable =
                SpannableString(getString(R.string.license_i_agree_with))
            licenseTextIAgree.setSpan(
                ForegroundColorSpan(ProEventApp.instance.getColor(R.color.ProEvent_blue_800)),
                0,
                licenseTextIAgree.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            licenseText.text = licenseTextIAgree

            val licenseTextConditionOne: Spannable =
                SpannableString(getString(R.string.license_condition_1))
            licenseTextConditionOne.setSpan(
                ForegroundColorSpan(ProEventApp.instance.getColor(R.color.ProEvent_bright_orange_500)),
                0,
                licenseTextConditionOne.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            licenseText.append(licenseTextConditionOne)

            val licenseTextConditionSeparator: Spannable =
                SpannableString(getString(R.string.license_condition_separator))
            licenseTextConditionSeparator.setSpan(
                ForegroundColorSpan(ProEventApp.instance.getColor(R.color.ProEvent_blue_800)),
                0,
                licenseTextConditionSeparator.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            licenseText.append(licenseTextConditionSeparator)

            val licenseTextConditionTwo: Spannable =
                SpannableString(getString(R.string.license_condition_2))
            licenseTextConditionTwo.setSpan(
                ForegroundColorSpan(ProEventApp.instance.getColor(R.color.ProEvent_bright_orange_500)),
                0,
                licenseTextConditionTwo.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            licenseText.append(licenseTextConditionTwo)
        }
        return view.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}