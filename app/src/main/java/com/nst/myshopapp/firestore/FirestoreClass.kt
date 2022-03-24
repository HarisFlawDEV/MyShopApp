package com.nst.myshopapp.firestore
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nst.myshopapp.activities.LoginActivity
import com.nst.myshopapp.activities.RegisterActivity
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


}