package com.hr.navigator.business.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hr.navigator.business.R
import com.hr.navigator.business.base.BaseActivity
import com.hr.navigator.business.base.extentions.startActivityWithFadeInAnimation
import com.hr.navigator.business.databinding.ActivityHomeBinding
import com.hr.navigator.business.ui.accounts.AccountsActivity


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
    }

    private fun initViews() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> {
                    onDefaultFragment()
                    true
                }
                R.id.menuList -> {
                    val employeeListFragment = EmployeeListFragment()
                    replaceFragment(employeeListFragment, R.id.frameContainer)
                    true
                }
                else -> false
            }
        }
        onDefaultFragment()

        binding.btnAccount.setOnClickListener {
            startActivityWithFadeInAnimation(AccountsActivity.getIntent(this))
        }
    }

    private fun onDefaultFragment() {
        val feedsFragment = FeedsFragment()
        replaceFragment(feedsFragment, R.id.frameContainer)
    }
}