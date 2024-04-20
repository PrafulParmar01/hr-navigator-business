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
import com.hr.navigator.business.databinding.FragmentFeedsBinding


class FeedsFragment : BaseFragment() {

    private lateinit var binding: FragmentFeedsBinding

    private lateinit var feedListAdapter: FeedListAdapter
    private var listOfData: MutableList<GeofenceEntryModel> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        binding.recycleViewFeeds.layoutManager = LinearLayoutManager(requireContext())
        feedListAdapter = FeedListAdapter(requireContext())
        binding.recycleViewFeeds.adapter = feedListAdapter
        onSyncReadData()
    }


    private fun onSyncReadData() {
        progressDialogs.showProgressDialog()
        val firebase = FirebaseDatabase.getInstance()
        val reference = firebase.reference
        reference.child("GeofenceEntry")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfData.clear()
                    for (childSnapshot in snapshot.children) {
                        val receivedData: GeofenceEntryModel? = childSnapshot.getValue(GeofenceEntryModel::class.java)
                        if (receivedData != null) {
                            listOfData.add(receivedData)
                        }
                    }
                    feedListAdapter.onUpdateList(listOfData)
                    progressDialogs.dismissDialog()
                }

                override fun onCancelled(error: DatabaseError) {
                    requireContext().toastShort("Something went wrong. Please try again later.")
                    progressDialogs.dismissDialog()
                }
            })
    }
}