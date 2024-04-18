package com.hr.navigator.business.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hr.navigator.business.base.BaseActivity
import com.hr.navigator.business.databinding.ActivityHomeBinding


class DashboardActivity : BaseActivity(){

    private lateinit var binding: ActivityHomeBinding

    companion object{
        fun getIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        onClickSync()
    }

    private fun initViews() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        onDefaultFragment()
    }

    private fun onDefaultFragment() {
        //val homeFragment = HomeShipperFragment()
        //replaceFragment(homeFragment)
    }

    private fun onClickSync() {

    }

}