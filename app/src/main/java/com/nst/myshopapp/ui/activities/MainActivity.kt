package com.nst.myshopapp.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nst.myshopapp.R
import com.nst.myshopapp.databinding.ActivityMainBinding
import com.nst.myshopapp.databinding.ActivityRegisterBinding
import com.nst.myshopapp.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val sharedPreferences = getSharedPreferences(Constants.MYSHOPAPP_PREF,Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")

        binding.tvConst.text = username





    }
}