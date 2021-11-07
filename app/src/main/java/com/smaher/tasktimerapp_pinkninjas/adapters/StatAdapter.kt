package com.smaher.tasktimerapp_pinkninjas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smaher.tasktimerapp_pinkninjas.MainActivity
import com.smaher.tasktimerapp_pinkninjas.StatisticsFragment
import com.smaher.tasktimerapp_pinkninjas.database.Task
import com.smaher.tasktimerapp_pinkninjas.databinding.StatItemBinding


class StatAdapter(val mainActivity: MainActivity, val statFragment: StatisticsFragment): RecyclerView.Adapter<StatAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: StatItemBinding) : RecyclerView.ViewHolder(binding.root)


    var tasks = arrayListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        return ItemViewHolder(
            StatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val name = tasks[position].name
        val description = tasks[position].description
        val image = tasks[position].image
        val status = tasks[position].status
        val totalTime = tasks[position].totalTime
        val currentTime =  tasks[position].currentTime

        statFragment.binding.tvTotalTimeAll.setText("The total time you have spent growing your beautiful plants \n"+statFragment.timeFormat(totalTimeAllTasks()))
        holder.binding.apply {
            tvTaskName.text = name
            tvDesc.text = description
            plantStatAdapter.setAnimation(image!!)
            tvTime.text= "Remaining time: ${statFragment.timeFormat(currentTime)}"

        }
    }

        override fun getItemCount() = tasks.size


        fun update(taskList: List<Task>) {
            tasks.addAll(taskList)
            notifyDataSetChanged()
        }

    fun totalTimeAllTasks():Long{
        var total=0L
        tasks.forEach{
            total+= it.totalTime - it.currentTime
        }
        return total
    }



}