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
    private var emailImage: Drawable?

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.IconEditText,
            0, 0
        ).apply {
            try {
                emailImage = getDrawable(R.styleable.IconEditText_icon)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        emailImage?.let {
            setButtonDrawables(startOfTheText = it)
        }
    }

    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null){
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
        compoundDrawablePadding = 20
    }
}