package com.nst.myshopapp.activities

import android.app.Dialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nst.myshopapp.R
import com.nst.myshopapp.databinding.ActivityBaseBinding
import com.nst.myshopapp.databinding.ActivityRegisterBinding
import com.nst.myshopapp.databinding.DialogProgressBinding

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    private lateinit var binding: DialogProgressBinding


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

        binding = DialogProgressBinding.inflate(layoutInflater)
        val view = binding.root

        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(view)

        binding.tvProgressText.text = text

        mProgressDialog.setCancelable(false)

        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()

    }
    fun hideProgressDialog()
    {
        mProgressDialog.dismiss()
    }
}