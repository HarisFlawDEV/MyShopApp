package com.nst.myshopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPTextViewBold(context : Context, attributes: AttributeSet) : AppCompatTextView(context,attributes) {

    init {
        applyfont()
    }

    private fun applyfont() {

        val typeface : Typeface = Typeface.createFromAsset(context.assets,"fonts/Rubik-Medium.ttf")
        setTypeface(typeface)
        //      tv_app_name.typeface = typeface

    }


}