package com.nst.myshopapp.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.nst.myshopapp.R
import com.nst.myshopapp.databinding.ActivityMainBinding
import com.nst.myshopapp.databinding.ActivityRegisterBinding
import com.nst.myshopapp.firestore.FirestoreClass
import com.nst.myshopapp.model.User

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
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

        binding.tvTologin.setOnClickListener {
           onBackPressed()

        }
        binding.btnRegister.setOnClickListener {
           registerUser()
        }


    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarRegisterActivity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_ios_24)
        }
        binding.toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }

    }

    private fun validateRegisterDetails() : Boolean {
        return when {

            TextUtils.isEmpty(binding.etFname.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name),true)
                false
            }

            TextUtils.isEmpty(binding.etLname.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name),true)
                false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' '}) -> {
                Log.d("email",binding.etEmail.text.toString().trim())
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }

            TextUtils.isEmpty(binding.etCpassword.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password),true)
                false
            }

            binding.etPassword.text.toString().trim {it <= ' '} != binding.etCpassword.text.toString().trim { it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),true)
                false
            }
            !binding.cbTermsAndCondition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition),true)
                false
            }
            else -> {
              //  showErrorSnackBar(resources.getString(R.string.registery_successfull),false)
                true
            }
        }
    }

    private fun registerUser() {
        if (validateRegisterDetails())
        {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email : String = binding.etEmail.text.toString().trim()  { it <= ' '}
            val password : String = binding.etPassword.text.toString().trim() { it <= ' '}

            //Create an instance and create a register a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->


                        if (task.isSuccessful) {
                            Log.d("register","Success")
                            val firebaseUser : FirebaseUser = task.result!!.user!!

                            val user = User(firebaseUser.uid,
                                firstName = binding.etFname.text.toString().trim { it <= ' ' },
                                lastName =  binding.etLname.text.toString().trim { it <= ' ' },
                                email = binding.etFname.text.toString().trim { it <= ' ' }
                            )
                            FirestoreClass().registerUser(this@RegisterActivity,user)

                     /*   showErrorSnackBar(
                            "You are registered successfully, Your user id is " +
                                    firebaseUser.uid,false
                        )*/
                           // FirebaseAuth.getInstance().signOut()
                       //     finish()
                        }
                        else {
                            Log.d("register", "Failed$email")

                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }

                    }
                )
        }
    }
    fun userRegisteredSuccess(){
        //Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(this@RegisterActivity,
        resources.getString(R.string.registery_succes),Toast.LENGTH_SHORT).show()

    }
}