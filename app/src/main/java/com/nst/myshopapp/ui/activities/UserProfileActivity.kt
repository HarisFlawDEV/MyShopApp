package com.nst.myshopapp.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nst.myshopapp.R
import com.nst.myshopapp.databinding.ActivityRegisterBinding
import com.nst.myshopapp.databinding.ActivityUserProfileBinding
import com.nst.myshopapp.firestore.FirestoreClass
import com.nst.myshopapp.model.User
import com.nst.myshopapp.utils.Constants
import com.nst.myshopapp.utils.GlideLoader
import java.io.IOException
import java.util.jar.Manifest

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserProfileBinding

    private lateinit var muserDetails : User
    private var mSelectedImageUri : Uri? = null
    private  var mUserProfileImageURL : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //Get the user details from intent as a ParcelableExtra
            muserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        binding.etFname.isEnabled = false
        binding.etFname.setText(muserDetails.firstName)

        binding.etLname.isEnabled = false
        binding.etLname.setText(muserDetails.lastName)

        binding.etEmail.isEnabled = false
        binding.etEmail.setText(muserDetails.email)

        binding.ivUserPhoto.setOnClickListener(this@UserProfileActivity)

        binding.btnSave.setOnClickListener(this@UserProfileActivity)



    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE){

            mSelectedImageUri = data?.data
            binding.ivUserPhoto.setImageURI(data?.data) // handle chosen image

        }
    }
    private fun setOnCheckedChangeListener() : String{
        var gender : String = ""
        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
            gender = if (R.id.rb_male == checkedId) Constants.MALE else Constants.FEMALE
//
        }
        return gender
    }


    override fun onClick(v: View?) {
        if(v != null){
            when (v.id){
                R.id.iv_user_photo -> {
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        //showErrorSnackBar("You already have the storage permission.",false)
                        Constants.showImageChooser(this)
                    }
                    else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_save ->{

                    if (validateUserProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageUri != null) {
                            FirestoreClass().uploadImageToCloudStorage(
                                this@UserProfileActivity,
                                mSelectedImageUri
                            )
                        } else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }
    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String,Any>()

        val mobileNumber = binding.etMobileno.text.toString().trim { it <= ' ' }

        val gender = setOnCheckedChangeListener()
        Toast.makeText(this, gender, Toast.LENGTH_SHORT).show()

        if (mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }
        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1

       // showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().updateUserProfileData(this,userHashMap)



//      showErrorSnackBar("Your details are valid. You can update them.",false)

    }
    fun userProfileUpdateSuccess(){
        hideProgressDialog()

        Toast.makeText(this@UserProfileActivity,
        resources.getString(R.string.msg_profile_update_success),
        Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
    }

/*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

             //   showErrorSnackBar("The storage permission is granted.",false)
            Constants.showImageChooser(this)
            }
            else {
                Toast.makeText(
                    this,resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        val SelectedImageUri = data.data!!
                        Log.d("image","yes"+data.data!!)
                        GlideLoader(this).loadUserPicture(SelectedImageUri!!, binding.ivUserPhoto)

                        binding.ivUserPhoto.setImageURI(Uri.parse(SelectedImageUri.toString()))
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
        else
        {

            Log.e("ERRRR", requestCode.toString() + "  " + Activity.RESULT_OK.toString() )

        }
    }
*/

    private fun validateUserProfileDetails() : Boolean {
        return when {
            TextUtils.isEmpty(binding.etMobileno.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number),true)
                false
            }
            else -> true
        }

    }

    fun imageUploadSuccess(imageURL : String) {
     //   hideProgressDialog()
      /*  Toast.makeText(
            this@UserProfileActivity,
            "Your image is uploaded successfully . Image URL is $imageURL",
            Toast.LENGTH_SHORT
        ).show()*/

        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

}