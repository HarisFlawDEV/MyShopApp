package com.nst.myshopapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nst.myshopapp.R
import com.nst.myshopapp.databinding.ActivityRegisterBinding
import com.nst.myshopapp.databinding.ActivityUserProfileBinding
import com.nst.myshopapp.model.User
import com.nst.myshopapp.utils.Constants

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var userDetails : User = User()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //Get the user details from intent as a ParcelableExtra
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        binding.etFname.isEnabled = false
        binding.etFname.setText(userDetails.firstName)

        binding.etLname.isEnabled = false
        binding.etLname.setText(userDetails.lastName)

        binding.etEmail.isEnabled = false
        binding.etEmail.setText(userDetails.email)

    }

    override fun onClick(v: View?) {

    }
}