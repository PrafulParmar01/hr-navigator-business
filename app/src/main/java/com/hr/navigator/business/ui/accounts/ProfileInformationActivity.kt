package com.hr.navigator.business.ui.accounts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.hr.navigator.business.base.BaseActivity
import com.hr.navigator.business.base.extentions.getAddress
import com.hr.navigator.business.base.extentions.hideKeyboard
import com.hr.navigator.business.base.extentions.startActivityWithFadeInAnimation
import com.hr.navigator.business.base.extentions.toastShort
import com.hr.navigator.business.databinding.ActivityProfileInformationBinding
import com.hr.navigator.business.ui.home.DashboardActivity
import com.hr.navigator.business.ui.home.UserModel
import com.hr.navigator.business.ui.location.LocationActivity
import com.hr.navigator.business.ui.profile.CompanyModel
import com.hr.navigator.business.utils.PrefUtil
import com.hr.navigator.business.utils.UtilsMethod
import java.util.Locale


class ProfileInformationActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileInformationBinding
    private var strCompanyName = ""
    private var strAddress = ""
    private var strLocation = ""
    private var strAddressLine = ""

    private var formattedLatitude = ""
    private var formattedLongitude = ""

    private lateinit var getCompanyModel: CompanyModel

    private lateinit var database: DatabaseReference

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ProfileInformationActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }


    private fun initViews() {
        getCompanyModel = UtilsMethod.convertStringToCompanyModel(applicationContext)

        binding.layoutToolBar.txtTitle.text = "Profile Information"
        binding.layoutToolBar.btnEdit.visibility = View.VISIBLE
        binding.btnSave.visibility = View.GONE

        binding.layoutToolBar.btnBack.setOnClickListener {
            finish()
        }

        binding.layoutToolBar.btnEdit.setOnClickListener {
            setEditEnabled()
            binding.btnSave.visibility = View.VISIBLE
        }

        binding.btnSave.setOnClickListener {
            if (!isCheckValidation()) {
                hideKeyboard()
                updateProfileDetails()
            }
        }
        binding.txtLocation.setOnClickListener {
            intentLauncher.launch(LocationActivity.getIntent(this))
        }

        setDefaultData()
        setEditDisabled()
    }

    private fun setEditDisabled() {
        binding.edtCompanyName.isEnabled = false
        binding.edtAddress.isEnabled = false
        binding.txtLocation.isEnabled = false
    }

    private fun setEditEnabled() {
        binding.edtCompanyName.isEnabled = true
        binding.edtAddress.isEnabled = true
        binding.txtLocation.isEnabled = true
    }


    private fun setDefaultData() {
        strCompanyName = getCompanyModel.companyName
        strAddress = getCompanyModel.address
        strLocation = getCompanyModel.address
        strAddressLine = getCompanyModel.addressLine

        formattedLatitude = getCompanyModel.latitude
        formattedLongitude = getCompanyModel.longitude
        strAddressLine = getCompanyModel.addressLine

        binding.edtCompanyName.setText(strCompanyName)
        binding.edtAddress.setText(strAddress)

        Geocoder(applicationContext, Locale.getDefault()).getAddress(formattedLatitude.toDouble(), formattedLongitude.toDouble()) { address: Address? ->
            if (address != null) {
                val shortAddress = UtilsMethod.generateShortAddress(address)
                binding.txtLocation.text = shortAddress
                binding.txtAddress.text = "Address: "+strAddressLine
            }
        }
    }



    private val intentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                formattedLatitude = result.data?.getStringExtra("latitude") as String
                formattedLongitude = result.data?.getStringExtra("longitude") as String
                if (formattedLatitude.isNotEmpty() && formattedLongitude.isNotEmpty()){
                    Geocoder(applicationContext, Locale("in")).getAddress(formattedLatitude.toDouble(), formattedLongitude.toDouble()) { address: Address? ->
                        if (address != null) {
                            val shortAddress = UtilsMethod.generateShortAddress(address)
                            strAddressLine = UtilsMethod.generateAddress(address)
                            binding.txtLocation.text = shortAddress
                            binding.txtAddress.text = "Address: "+strAddressLine
                        }
                    }
                }
            }
        }



    private fun isCheckValidation(): Boolean {
        var isCheck = false
        strCompanyName = binding.edtCompanyName.text.toString()
        strAddress = binding.edtAddress.text.toString()
        strLocation = binding.txtLocation.text.toString()

        if (strCompanyName.isEmpty()) {
            toastShort("Please enter company name")
            isCheck = true
        } else if (strAddress.isEmpty()) {
            toastShort("Please enter address")
            isCheck = true
        } else if (strLocation.isEmpty()) {
            toastShort("Please select location")
            isCheck = true
        }
        return isCheck
    }

    private fun updateProfileDetails() {
        progressDialogs.showProgressDialog()
        val companyModel = CompanyModel(
            companyName = strCompanyName,
            email = getCompanyModel.email,
            phone = getCompanyModel.phone,
            address = strAddress,
            latitude = formattedLatitude,
            longitude = formattedLongitude,
            addressLine = strAddressLine,
            companyId = getCompanyModel.companyId,
            type = getCompanyModel.type
        )

        val instance = FirebaseDatabase.getInstance()
        database = instance.reference
        database.child("CompanyList").child(getCompanyModel.phone).setValue(companyModel)
            .addOnSuccessListener {
                progressDialogs.dismissDialog()
                toastShort("Profile updated successfully")
                PrefUtil.putStringPref(PrefUtil.PREF_BUSINESS_MODEL, Gson().toJson(companyModel),applicationContext)
                finish()
            }.addOnFailureListener {
                progressDialogs.dismissDialog()
                Log.e("error : ","===> "+it.message.toString())
                toastShort("Something went wrong. Please try again later.")
            }
    }
}