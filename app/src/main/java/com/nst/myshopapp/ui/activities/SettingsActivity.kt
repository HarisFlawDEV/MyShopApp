package com.nst.myshopapp.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
//import android.widget.Toolbar
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.nst.myshopapp.R
import com.nst.myshopapp.databinding.ActivityRegisterBinding
import com.nst.myshopapp.databinding.ActivitySettingsBinding
import com.nst.myshopapp.firestore.FirestoreClass
import com.nst.myshopapp.model.User
import com.nst.myshopapp.utils.Constants
import com.nst.myshopapp.utils.GlideLoader

class SettingsActivity : BaseActivity() ,View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mUserDetails : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar(view)
        binding.tvEdit.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)

    }


    private fun setupActionBar(v : View) {
        setSupportActionBar(binding.toolbarSettingsActivity)
        val actionBar = supportActionBar
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_ios_24)

        }
        binding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }

    }

    private fun getUserDetails(){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getUserDetails(this@SettingsActivity)

        }
    fun userDetailsSuccess(user : User){

        mUserDetails = user
        hideProgressDialog()

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image,binding.ivUserPhoto)
        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvPhone.text = "${user.mobile}"

    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(p0: View?) {
        if ( p0 != null)
        {
            when(p0.id){
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity,LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity,UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                    startActivity(intent)

                }
            }

        }

    }

}