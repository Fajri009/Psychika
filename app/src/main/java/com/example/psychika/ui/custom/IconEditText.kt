package com.example.psychika.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.psychika.R

class IconEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    private var iconEditText: Drawable?

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.IconEditText,
            0, 0
        ).apply {
            try {
                iconEditText = getDrawable(R.styleable.IconEditText_icon)
            } finally {
                recycle()
            }
        }
        maxLines = 1
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        iconEditText?.let {
            setEditTextDrawables(startOfTheText = it)
        }
        setPaddingRelative(dpToPx(15), dpToPx(0), dpToPx(15), dpToPx(0))
    }

    private fun setEditTextDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null){
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
        compoundDrawablePadding = 20
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}