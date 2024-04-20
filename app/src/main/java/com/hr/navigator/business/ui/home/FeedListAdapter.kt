package com.hr.navigator.business.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.navigator.business.R

class FeedListAdapter(val mContext: Context) : RecyclerView.Adapter<FeedListAdapter.ViewHolder>() {

    private var mList: List<GeofenceEntryModel> = arrayListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: AppCompatTextView = itemView.findViewById(R.id.txtName)
        val txtDesignation: AppCompatTextView = itemView.findViewById(R.id.txtDesignation)
        val txtEmail: AppCompatTextView = itemView.findViewById(R.id.txtEmail)
        val tvPhone: AppCompatTextView = itemView.findViewById(R.id.txtPhone)
        val txtEntryTime: AppCompatTextView = itemView.findViewById(R.id.txtEntryTime)
        val txtExitTime: AppCompatTextView = itemView.findViewById(R.id.txtExitTime)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = mList[position]
        holder.txtName.text = info.firstName + " " + info.lastName
        holder.txtDesignation.text = "Designation: " + info.designation
        holder.txtEmail.text = "E-mail: " + info.email
        holder.tvPhone.text = "Phone: " + info.phone
        if (info.entryTime.isNotEmpty()){
            holder.txtEntryTime.visibility = View.VISIBLE
            holder.txtEntryTime.text = "Entry Time: " + info.entryTime
        }
        else{
            holder.txtEntryTime.visibility = View.GONE
        }

        if (info.exitTime.isNotEmpty()){
            holder.txtExitTime.visibility = View.VISIBLE
            holder.txtExitTime.text = "Exit Time: " + info.exitTime
        }
        else{
            holder.txtExitTime.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_geonece_entry_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun onUpdateList(diaryList: List<GeofenceEntryModel>) {
        mList = diaryList
        notifyDataSetChanged()
    }
}