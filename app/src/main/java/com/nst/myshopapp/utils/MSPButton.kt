package com.nst.myshopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MSPButton (context: Context, attributeSet: AttributeSet) : AppCompatButton(context,attributeSet) {
    init {
        applyfont()
    }

    private fun applyfont() {

        val typeface : Typeface = Typeface.createFromAsset(context.assets,"AltonaSans-Regular.tff")
        setTypeface(typeface)
        //      tv_app_name.typeface = typeface

    }

}