package com.than.chapter5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.than.chapter5.databinding.ListItemBinding
import com.than.chapter5.model.GetAllDataCovidResponse

class MainAdapter(private val onItemClick: OnClickListener): RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<GetAllDataCovidResponse>(){
        override fun areItemsTheSame(
            oldItem: GetAllDataCovidResponse,
            newItem: GetAllDataCovidResponse
        ): Boolean {
            return oldItem.country == newItem.country
        }

        override fun areContentsTheSame(
            oldItem: GetAllDataCovidResponse,
            newItem: GetAllDataCovidResponse
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitData(value: List<GetAllDataCovidResponse>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(private val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetAllDataCovidResponse){
            binding.apply {
                tvCountry.text = data.country
                tvCases.text = data.cases.toString()
                Glide.with(binding.root)
                    .load(data.countryInfo.flag)
                    .centerCrop()
                    .into(ivFlag)
                root.setOnClickListener{
                    onItemClick.onClickItem(data)
                }
            }
        }
    }
    interface OnClickListener{
        fun onClickItem(data: GetAllDataCovidResponse)
    }

}