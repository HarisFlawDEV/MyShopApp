package com.nst.myshopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MSPEditText(context: Context , attributeSet: AttributeSet) : AppCompatEditText(context,attributeSet){
    init {
        applyfont()

    }

    private fun applyfont() {

        val typeface : Typeface = Typeface.createFromAsset(context.assets,"fonts/AltonaSans-Regular.ttf")
        setTypeface(typeface)
        //      tv_app_name.typeface = typeface

    }
}