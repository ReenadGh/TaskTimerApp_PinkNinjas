package com.smaher.tasktimerapp_pinkninjas.adapters

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.smaher.tasktimerapp_pinkninjas.HomeFragment
import com.smaher.tasktimerapp_pinkninjas.MainActivity
import com.smaher.tasktimerapp_pinkninjas.R
import com.smaher.tasktimerapp_pinkninjas.database.Task
import com.smaher.tasktimerapp_pinkninjas.databinding.ItemRowBinding


class RVAdapter(private val mainActivity: MainActivity, private val homeFragment: HomeFragment): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    var addTask = Task(0,"Add Task","hello",null,"add",30)
    var tasks = arrayListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
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
            when (status) { //status = new , active , paused , completed
                "add" -> {
                    holder.binding.apply {
                        taskImageView.setImageDrawable(getDrawable(mainActivity, R.drawable.ic_baseline_add_task_24))
                        cardView.background= (getDrawable(mainActivity, R.drawable.item_row_add))
                    }}
                "completed" -> {
                    holder.binding.apply {
                        taskImageView.setImageDrawable(
                            getDrawable(
                                mainActivity,
                                R.drawable.ic_baseline_check_circle_24
                            )
                        )
                        cardView.background = (getDrawable(mainActivity, R.drawable.completed_task_bg))

                    } }
                "new" -> {
                    holder.binding.apply {
                        taskImageView.setImageDrawable(getDrawable(mainActivity, R.drawable.ic_baseline_play_circle_outline_24))
                        tvTask.setTextColor(mainActivity.resources.getColor(R.color.purple_200))
                        cardView.background = (getDrawable(mainActivity, R.drawable.item_row_style))
                    }
                }
                else->{
                    holder.binding.apply {
                        taskImageView.setImageDrawable(getDrawable(mainActivity, R.drawable.item_row_empty))
                        cardView.background = (getDrawable(mainActivity, R.drawable.item_row_empty))
                    }
                }
            }
        }

        holder.itemView.setOnClickListener {
            when (status) { //status = new , active , paused , completed
                "add" -> {
                    Navigation.findNavController(homeFragment.requireView()).navigate(R.id.action_homeFragment_to_addTaskFragment)
                }
                "completed" -> {

                    //val bundle = Bundle()
                    //bundle.putSerializable("passed_task",tasks[position])
                    //homeFragment.findNavController().navigate(R.id.action_homeFragment_to_settingsFragment,bundle)
                    homeFragment.binding.tvTimeHeader.text= tasks[position].currentTime.toString()
                }
 
                "new" ->  {
                   // val bundle = Bundle()
                   // bundle.putSerializable("passed_task",tasks[position])
                  // homeFragment.findNavController().navigate(R.id.action_homeFragment_to_settingsFragment,bundle)
                    homeFragment.binding.tvTimeHeader.text= tasks[position].currentTime.toString()
                    homeFragment.binding.tvTaskHeader.text= tasks[position].name
                    homeFragment.binding.tvTaskCard.text=tasks[position].name
                    homeFragment.binding.tvTotalTimeCard.text=tasks[position].description+"\n Duration: "+tasks[position].totalTime.toString()

                }

            }
        }

        holder.itemView.setOnLongClickListener {
            var myInfoDialog = Dialog(mainActivity)
            myInfoDialog.setContentView(R.layout.choices_layout)
            myInfoDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myInfoDialog.show()

            myInfoDialog.findViewById<LinearLayout>(R.id.startTaskLayout).setOnClickListener{

            }

            myInfoDialog.findViewById<LinearLayout>(R.id.editTaskLayout).setOnClickListener{
                Navigation.findNavController(homeFragment.requireView()).navigate(R.id.action_homeFragment_to_addTaskFragment)
            }

            myInfoDialog.findViewById<TextView>(R.id.closeDialogButton).setOnClickListener{
                myInfoDialog.dismiss()
            }
            true
        }
    }

    override fun getItemCount() = tasks.size


    fun update(taskList: List<Task>) {
        tasks.clear()


        tasks.add(addTask)
        tasks.add( Task(0,"Add Task","hello",null,"new",30))
        tasks.add( Task(0,"Add Task","hello",null,"new",30))
        tasks.add( Task(0,"Add Task","hello",null,"new",30))
        tasks.add( Task(0,"Add Task","hello",null,"new",30))
        tasks.add( Task(0,"","",null,"empty",0))
        tasks.add( Task(0,"","",null,"empty",0))
        tasks.add( Task(0,"","",null,"empty",0))
        tasks.addAll(taskList)
        notifyDataSetChanged()
    }
}