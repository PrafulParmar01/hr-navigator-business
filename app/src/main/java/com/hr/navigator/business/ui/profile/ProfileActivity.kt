package com.hr.navigator.business.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.hr.navigator.business.base.BaseActivity
import com.hr.navigator.business.base.extentions.getAddress
import com.hr.navigator.business.base.extentions.hideKeyboard
import com.hr.navigator.business.base.extentions.startActivityWithFadeInAnimation
import com.hr.navigator.business.base.extentions.toastShort
import com.hr.navigator.business.databinding.ActivityProfileBinding
import com.hr.navigator.business.ui.home.DashboardActivity
import com.hr.navigator.business.ui.location.LocationActivity
import com.hr.navigator.business.utils.PrefUtil
import com.hr.navigator.business.utils.UtilsMethod
import java.util.Locale


class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var strCompanyName = ""
    private var strEmail = ""
    private var strPhone = ""
    private var strAddress = ""
    private var strLocation = ""
    private var strAddressLine = ""

    private var formattedLatitude = ""
    private var formattedLongitude = ""

    private lateinit var database: DatabaseReference

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }


    private fun initViews() {
        strPhone = PrefUtil.getStringPref(PrefUtil.PREF_PHONE_NUMBER, applicationContext)
        binding.edtPhone.text = strPhone

        binding.lToolbar.txtTitle.text = "Company Information"
        binding.lToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.btnSave.setOnClickListener {
            if (!isCheckValidation()) {
                hideKeyboard()
                insertProfileDetails()
            }
        }
        binding.txtLocation.setOnClickListener {
            intentLauncher.launch(Intent(this, LocationActivity::class.java))
        }
    }


    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                formattedLatitude = result.data?.getStringExtra("latitude") as String
                formattedLongitude = result.data?.getStringExtra("longitude") as String
                Geocoder(applicationContext, Locale("in"))
                    .getAddress(formattedLatitude.toDouble(), formattedLongitude.toDouble()) { address: Address? ->
                        if (address != null) {
                            val shortAddress = UtilsMethod.generateShortAddress(address)
                            strAddressLine = UtilsMethod.generateAddress(address)
                            binding.txtLocation.text = shortAddress
                            binding.txtAddress.text = "Address: "+strAddressLine
                        }
                    }
            }
        }



    private fun isCheckValidation(): Boolean {
        var isCheck = false
        strCompanyName = binding.edtCompanyName.text.toString()
        strEmail = binding.edtEmail.text.toString()
        strPhone = binding.edtPhone.text.toString()
        strAddress = binding.edtAddress.text.toString()
        strLocation = binding.txtLocation.text.toString()

        if (strCompanyName.isEmpty()) {
            toastShort("Please enter company name")
            isCheck = true
        } else if (strEmail.isEmpty()) {
            toastShort("Please email address")
            isCheck = true
        } else if (strPhone.isEmpty()) {
            toastShort("Please enter phone number")
            isCheck = true
        } else if (strAddress.isEmpty()) {
            toastShort("Please enter company address")
            isCheck = true
        } else if (formattedLatitude.isEmpty() && formattedLongitude.isEmpty()) {
            toastShort("Please select location")
            isCheck = true
        }
        return isCheck
    }

    private fun insertProfileDetails() {
        progressDialogs.showProgressDialog()
        val companyModel = CompanyModel(
            companyName = strCompanyName,
            email = strEmail,
            phone = strPhone,
            address = strAddress,
            latitude = formattedLatitude,
            longitude = formattedLongitude,
            addressLine = strAddressLine,
            companyId = UtilsMethod.generateCompanyId(),
            type = "business"
        )

        val instance = FirebaseDatabase.getInstance()
        database = instance.reference
        database.child("CompanyList").child(strPhone).setValue(companyModel)
            .addOnSuccessListener {
                progressDialogs.dismissDialog()
                toastShort("Profile details added successfully")
                PrefUtil.putStringPref(PrefUtil.PREF_BUSINESS_MODEL, Gson().toJson(companyModel),applicationContext)
                PrefUtil.putBooleanPref(PrefUtil.PREF_IS_PROFILE_FILLED, true, applicationContext)
                val intent = DashboardActivity.getIntent(this)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivityWithFadeInAnimation(intent)
                finish()
            }.addOnFailureListener {
                progressDialogs.dismissDialog()
                Log.e("error : ","===> "+it.message.toString())
                toastShort("Something went wrong. Please try again later.")
            }
    }
}