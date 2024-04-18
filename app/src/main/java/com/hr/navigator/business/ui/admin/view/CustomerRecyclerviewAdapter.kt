package com.hr.navigator.business.ui.admin.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.navigator.business.R
import com.hr.navigator.business.ui.profile.CompanyModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerRecyclerviewAdapter : RecyclerView.Adapter<CustomerRecyclerviewAdapter.ViewHolder>() {

    private var mList: List<CompanyModel> = arrayListOf()
    private var mListFiltered: List<CompanyModel> = arrayListOf()

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDate: AppCompatTextView = itemView.findViewById(R.id.txtDate)
        val tvName: AppCompatTextView = itemView.findViewById(R.id.txtName)
        val tvPlan: AppCompatTextView = itemView.findViewById(R.id.txtPlan)
        val tvPhone: AppCompatTextView = itemView.findViewById(R.id.txtPhone)
        val btnDelete: AppCompatTextView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = mListFiltered[position]


        holder.btnDelete.setOnClickListener {
            onClickArrow(mListFiltered[holder.adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_customer_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mListFiltered.size
    }

    fun onUpdateDiaryList(diaryList: List<CompanyModel>) {
        mList = diaryList
        mListFiltered = mList
        notifyDataSetChanged()
    }

    var onClickArrow: ((data: CompanyModel) -> Unit) = { }


    fun getFilter(constraint: String) {
        CoroutineScope(Dispatchers.Main).launch {

        }
    }
}