package ru.myproevent.ui.fragments.events.event

import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.KeyListener
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputLayout
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentEventBinding
import ru.myproevent.databinding.ItemContactBinding
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.domain.models.entities.Address
import ru.myproevent.domain.models.entities.Contact
import ru.myproevent.domain.models.entities.Event
import ru.myproevent.domain.utils.CONTACTS_KEY
import ru.myproevent.domain.utils.PARTICIPANTS_PICKER_RESULT_KEY
import ru.myproevent.domain.utils.toProfileDto
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.fragments.ProEventMessageDialog
import ru.myproevent.ui.presenters.events.event.EventPresenter
import ru.myproevent.ui.presenters.events.event.EventView
import ru.myproevent.ui.presenters.main.BottomNavigationView
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.main.Tab
import ru.myproevent.ui.views.CenteredImageSpan
import ru.myproevent.ui.views.KeyboardAwareTextInputEditText
import java.util.*
import kotlin.properties.Delegates

// TODO: отрефакторить - разбить этот класс на кастомные вьющки и утилиты
class EventFragment : BaseMvpFragment<FragmentEventBinding>(FragmentEventBinding::inflate),
    EventView, BackButtonListener {
    private var isFilterOptionsExpanded = false

    private var event: Event? = null
    private var address: Address? = null

    // TODO: копирует поле licenceTouchListener из RegistrationFragment
    private val filterOptionTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> with(v as TextView) {
                setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_600))
                setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
            }
            MotionEvent.ACTION_UP -> with(v as TextView) {
                setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
                setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_800))
                performClick()
            }
        }
        true
    }

    private fun showFilterOptions() {
        Log.d("[MYLOG]", "eventStatus: ${event!!.eventStatus}")
        isFilterOptionsExpanded = true
        with(binding) {
            //searchEdit.hideKeyBoard() // TODO: нужно вынести это в вызов предществующий данному, чтобы тень при скрытии клавиатуры отображалась корректно
            shadow.visibility = VISIBLE
            copyEvent.visibility = VISIBLE
            if (event!!.eventStatus != Event.Status.CANCELLED && event!!.eventStatus != Event.Status.COMPLETED) {
                // TODO: появляется только если прошла последняя дата проведения, данные об этом получать с сервера
                // finishEvent.visibility = VISIBLE
                cancelEvent.visibility = VISIBLE
            } else {
                deleteEvent.visibility = VISIBLE
            }
        }
    }

    private fun hideFilterOptions() {
        isFilterOptionsExpanded = false
        with(binding) {
            shadow.visibility = GONE
            copyEvent.visibility = GONE
            finishEvent.visibility = GONE
            cancelEvent.visibility = GONE
            deleteEvent.visibility = GONE
        }
    }

    private var statusBarHeight by Delegates.notNull<Int>()

    private var descriptionBarDistance by Delegates.notNull<Int>()
    private var mapsBarDistance by Delegates.notNull<Int>()
    private var pointsBarDistance by Delegates.notNull<Int>()
    private var participantsBarDistance by Delegates.notNull<Int>()

    private fun extractStatusBarHeight(): Int {
        val rectangle = Rect()
        val window: Window = requireActivity().window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        return rectangle.top
    }

    override val presenter by moxyPresenter {
        EventPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        val EVENT_ARG = "EVENT"
        fun newInstance(event: Event? = null) = EventFragment().apply {
            arguments = Bundle().apply { putParcelable(EVENT_ARG, event) }
        }
    }

    private fun calculateRectOnScreen(view: View): RectF {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return RectF(
            location[0].toFloat(),
            location[1].toFloat(),
            (location[0] + view.measuredWidth).toFloat(),
            (location[1] + view.measuredHeight).toFloat()
        )
    }

    private fun showAbsoluteBar(
        title: String,
        iconResource: Int?,
        iconTintResource: Int?,
        onCollapseScroll: Int,
        onCollapse: () -> Unit,
        onEdit: () -> Unit
    ) =
        with(binding) {
            absoluteBar.visibility = VISIBLE
            absoluteBarHitArea.visibility = VISIBLE
            absoluteBarEdit.visibility = VISIBLE
            absoluteBarExpand.visibility = VISIBLE

            iconResource?.let {
                absoluteBarEdit.visibility = VISIBLE
                absoluteBarEdit.setImageResource(it)
            } ?: run {
                absoluteBarEdit.visibility = GONE
            }
            if (iconTintResource == null) {
                absoluteBarEdit.clearColorFilter()
            } else {
                absoluteBarEdit.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        iconTintResource
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            absoluteBar.setOnClickListener { absoluteBarExpand.performClick() }
            absoluteBarHitArea.setOnClickListener { absoluteBar.performClick() }
            absoluteBarEdit.setOnClickListener { onEdit() }
            absoluteBarExpand.setOnClickListener {
                binding.scroll.scrollTo(0, onCollapseScroll)
                binding.scroll.fling(0)
                isAbsoluteBarBarHidden = true
                onCollapse()
                hideAbsoluteBar()
            }
            absoluteBar.text = title
        }

    private var isAbsoluteBarBarHidden = true
    private fun hideAbsoluteBar() = with(binding) {
        absoluteBar.visibility = GONE
        absoluteBarHitArea.visibility = GONE
        absoluteBarEdit.visibility = GONE
        absoluteBarExpand.visibility = GONE
    }

    private fun showEditOptions() = with(binding) {
        save.visibility = VISIBLE
        saveHitArea.visibility = VISIBLE
        cancel.visibility = VISIBLE
        cancelHitArea.visibility = VISIBLE
    }

    private fun hideEditOptions() = with(binding) {
        save.visibility = GONE
        saveHitArea.visibility = GONE
        cancel.visibility = GONE
        cancelHitArea.visibility = GONE
    }

    private fun showActionOptions() = with(binding) {
        actionMenu.visibility = VISIBLE
    }

    private lateinit var defaultKeyListener: KeyListener


    private fun showKeyBoard(view: View) {
        val imm: InputMethodManager =
            requireContext().getSystemService(InputMethodManager::class.java)
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    // TODO: отрефакторить - эта функция копирует функцию из AccountFragment. Вынести это в кастомную вьюху ProEventEditText
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
            textInput.endIconMode = TextInputLayout.END_ICON_NONE
            showEditOptions()
        }
    }

    private fun lockEdit(
        textInput: TextInputLayout,
        textEdit: KeyboardAwareTextInputEditText,
        icon: Drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_edit)!!
    ) {
        textEdit.clearFocus()
        textEdit.hideKeyBoard()
        textInput.endIconMode = TextInputLayout.END_ICON_CUSTOM
        textInput.endIconDrawable = icon
        setEditListeners(textInput, textEdit)
    }

    private fun lockDescriptionEdit() = with(binding) {
        fun showAbsoluteBarEdit() {
            isAbsoluteBarBarHidden = true
            scroll.scrollBy(0, 1)
            scroll.scrollBy(0, -1)
        }
        descriptionText.keyListener = null
        descriptionText.clearFocus()
        descriptionText.hideKeyBoard()
        editDescription.visibility = VISIBLE
        showAbsoluteBarEdit()
        if (descriptionText.text.isNullOrBlank()) {
            noDescription.visibility = VISIBLE
        }
    }

    private fun setViewValues(event: Event, inflater: LayoutInflater) = with(binding) {
        with(event) {
            nameEdit.text = SpannableStringBuilder(name)
            dateEdit.text = SpannableStringBuilder(startDate.toString())
            address?.let { locationEdit.text = SpannableStringBuilder(it.addressLine) }
                ?: this@EventFragment.address?.let {
                    locationEdit.text = SpannableStringBuilder(it.addressLine)
                }
            if (!description.isNullOrBlank()) {
                descriptionText.text = SpannableStringBuilder(description)
                noDescription.visibility = GONE
                descriptionText.visibility = VISIBLE
            } else {
                descriptionText.text = SpannableStringBuilder("")
                noDescription.visibility = VISIBLE
                descriptionText.visibility = GONE
            }
            if (participantsUserIds != null && participantsUserIds!!.isNotEmpty()) {
                Log.d("[VIEWSTATE]", "setViewValues presenter.initParticipantsProfiles")
                binding.noParticipants.isVisible = false
                presenter.initParticipantsProfiles(participantsUserIds!!)
            }
        }
    }

    override fun clearParticipants() = with(binding) {
        Log.d("[VIEWSTATE]", "clearParticipants")
        if (participantsContainer.childCount > 1) {
            participantsContainer.removeViews(1, participantsContainer.childCount - 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(
            PARTICIPANTS_PICKER_RESULT_KEY,
            this
        ) { _, bundle ->
            binding.noParticipants.isVisible = false
            showEditOptions()
            binding.participantsContainer.isVisible = true
            val participantsContacts = bundle.getParcelableArray(CONTACTS_KEY)!! as Array<Contact>
            presenter.loadParticipantsProfiles(participantsContacts.map { it.toProfileDto() }
                .toTypedArray())
        }

        parentFragmentManager.setFragmentResultListener(
            AddEventPlaceFragment.ADD_EVENT_PLACE_REQUEST_KEY,
            this
        ) { _, bundle ->
            val address =
                bundle.getParcelable<Address>(AddEventPlaceFragment.ADD_EVENT_PLACE_RESULT)
            event?.let { it.address = address } ?: run { this.address = address }
            if (address != null) binding.locationEdit.setText(address.addressLine)
            showEditOptions()
        }


        arguments?.getParcelable<Event>(EVENT_ARG)?.let { event = it }
    }

    private var isSaveAvailable = true

    private val participantsIds = arrayListOf<Long>()

    override fun addParticipantItemView(profileDto: ProfileDto) {
        Log.d("[VIEWSTATE]", "addParticipantItemView")
        val view = ItemContactBinding.inflate(layoutInflater)
        profileDto.fullName?.let {
            view.tvName.text = "#${profileDto.userId} $it"
        } ?: profileDto.nickName?.let {
            view.tvName.text = "#${profileDto.userId} $it"
        } ?: run {
            view.tvName.text = "#${profileDto.userId}"
        }
        view.tvDescription.text = profileDto.description
        binding.participantsContainer.addView(view.root)
        participantsIds.add(profileDto.userId)
    }

    private fun setImageSpan(view: TextView, text: String, iconRes: Int) {
        val span: Spannable = SpannableString(text)
        val image = CenteredImageSpan(
            requireContext(),
            iconRes
        )
        span.setSpan(image, 21, 22, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        view.text = span
    }

    private fun saveCallback(successEvent: Event?) {
        isSaveAvailable = true
        binding.save.setTextColor(resources.getColor(R.color.ProEvent_bright_orange_500))
        successEvent?.let {
            event = it
            binding.title.text = it.name
        }
    }

    private fun isDescriptionExpanded() = binding.descriptionContainer.visibility == VISIBLE

    private fun expandDescriptionContent() = with(binding) {
        expandDescription.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.ProEvent_bright_orange_300
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
        descriptionContainer.visibility = VISIBLE
        scroll.post {
            scroll.smoothScrollTo(0, descriptionBarDistance)
        }
    }

    override fun onViewCreated(view: View, saved: Bundle?) {
        Log.d("[EventFragment]", "onViewCreated")
        super.onViewCreated(view, saved)
        statusBarHeight = extractStatusBarHeight()
        with(binding) {
            event?.let { title.text = it.name }
            title.setOnClickListener {
                // TODO: отрефакторить
                // https://github.com/terrakok/Cicerone/issues/106
                val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
                val prev: Fragment? = parentFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val newFragment: DialogFragment =
                    ProEventMessageDialog.newInstance(title.text.toString())
                newFragment.show(ft, "dialog")
            }
            actionMenu.setOnClickListener {
                if (!isFilterOptionsExpanded) {
                    showFilterOptions()
                } else {
                    hideFilterOptions()
                }
            }
            actionMenuHitArea.setOnClickListener {
                if (actionMenu.isVisible) {
                    actionMenu.performClick()
                }
            }
            shadow.setOnClickListener { hideFilterOptions() }
            expandDescription.setOnClickListener {
                if (!isDescriptionExpanded()) {
                    expandDescriptionContent()
                } else {
                    expandDescription.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_blue_800
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    descriptionContainer.visibility = GONE
                }
            }
            descriptionBar.setOnClickListener { expandDescription.performClick() }
            descriptionBarHitArea.setOnClickListener { descriptionBar.performClick() }
            expandMaps.setOnClickListener {
                fun isMapsExpanded() = mapsContainer.visibility == VISIBLE
                if (!isMapsExpanded()) {
                    expandMaps.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_bright_orange_300
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    mapsContainer.visibility = VISIBLE
                } else {
                    expandMaps.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_blue_800
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    mapsContainer.visibility = GONE
                }
            }
            mapsBar.setOnClickListener { expandMaps.performClick() }
            mapBarHitArea.setOnClickListener { mapsBar.performClick() }
            expandPoints.setOnClickListener {
                fun isPointsExpanded() = pointsContainer.visibility == VISIBLE
                if (!isPointsExpanded()) {
                    expandPoints.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_bright_orange_300
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    pointsContainer.visibility = VISIBLE
                } else {
                    expandPoints.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_blue_800
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    pointsContainer.visibility = GONE
                }
            }
            pointsBar.setOnClickListener { expandPoints.performClick() }
            pointsBarHitArea.setOnClickListener { pointsBar.performClick() }
            expandParticipants.setOnClickListener {
                fun isParticipantsExpanded() = participantsContainer.visibility == VISIBLE
                if (!isParticipantsExpanded()) {
                    expandParticipants.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_bright_orange_300
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    participantsContainer.visibility = VISIBLE
                } else {
                    expandParticipants.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_blue_800
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    participantsContainer.visibility = GONE
                }
            }
            participantsBar.setOnClickListener { expandParticipants.performClick() }
            participantsBarHitArea.setOnClickListener { participantsBar.performClick() }

            scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                Log.d("[MYLOG]", "setOnScrollChangeListener")
                if (descriptionContainer.visibility == VISIBLE && scrollY in descriptionBarDistance..(descriptionBarDistance + descriptionContainer.height)) {
                    if (isAbsoluteBarBarHidden) {
                        showAbsoluteBar(
                            "Описание",
                            if (editDescription.visibility == VISIBLE) {
                                R.drawable.ic_edit
                            } else {
                                null
                            },
                            R.color.ProEvent_blue_800,
                            descriptionBarDistance,
                            { expandDescription.performClick() },
                            { editDescription.performClick() }
                        )
                        isAbsoluteBarBarHidden = false
                    }
                } else if (mapsContainer.visibility == VISIBLE && scrollY in mapsBarDistance..(mapsBarDistance + mapsContainer.height)) {
                    if (isAbsoluteBarBarHidden) {
                        showAbsoluteBar(
                            "Карта мероприятия",
                            R.drawable.ic_add,
                            null,
                            mapsBarDistance,
                            { expandMaps.performClick() },
                            { addMap.performClick() }
                        )
                        isAbsoluteBarBarHidden = false
                    }
                } else if (pointsContainer.visibility == VISIBLE && scrollY in pointsBarDistance..(pointsBarDistance + pointsContainer.height)) {
                    if (isAbsoluteBarBarHidden) {
                        showAbsoluteBar(
                            "Точки",
                            R.drawable.ic_add,
                            null,
                            pointsBarDistance,
                            { expandPoints.performClick() },
                            { addPoint.performClick() }
                        )
                        isAbsoluteBarBarHidden = false
                    }
                } else if (participantsContainer.visibility == VISIBLE && scrollY in participantsBarDistance..(participantsBarDistance + participantsContainer.height)) {
                    if (isAbsoluteBarBarHidden) {
                        showAbsoluteBar(
                            "Участники",
                            R.drawable.ic_add,
                            null,
                            pointsBarDistance,
                            { expandParticipants.performClick() },
                            { addParticipant.performClick() }
                        )
                        isAbsoluteBarBarHidden = false
                    }
                } else if (!isAbsoluteBarBarHidden) {
                    hideAbsoluteBar()
                    isAbsoluteBarBarHidden = true
                }
            }
            save.setOnClickListener {
                if (!isSaveAvailable) {
                    return@setOnClickListener
                }
                isSaveAvailable = false
                save.setTextColor(resources.getColor(R.color.PE_blue_gray_03))
                val participantsItems = participantsContainer.children.iterator().apply { next() }
                event?.let { it ->
                    it.name = nameEdit.text.toString()
                    it.startDate = Calendar.getInstance().time
                    it.endDate = Calendar.getInstance().time
                    it.description = descriptionText.text.toString()
                    it.participantsUserIds = participantsIds.toLongArray()
                    presenter.editEvent(it, ::saveCallback)
                } ?: run {
                    presenter.addEvent(
                        nameEdit.text.toString(),
                        Calendar.getInstance().time,
                        Calendar.getInstance().time,
                        address,
                        descriptionText.text.toString(),
                        participantsIds.toLongArray(),
                        ::saveCallback
                    )
                }
            }
            saveHitArea.setOnClickListener { save.performClick() }
            defaultKeyListener = nameEdit.keyListener
            if (event != null) {
                setViewValues(event!!, layoutInflater)
                lockEdit(nameInput, nameEdit)
                nameEdit.addTextChangedListener {
                    title.text = it
                }
                lockEdit(
                    dateInput,
                    dateEdit,
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_calendar)!!
                )
                lockDescriptionEdit()
                showActionOptions()
            } else {
                showEditOptions()
            }

            lockEdit(locationInput, locationEdit)
            locationInput.setEndIconOnClickListener {
                presenter.addEventPlace(event?.address ?: address)
            }

            back.setOnClickListener { presenter.onBackPressed() }
            backHitArea.setOnClickListener { back.performClick() }
            cancel.setOnClickListener {
                if (event == null) {
                    back.performClick()
                } else {
                    showMessage("Изменения отменены")
                    hideEditOptions()
                    noParticipants.isVisible = true
                    presenter.clearParticipants()
                    setViewValues(event!!, layoutInflater)
                    lockEdit(nameInput, nameEdit)
                    lockEdit(
                        dateInput,
                        dateEdit,
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_calendar
                        )!!
                    )
                    lockEdit(locationInput, locationEdit)
                    lockDescriptionEdit()
                }
            }
            cancelHitArea.setOnClickListener { cancel.performClick() }
            copyEvent.setOnTouchListener(filterOptionTouchListener)
            copyEvent.setOnClickListener {
                event?.let {
                    presenter.copyEvent(it)
                }
                hideFilterOptions()
            }
            finishEvent.setOnTouchListener(filterOptionTouchListener)
            finishEvent.setOnClickListener {
                presenter.finishEvent(event!!)
                hideFilterOptions()
            }
            cancelEvent.setOnTouchListener(filterOptionTouchListener)
            cancelEvent.setOnClickListener {
                presenter.cancelEvent(event!!)
                hideFilterOptions()
            }
            deleteEvent.setOnTouchListener(filterOptionTouchListener)
            deleteEvent.setOnClickListener {
                presenter.deleteEvent(event!!)
                hideFilterOptions()
            }
            setImageSpan(
                noDescription,
                "Отсутствует.\nНажмите / чтобы добавить.",
                R.drawable.ic_edit_blue // TODO: отрефакорить нужно передавать tint, а не использовать отдельный drawable
            )
            setImageSpan(
                noMaps,
                "Отсутствует.\nНажмите + чтобы добавить.",
                R.drawable.ic_add
            )
            setImageSpan(
                noPoints,
                "Отсутствуют.\nНажмите + чтобы добавить.",
                R.drawable.ic_add
            )
            setImageSpan(
                noParticipants,
                "Отсутствуют.\nНажмите + чтобы добавить.",
                R.drawable.ic_add
            )
            editDescription.setOnClickListener {
                showEditOptions()
                if (!isDescriptionExpanded()) {
                    expandDescriptionContent()
                }
                editDescription.visibility = GONE
                absoluteBarEdit.visibility = GONE
                descriptionText.keyListener = defaultKeyListener
                noDescription.visibility = GONE
                descriptionText.visibility = VISIBLE
                descriptionText.requestFocus()
                descriptionText.text?.let { text -> descriptionText.setSelection(text.length) }
                showKeyBoard(descriptionText)
                editDescription.post {
                    // TODO: разобраться как заставить это работать при первом нажатии
                    scroll.scrollTo(0, descriptionBarDistance)
                }
            }
            addMap.setOnClickListener { showMessage("addMap\nДанная возможность пока не доступна") }
            addPoint.setOnClickListener { showMessage("addPoint\nДанная возможность пока не доступна") }
            addParticipant.setOnClickListener { presenter.pickParticipants() }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                } else {
//                    view.viewTreeObserver.removeGlobalOnLayoutListener(this)
//                }

                //Log.d("[EventFragment]", "onGlobalLayout _vb: $_vb")

                // TODO: здесь вместо viewBinding используется view.findViewById, потому что, если использовать viewBinding, то по непонятоной мне причине если
                //              выйти их этого экрана (нажав кнопку back) и вновь его открыть, то _vb будет null - несмотря на то, что в конце onCreateView он имел значение.
                //              Этого не происходит если предварительно вызвать view.viewTreeObserver.removeOnGlobalLayoutListener(this), но тогда также по непонятной мне прчине
                //              calculateRectOnScreen возращает занчения не соответствующие значениям на конечной view,
                //              то есть растояния barDistances не соответствуют тем, что отображаются на экране
                //              Желательно разобраться почему это происходит и использовать здесь viewBinding

                descriptionBarDistance =
                    (calculateRectOnScreen(view.findViewById(R.id.description_bar)).top - calculateRectOnScreen(
                        view.findViewById(R.id.scroll_child)
                    ).top).toInt()
                mapsBarDistance =
                    (calculateRectOnScreen(view.findViewById(R.id.maps_bar)).top - calculateRectOnScreen(
                        view.findViewById(R.id.scroll_child)
                    ).top).toInt()
                pointsBarDistance =
                    (calculateRectOnScreen(view.findViewById(R.id.points_bar)).top - calculateRectOnScreen(
                        view.findViewById(R.id.scroll_child)
                    ).top).toInt()
                participantsBarDistance =
                    (calculateRectOnScreen(view.findViewById(R.id.participants_bar)).top - calculateRectOnScreen(
                        view.findViewById(R.id.scroll_child)
                    ).top).toInt()
            }
        })
    }

    override fun showMessage(message: String) {
        Toast.makeText(ProEventApp.instance, message, Toast.LENGTH_LONG).show()
    }
}