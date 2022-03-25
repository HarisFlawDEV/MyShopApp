package com.nst.myshopapp.firestore
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nst.myshopapp.ui.activities.LoginActivity
import com.nst.myshopapp.ui.activities.RegisterActivity
import com.nst.myshopapp.ui.activities.UserProfileActivity
import com.nst.myshopapp.model.User
import com.nst.myshopapp.utils.Constants

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener {
                activity.userRegisteredSuccess()

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user",
                    e
                )

            }

    }

    fun getCurrentUserID() : String {
        //An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //A variable to assign the currentUserId id it is not null or else it will be blank
        var currentUserID = ""

        if(currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID

    }

    fun getUserDetails(activity: Activity)
    {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYSHOPAPP_PREF,
                    Context.MODE_PRIVATE
                )
                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                // Key : logged_in_username
                // Value:
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"

                )
                editor.apply()




                //TODO STEP 6 : PASS THE RESULT TO THE LOGIN ACTIVITY
                //START
                when (activity){
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String,Any>){

        mFirestore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        //Hide the progress dialog if there is any error , And print the error in log
                        activity.userProfileUpdateSuccess()
                    }
                }


            }
            .addOnFailureListener { e->
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,"Error while updating the user details",e)

            }

    }

    fun uploadImageToCloudStorage(activity: Activity ,imageFileURI : Uri?){
        val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(activity,imageFileURI)
        )
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
            //The image upload is success
            Log.e(
                "Firebase Image URL",taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            //Get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL",uri.toString())
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }
        }
            .addOnFailureListener { exception ->
                //Hide the progress dialog if there is any error. And print the error in log
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

}