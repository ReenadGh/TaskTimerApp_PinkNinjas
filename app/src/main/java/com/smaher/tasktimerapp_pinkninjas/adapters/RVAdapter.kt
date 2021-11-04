package com.smaher.tasktimerapp_pinkninjas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.smaher.tasktimerapp_pinkninjas.HomeFragment
import com.smaher.tasktimerapp_pinkninjas.MainActivity
import com.smaher.tasktimerapp_pinkninjas.R
import com.smaher.tasktimerapp_pinkninjas.database.Task
import com.smaher.tasktimerapp_pinkninjas.databinding.ItemRowBinding


class RVAdapter(val mainActivity: MainActivity,val homeFragment: HomeFragment): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    var addTask = Task(0,"Add Task","hello","plant1","add",30)
    var tasks = arrayListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        tasks.add(addTask)
        return ItemViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val name = tasks[position].name
        val description = tasks[position].description
        val status = tasks[position].status
        val totalTime = tasks[position].totalTime

        holder.binding.apply {
           tvTask.text = name
            if (name=="Add Task"){
                taskImageView.setImageDrawable(getDrawable(mainActivity, R.drawable.ic_baseline_add_task_24))
                cardView.background= (getDrawable(mainActivity, R.drawable.item_row_add))
        }}

        holder.itemView.setOnClickListener {
            when (status) { //status = new , active , paused , completed
                "add" -> {
                    holder.binding.apply {
                        taskImageView.setImageDrawable(getDrawable(mainActivity, R.drawable.ic_baseline_add_task_24))
                        cardView.background= (getDrawable(mainActivity, R.drawable.item_row_add))
                        Navigation.findNavController(homeFragment.requireView()).navigate(R.id.action_homeFragment_to_addTaskFragment)
                    }}
                "completed" -> {
                    holder.binding.apply {
                        taskImageView.setImageDrawable(
                            getDrawable(
                                mainActivity,
                                R.drawable.ic_baseline_add_task_24
                            )
                        )
                        cardView.background = (getDrawable(mainActivity, R.drawable.completed_task_bg))
                        Navigation.findNavController(homeFragment.requireView())
                            .navigate(R.id.action_homeFragment_to_addTaskFragment)
                    } }
                else -> {

                }
            }
        }

    }

    override fun getItemCount() = tasks.size


    fun update(notesList: List<Task>) {
        tasks.clear()
        tasks.add(addTask)
        tasks.addAll(notesList)
        notifyDataSetChanged()
    }
}