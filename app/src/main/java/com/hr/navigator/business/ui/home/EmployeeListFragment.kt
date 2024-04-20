package com.hr.navigator.business.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hr.navigator.business.base.BaseFragment
import com.hr.navigator.business.base.extentions.toastShort
import com.hr.navigator.business.databinding.FragmentEmployeeBinding
import com.hr.navigator.business.databinding.FragmentFeedsBinding


class EmployeeListFragment : BaseFragment() {

    private lateinit var binding: FragmentEmployeeBinding

    private lateinit var employeeListAdapter: EmployeeListAdapter
    private var listOfData: MutableList<UserModel> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        binding.recycleViewList.layoutManager = LinearLayoutManager(requireContext())
        employeeListAdapter = EmployeeListAdapter(requireContext())
        binding.recycleViewList.adapter = employeeListAdapter
        onSyncReadData()
    }


    private fun onSyncReadData() {
        progressDialogs.showProgressDialog()
        val firebase = FirebaseDatabase.getInstance()
        val reference = firebase.reference
        reference.child("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfData.clear()
                    for (childSnapshot in snapshot.children) {
                        val receivedData: UserModel? = childSnapshot.getValue(UserModel::class.java)
                        if (receivedData != null) {
                            listOfData.add(receivedData)
                        }
                    }
                    employeeListAdapter.onUpdateList(listOfData)
                    progressDialogs.dismissDialog()
                }

                override fun onCancelled(error: DatabaseError) {
                    requireContext().toastShort("Something went wrong. Please try again later.")
                    progressDialogs.dismissDialog()
                }
            })
    }
}