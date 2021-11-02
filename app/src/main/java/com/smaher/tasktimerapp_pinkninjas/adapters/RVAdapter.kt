package com.smaher.tasktimerapp_pinkninjas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smaher.tasktimerapp_pinkninjas.MainActivity
import com.smaher.tasktimerapp_pinkninjas.data.Task
import com.smaher.tasktimerapp_pinkninjas.databinding.ItemRowBinding


class RVAdapter(val mainActivity: MainActivity): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)


    var tasks = emptyList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val name = tasks[position].name
        val description = tasks[position].description
        val totalTime = tasks[position].totalTime

        holder.binding.apply {
            tvTaskName.text = name
        }

    }

    override fun getItemCount() = tasks.size


    fun update(notesList: List<Task>) {
        tasks = notesList
        notifyDataSetChanged()
    }
}