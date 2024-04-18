package com.hr.navigator.business.ui.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hr.navigator.business.base.BaseActivity
import com.hr.navigator.business.base.extentions.toastShort
import com.hr.navigator.business.databinding.ActivityAdminBinding
import com.hr.navigator.business.ui.admin.view.CustomerRecyclerviewAdapter
import com.hr.navigator.business.ui.profile.CompanyModel
import java.util.Calendar

class AdminActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityAdminBinding
    val list = arrayListOf<CompanyModel>()

    private lateinit var customerAdapter: CustomerRecyclerviewAdapter
    private var searchDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        customerAdapter = CustomerRecyclerviewAdapter()
        binding.recycleViewCustomers.apply {
            layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
            adapter = customerAdapter
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.txtBookingDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, this, year, month, day)
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        onSyncReadData()
    }

    private fun onSyncReadData() {
        progressDialogs.showProgressDialog()
        val firebase = FirebaseDatabase.getInstance()
        val reference = firebase.reference
        reference.child("CustomerRequest")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (x in snapshot.children) {
                        val date = x.child("date").value.toString()
                        val fullName = x.child("fullName").value.toString()
                        val phoneNo = x.child("phoneNo").value.toString()
                        val plan = x.child("plan").value.toString()
                        val address = x.child("address").value.toString()

                        val customerRequestModel =
                            CompanyModel(date, fullName, phoneNo, plan, address)
                        list.add(customerRequestModel)
                        customerAdapter.onUpdateDiaryList(list)
                        progressDialogs.dismissDialog()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    toastShort("Something went wrong. Please try again later.")
                    progressDialogs.dismissDialog()
                }
            })
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var day = "" + dayOfMonth
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth
        }

        val mMonth = month + 1
        var month = "" + mMonth
        if (mMonth < 10) {
            month = "0" + month
        }

        searchDate = "$day/$month/$year"
        binding.txtBookingDate.text = searchDate
        customerAdapter.getFilter(searchDate)
    }
}