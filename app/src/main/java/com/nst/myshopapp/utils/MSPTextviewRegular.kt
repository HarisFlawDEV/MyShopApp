package com.nst.myshopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.activity_splash.*

class MSPTextviewRegular(context : Context , attributes: AttributeSet) : AppCompatTextView(context,attributes) {

    init {
        applyfont()
    }

    private fun applyfont() {

        val typeface : Typeface = Typeface.createFromAsset(context.assets,"AltonaSans-Regular.tff")
        setTypeface(typeface)
  //      tv_app_name.typeface = typeface

    }


}