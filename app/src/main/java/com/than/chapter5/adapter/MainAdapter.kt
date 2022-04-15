package com.than.chapter5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.than.chapter5.databinding.CarItemBinding
import com.than.chapter5.model.Data
import com.than.chapter5.model.DataLyric

class MainAdapter(private val onItemClick: OnClickListener): RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<Data>(){
        override fun areItemsTheSame(
            oldItem: Data,
            newItem: Data
        ): Boolean {
            return oldItem.songId == newItem.songId
        }

        override fun areContentsTheSame(
            oldItem: Data,
            newItem: Data
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitData(value: List<Data>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(CarItemBinding.inflate(inflater, parent, false))
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

    inner class ViewHolder(private val binding: CarItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: Data){
            binding.apply {
                tvJudul.text = data.songId
                tvHarga.text = data.songTitle
                root.setOnClickListener{
                    onItemClick.onClickItem(data)
                }
            }
        }
    }
    interface OnClickListener{
        fun onClickItem(data: Data)
    }

}