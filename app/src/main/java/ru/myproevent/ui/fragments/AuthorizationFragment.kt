package ru.myproevent.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentAuthorizationBinding
import ru.myproevent.ui.presenters.authorization.AuthorizationPresenter
import ru.myproevent.ui.presenters.authorization.AuthorizationView
import ru.myproevent.ui.presenters.main.MainView

class AuthorizationFragment : BaseMvpFragment(), AuthorizationView {
    private var _view: FragmentAuthorizationBinding? = null
    private val view get() = _view!!

    private var emailInvalidError = false
    private var passwordInvalidError = false

    override val presenter by moxyPresenter {
        AuthorizationPresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = AuthorizationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainView).hideBottomNavigation()
        _view = FragmentAuthorizationBinding.inflate(inflater, container, false).apply {
            authorizationConfirm.setOnClickListener {
                presenter.authorize(emailEdit.text.toString(), passwordEdit.text.toString())
            }
            registration.setOnClickListener {
                presenter.openRegistration()
            }
            registrationHitArea.setOnClickListener {
                registration.performClick()
            }
            passwordRecovery.setOnClickListener { presenter.recoverPassword() }
            passwordRecoveryHitArea.setOnClickListener { passwordRecovery.performClick() }

            val colorState = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_active),
                    intArrayOf(android.R.attr.state_focused),
                    intArrayOf(-android.R.attr.state_focused),
                    intArrayOf(android.R.attr.state_hovered),
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled)
                ),
                intArrayOf(
                    requireContext().getColor(R.color.ProEvent_blue_300),
                    requireContext().getColor(R.color.ProEvent_blue_600),
                    requireContext().getColor(R.color.ProEvent_blue_600),
                    requireContext().getColor(R.color.ProEvent_blue_300),
                    requireContext().getColor(R.color.ProEvent_blue_300),
                    requireContext().getColor(R.color.ProEvent_blue_300)
                )
            )
            emailEdit.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    emailInvalidError = false
                    emailInput.setBoxStrokeColorStateList(colorState)

                    if (!passwordInvalidError) {
                        view.errorMessage.visibility = GONE
                    }
                }
            }
            passwordEdit.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    passwordInvalidError = false
                    passwordInput.setBoxStrokeColorStateList(colorState)

                    if (!emailInvalidError) {
                        view.errorMessage.visibility = GONE
                    }

                    passwordInput.setEndIconTintList(
                        ColorStateList(
                            arrayOf(intArrayOf()),
                            intArrayOf(requireContext().getColor(R.color.ProEvent_blue_300))
                        )
                    )
                } else {
                    passwordInput.setEndIconTintList(
                        ColorStateList(
                            arrayOf(intArrayOf()),
                            intArrayOf(requireContext().getColor(R.color.ProEvent_blue_800))
                        )
                    )
                }
            }
        }
        return view.root
    }


    override fun authorizationDataInvalid() {
        with(view) {
            val colorState = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_active),
                    intArrayOf(android.R.attr.state_focused),
                    intArrayOf(-android.R.attr.state_focused),
                    intArrayOf(android.R.attr.state_hovered),
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled)
                ),
                intArrayOf(
                    requireContext().getColor(R.color.ProEvent_bright_orange_300),
                    requireContext().getColor(R.color.ProEvent_blue_600),
                    requireContext().getColor(R.color.ProEvent_blue_600),
                    requireContext().getColor(R.color.ProEvent_bright_orange_300),
                    requireContext().getColor(R.color.ProEvent_bright_orange_300),
                    requireContext().getColor(R.color.ProEvent_bright_orange_300)
                )
            )
            emailInvalidError = true
            passwordInvalidError = true
            emailInput.setBoxStrokeColorStateList(colorState)
            passwordInput.setBoxStrokeColorStateList(colorState)
            errorMessage.text = getString(R.string.authorization_data_invalid)
            errorMessage.visibility = VISIBLE
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}