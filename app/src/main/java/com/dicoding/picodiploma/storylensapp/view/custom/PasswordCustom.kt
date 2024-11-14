package com.dicoding.picodiploma.storylensapp.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.storylensapp.R

class PasswordCustom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), View.OnTouchListener {

    private lateinit var clearButton: Drawable
    init {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Set hint
        hint = "Enter your password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        clearButton = ContextCompat.getDrawable(context, R.drawable.baseline_close) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Cek apakah panjang karakter kurang dari 8 atau tidak
                error = if (s.toString().length < 8) {
                    resources.getString(R.string.password_error)
                } else {
                    null
                }

                // Tampilkan atau sembunyikan tombol clear
                if (s.isNotEmpty()) showClearButton() else hideClearButton()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Tidak perlu tindakan di sini
            }

            override fun afterTextChanged(s: Editable) {
                // Tidak perlu tindakan di sini
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {  // Pastikan ikon clear button ada
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                // Jika RTL (Right-To-Left)
                clearButtonEnd = (clearButton.intrinsicWidth + paddingStart).toFloat()
                if (event.x < clearButtonEnd) isClearButtonClicked = true
            } else {
                // Jika LTR (Left-To-Right)
                clearButtonStart = (width - paddingEnd - clearButton.intrinsicWidth).toFloat()
                if (event.x > clearButtonStart) isClearButtonClicked = true
            }

            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Tampilkan clear button saat disentuh
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        // Bersihkan teks saat clear button disentuh
                        text?.clear()
                        hideClearButton()
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButton)
    }
}
