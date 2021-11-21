package ru.myproevent.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class LockableNestedScrollView : NestedScrollView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var isScrollable = false

    var touchEventCallback: ((ev: MotionEvent) -> Unit)? = null

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if(!isScrollable){
            touchEventCallback?.invoke(ev)
            return false
        }
        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isScrollable && super.onInterceptTouchEvent(ev)
    }
}