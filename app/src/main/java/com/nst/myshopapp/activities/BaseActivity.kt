package com.nst.myshopapp.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nst.myshopapp.R
import com.nst.myshopapp.databinding.ActivityBaseBinding
import com.nst.myshopapp.databinding.ActivityRegisterBinding

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog



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
    fun showProgressDialog(text : String)
    {
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.findViewById<TextView>(R.id.tv_progress_text
        )
    }
}