package ru.myproevent.ui.fragments.events

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.content.ContextCompat
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentEventBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.events.event.EventPresenter
import ru.myproevent.ui.presenters.events.event.EventView
import ru.myproevent.ui.presenters.main.BottomNavigationView
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.main.Tab
import kotlin.properties.Delegates
import android.graphics.RectF
import ru.myproevent.databinding.ItemPointBinding

import android.view.ViewTreeObserver.OnGlobalLayoutListener


class EventFragment : BaseMvpFragment(), EventView, BackButtonListener {
    private var _vb: FragmentEventBinding? = null
    private val vb get() = _vb!!

    private var isFilterOptionsExpanded = false

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
        isFilterOptionsExpanded = true
        with(vb) {
            //searchEdit.hideKeyBoard() // TODO: нужно вынести это в вызов предществующий данному, чтобы тень при скрытии клавиатуры отображалась корректно
            shadow.visibility = VISIBLE
            finishEvent.visibility = VISIBLE
            copyEvent.visibility = VISIBLE
        }
    }

    private fun hideFilterOptions() {
        isFilterOptionsExpanded = false
        with(vb) {
            shadow.visibility = GONE
            finishEvent.visibility = GONE
            copyEvent.visibility = GONE
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
        fun newInstance() = EventFragment()
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
        iconResource: Int,
        iconTintResource: Int?,
        onCollapseScroll: Int,
        onCollapse: () -> Unit
    ) =
        with(vb) {
            absoluteBarEdit.setImageResource(iconResource)
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
            absoluteBarExpand.setOnClickListener {
                vb.scroll.scrollTo(0, onCollapseScroll)
                vb.scroll.fling(0)
                isAbsoluteBarBarHidden = true
                onCollapse()
                hideAbsoluteBar()
            }
            absoluteBar.setOnClickListener { absoluteBarExpand.performClick() }
            absoluteBarHitArea.setOnClickListener { absoluteBar.performClick() }

            absoluteBar.text = title

            absoluteBar.visibility = VISIBLE
            absoluteBarHitArea.visibility = VISIBLE
            absoluteBarEdit.visibility = VISIBLE
            absoluteBarExpand.visibility = VISIBLE
        }

    private var isAbsoluteBarBarHidden = true
    private fun hideAbsoluteBar() = with(vb) {
        absoluteBar.visibility = GONE
        absoluteBarHitArea.visibility = GONE
        absoluteBarEdit.visibility = GONE
        absoluteBarExpand.visibility = GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        with(requireActivity() as BottomNavigationView) {
            checkTab(Tab.EVENTS)
        }
        statusBarHeight = extractStatusBarHeight()
        _vb = FragmentEventBinding.inflate(inflater, container, false).apply {
            finishEvent.setOnTouchListener(filterOptionTouchListener)
            finishEvent.setOnClickListener {
                presenter.finishEvent()
                hideFilterOptions()
            }
            copyEvent.setOnTouchListener(filterOptionTouchListener)
            copyEvent.setOnClickListener {
                presenter.copyEvent()
                hideFilterOptions()
            }
            actionMenu.setOnClickListener {
                if (!isFilterOptionsExpanded) {
                    showFilterOptions()
                } else {
                    hideFilterOptions()
                }
            }
            actionMenuHitArea.setOnClickListener { actionMenu.performClick() }
            shadow.setOnClickListener { hideFilterOptions() }
            expandDescription.setOnClickListener {
                fun isDescriptionExpanded() = descriptionContainer.visibility == VISIBLE
                if (!isDescriptionExpanded()) {
                    expandDescription.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_bright_orange_300
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    descriptionContainer.visibility = VISIBLE
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
                if (descriptionContainer.visibility == VISIBLE && scrollY in descriptionBarDistance..(descriptionBarDistance + descriptionContainer.height)) {
                    if (isAbsoluteBarBarHidden) {
                        showAbsoluteBar(
                            "Описание",
                            R.drawable.ic_edit,
                            R.color.ProEvent_blue_800,
                            descriptionBarDistance
                        ) { vb.expandDescription.performClick() }
                        isAbsoluteBarBarHidden = false
                    }
                } else if (mapsContainer.visibility == VISIBLE && scrollY in mapsBarDistance..(mapsBarDistance + mapsContainer.height)) {
                    if (isAbsoluteBarBarHidden) {
                        showAbsoluteBar(
                            "Карта мероприятия",
                            R.drawable.ic_add,
                            null,
                            mapsBarDistance
                        ) { vb.expandMaps.performClick() }
                        isAbsoluteBarBarHidden = false
                    }
                } else if (pointsContainer.visibility == VISIBLE && scrollY in pointsBarDistance..(pointsBarDistance + pointsContainer.height)) {
                    if (isAbsoluteBarBarHidden) {
                        showAbsoluteBar(
                            "Точки",
                            R.drawable.ic_add,
                            null,
                            pointsBarDistance
                        ) { vb.expandPoints.performClick() }
                        isAbsoluteBarBarHidden = false
                    }
                } else if (participantsContainer.visibility == VISIBLE && scrollY in participantsBarDistance..(participantsBarDistance + participantsContainer.height)) {
                    if (isAbsoluteBarBarHidden) {
                        showAbsoluteBar(
                            "Участники",
                            R.drawable.ic_add,
                            null,
                            pointsBarDistance
                        ) { vb.expandParticipants.performClick() }
                        isAbsoluteBarBarHidden = false
                    }
                } else if (!isAbsoluteBarBarHidden) {
                    hideAbsoluteBar()
                    isAbsoluteBarBarHidden = true
                }
            }
            for (i in 1..50) {
                // TODO: делать это асинхронно, показывая progressBar
                val pointItem = ItemPointBinding.inflate(inflater, container, false)
                if (i != 1) {
                    pointItem.name.text = "Новая точка $i"
                }
                pointsContainer.addView(pointItem.root)
            }
            for (i in 1..25) {
                // TODO: делать это асинхронно, показывая progressBar
                val pointItem = ItemPointBinding.inflate(inflater, container, false)
                pointItem.name.text = if (i != 1) { "Новый участник $i" } else { "Новый участник" }
                participantsContainer.addView(pointItem.root)
            }
        }
        return vb.root
    }

    override fun onViewCreated(view: View, saved: Bundle?) {
        super.onViewCreated(view, saved)
        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                } else {
//                    view.viewTreeObserver.removeGlobalOnLayoutListener(this)
//                }
                descriptionBarDistance =
                    (calculateRectOnScreen(vb.descriptionBar).top - calculateRectOnScreen(vb.scrollChild).top).toInt()
                mapsBarDistance =
                    (calculateRectOnScreen(vb.mapsBar).top - calculateRectOnScreen(vb.scrollChild).top).toInt()
                pointsBarDistance =
                    (calculateRectOnScreen(vb.pointsBar).top - calculateRectOnScreen(vb.scrollChild).top).toInt()
                participantsBarDistance =
                    (calculateRectOnScreen(vb.participantsBar).top - calculateRectOnScreen(vb.scrollChild).top).toInt()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }
}