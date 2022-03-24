package com.nst.myshopapp.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.nst.myshopapp.R
import com.nst.myshopapp.databinding.ActivityForgotBinding
import com.nst.myshopapp.databinding.ActivityRegisterBinding

class ForgotActivity : BaseActivity() {

    private lateinit var binding: ActivityForgotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()



    }
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarForgotpasswordActivity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_ios_24)
        }
        binding.toolbarForgotpasswordActivity.setNavigationOnClickListener { onBackPressed() }

        binding.btnSubmit.setOnClickListener {
            val email : String = binding.etEmail.text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
            }
            else{
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideProgressDialog()

                        if (task.isSuccessful)  {
                            Toast.makeText(this@ForgotActivity, resources.getString(R.string.email_sent_success),Toast.LENGTH_LONG).show()
                        finish()
                        }
                        else {
                            showErrorSnackBar(task.exception!!.message.toString(),true)

                        }
                    }
            }
        }


    }

}