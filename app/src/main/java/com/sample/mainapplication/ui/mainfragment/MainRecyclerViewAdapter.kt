package com.sample.mainapplication.ui.mainfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.mainapplication.model.MainData
import com.sample.mainapplication.R

class MainRecyclerViewAdapter(): RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var itemList = emptyList<MainData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.main_recycler_item, parent, false)
        return MainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateList(newItemList: List<MainData>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    inner class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val nameTextView: TextView

        init {
            nameTextView = itemView.findViewById(R.id.name)
        }

        fun bind(mainData: MainData) {
            nameTextView.text = mainData.name

            itemView.setOnClickListener {
                onItemClick?.invoke(mainData.name)
            }
        }
    }
}