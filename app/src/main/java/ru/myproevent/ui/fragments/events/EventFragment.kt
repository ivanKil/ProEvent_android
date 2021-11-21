package ru.myproevent.ui.fragments.events

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
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

    private var descriptionBarDistance: Int? = null

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
                    descriptionBarDistance =
                        (calculateRectOnScreen(vb.descriptionBar).top - calculateRectOnScreen(vb.scrollChild).top).toInt()
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
            scroll.isScrollable = true
//            scroll.touchEventCallback = { ev ->
//                descriptionContainer.onTouchEvent(ev)
//            }
            scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

                Log.d("[setOnScrollChangeListener]", "distance: $descriptionBarDistance")
                Log.d(
                    "[setOnScrollChangeListener]",
                    "$scrollX, $scrollY, $oldScrollX, $oldScrollY"
                )
                if (descriptionBarDistance == null) {
                    return@setOnScrollChangeListener
                }

                if (descriptionBarDistance!! in oldScrollY..scrollY || descriptionBarDistance!! in scrollY..oldScrollY) {
                    scroll.isScrollable = false
                    if (oldScrollY != descriptionBarDistance) {
                        Log.d("[scrollTo]", "$oldScrollY vs $descriptionBarDistance")
                        scroll.scrollTo(0, descriptionBarDistance!!)
                    }
                    descriptionContainer.isScrollable = true
                } else {
                    scroll.isScrollable = true
                    descriptionContainer.isScrollable = false
                }

                Log.d(
                    "[MYLOG]",
                    "descriptionContainer.isScrollable: ${descriptionContainer.isScrollable}"
                )
//
//                val bounds = Rect()
//                descriptionBar.getGlobalVisibleRect(bounds) // now bounds has the visible drawing coordinates of the view
//                //view.root.offsetDescendantRectToMyCoords(descriptionContainer, bounds) // now bounds has the view's coordinates according to the parentViewGroup
//
//                Log.d(
//                    "[setOnScrollChangeListener]",
//                    "bounds.top: ${bounds.centerY()}"
//                )
//
//                val descriptionBarLocation = IntArray(2)
//                descriptionBar.getLocationOnScreen(descriptionBarLocation)
//                if (descriptionBarLocation[1] - eventBar.height - statusBarHeight == 0) {
//                    scroll.smoothScrollBy(0,0)
//                    descriptionContainer.isScrollable = true
//                } else {
//                    descriptionContainer.isScrollable = false
//                }
//                Log.d(
//                    "[MYLOG]",
//                    "descriptionContainer.isScrollable: ${descriptionContainer.isScrollable}"
//                )
            }
            descriptionBar.setOnClickListener { expandDescription.performClick() }
            descriptionBarHitArea.setOnClickListener { descriptionBar.performClick() }
            expandMaps.setOnClickListener {
                fun isMapsExpanded() = addMapAlt.visibility == VISIBLE
                if (!isMapsExpanded()) {
                    expandMaps.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_bright_orange_300
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    addMapAlt.visibility = VISIBLE
                } else {
                    expandMaps.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ProEvent_blue_800
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    addMapAlt.visibility = GONE
                }
            }
            mapsBar.setOnClickListener { expandMaps.performClick() }
            mapBarHitArea.setOnClickListener { mapsBar.performClick() }
        }
        return vb.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }
}