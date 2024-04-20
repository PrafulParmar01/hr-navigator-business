package com.hr.navigator.business.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.navigator.business.R

class EmployeeListAdapter(val mContext: Context) : RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>() {
    private var mList: List<UserModel> = arrayListOf()

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtName: AppCompatTextView = itemView.findViewById(R.id.txtName)
        val txtDesignation: AppCompatTextView = itemView.findViewById(R.id.txtDesignation)
        val txtEmail: AppCompatTextView = itemView.findViewById(R.id.txtEmail)
        val tvPhone: AppCompatTextView = itemView.findViewById(R.id.txtPhone)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = mList[position]
        holder.txtName.text = info.firstName + " " + info.lastName
        holder.txtDesignation.text = "Designation: " + info.designation
        holder.txtEmail.text = "E-mail: " + info.email
        holder.tvPhone.text = "Phone: " + info.phone
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_employee_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun onUpdateList(diaryList: List<UserModel>) {
        mList = diaryList
        notifyDataSetChanged()
    }
}