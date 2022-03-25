package com.nst.myshopapp.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.GetChars
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.nst.myshopapp.R
import com.nst.myshopapp.databinding.ActivityLoginBinding
import com.nst.myshopapp.databinding.ActivityRegisterBinding
import com.nst.myshopapp.firestore.FirestoreClass
import com.nst.myshopapp.model.User
import com.nst.myshopapp.utils.Constants

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        //OnclickListener
        binding.tvForgotPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvToregister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        if(v!= null){
            when(v.id){
                R.id.tv_forgot_password -> {
                    startActivity(Intent(this@LoginActivity, ForgotActivity::class.java))

                }
                R.id.btn_login -> {
                    loginRegisteredUser()

                }
                R.id.tv_toregister ->{
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

                }
            }
        }

    }

    private fun validateLoginDetails() : Boolean {
        return when {

            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' '}) -> {
                Log.d("email",binding.etEmail.text.toString().trim())
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            else -> {
                 // showErrorSnackBar(resources.getString(R.string.login_successfull),false)
                true
            }
        }
    }

    private fun loginRegisteredUser() {
        if (validateLoginDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            //Get the text from editText and trim the space
            val email =binding.etEmail.text.toString().trim { it <= ' '}
            val password =binding.etPassword.text.toString().trim { it <= ' ' }

            //Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    //Hide the progess dialog
                  //  hideProgressDialog()

                    if(task.isSuccessful) {

                        //TODO - SEND user to Main Activity

                        FirestoreClass().getUserDetails(this@LoginActivity)
                      //  showErrorSnackBar("You are logged in successfully.",false)

                    }
                    else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }

                }



        }
    }

    fun userLoggedInSuccess(user: User) {

        hideProgressDialog()

        //get details here

        if(user.profileCompleted == 0)
        {
            val intent = Intent(this@LoginActivity , UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }else {
            startActivity(Intent(this@LoginActivity , DashBoardActivity::class.java))
        }
        finish()
    }


}



