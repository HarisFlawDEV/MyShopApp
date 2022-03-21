package com.nst.myshopapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nst.myshopapp.R

open class BaseActivity : AppCompatActivity() {

    fun showErrorSnackBar(message : String , errorMessage: Boolean){
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message , Snackbar.LENGTH_LONG)

        val snackbarView = snackbar.view

        if (errorMessage)
        {
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity, R.color.colorSnackBarSuccess
                )
            )
        }
        snackbar.show()
    }
}