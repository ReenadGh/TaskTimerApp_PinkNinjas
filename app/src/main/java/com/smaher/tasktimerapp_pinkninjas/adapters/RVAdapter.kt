package com.smaher.tasktimerapp_pinkninjas.adapters

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.smaher.tasktimerapp_pinkninjas.Constants
import com.smaher.tasktimerapp_pinkninjas.HomeFragment
import com.smaher.tasktimerapp_pinkninjas.MainActivity
import com.smaher.tasktimerapp_pinkninjas.R
import com.smaher.tasktimerapp_pinkninjas.database.Task
import com.smaher.tasktimerapp_pinkninjas.databinding.ItemRowBinding


class RVAdapter(private val mainActivity: MainActivity, private val homeFragment: HomeFragment): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)
    var currentTask = Task(0,"empty","",null,"",30)
    var addTask = Task(0,"Add Task","hello",null,"add",30)
    var isActive=false
    var tasks = arrayListOf<Task>()
    var currentPos = -1
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

        if(itemCount==1){
            homeFragment.binding.tvTimeHeader.text= homeFragment.timeFormat(0)
            homeFragment.binding.tvTaskHeader.text= tasks[position].name
        }

        holder.binding.apply {
            tvTask.text = name
            when (status) { //status = new , active , paused , completed
                "add" -> {
                    holder.binding.apply {
                        tvTask.setTextColor(mainActivity.resources.getColor(R.color.white))
                        taskImageView.setImageDrawable(getDrawable(mainActivity, R.drawable.ic_baseline_add_task_24))
                        cardView.background= (getDrawable(mainActivity, R.drawable.item_row_add))
                    }}
                "completed" -> {
                    holder.binding.apply {
                        taskImageView.setImageDrawable(getDrawable(mainActivity, R.drawable.ic_baseline_check_circle_24))
                        cardView.background = (getDrawable(mainActivity, R.drawable.completed_task_bg))

                    } }
                "new" -> {
                    holder.binding.apply {
                        taskImageView.setImageDrawable(getDrawable(mainActivity, R.drawable.ic_baseline_play_purple))
                        tvTask.setTextColor(mainActivity.resources.getColor(R.color.purple_200))
                        cardView.background = (getDrawable(mainActivity, R.drawable.item_row_style))
                    }
                }
                "selected" -> {
                    holder.binding.apply {
                        taskImageView.setImageDrawable(getDrawable(mainActivity, R.drawable.ic_baseline_play_circle_outline_24))
                        tvTask.setTextColor(mainActivity.resources.getColor(R.color.white))
                        cardView.background = (getDrawable(mainActivity, R.drawable.item_row_selected))
                        //last selected show it up
                        homeFragment.binding.tvTimeHeader.text= homeFragment.timeFormat(tasks[position].currentTime)
                        homeFragment.binding.tvTaskHeader.text= tasks[position].name
                        homeFragment.binding.tvTaskCard.text=tasks[position].name
                        homeFragment.binding.tvRemainingTimeCard.text="Remaining time: "+ homeFragment.timeFormat(tasks[position].currentTime)
                        homeFragment.binding.tvTotalTimeCard.text=tasks[position].description+"\nDuration: "+tasks[position].totalTime/60000 +" min"
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
            if(!isActive) {
                when (status) { //status = new  , completed
                    "add" -> {
                        Navigation.findNavController(homeFragment.requireView())
                            .navigate(R.id.action_homeFragment_to_addTaskFragment)
                    }
                    "completed" -> {
                        homeFragment.binding.tvTimeHeader.text =
                            homeFragment.timeFormat((tasks[position].currentTime).toLong() / 60000)
                        homeFragment.binding.tvTaskHeader.text = tasks[position].name
                        homeFragment.binding.tvTaskCard.text = tasks[position].name
                        homeFragment.binding.tvRemainingTimeCard.text="Remaining time: "+ homeFragment.timeFormat(tasks[position].currentTime)
                        homeFragment.binding.tvTotalTimeCard.text =
                            tasks[position].description + "\nDuration: " + tasks[position].totalTime.toString() + " min  "

                    }
                    "new" -> {
                        currentPos = position
                        setSelected(position, holder)
                    }
                }
            }
        }

        homeFragment.binding.editImageView.setOnClickListener{
            if(itemCount >1 && currentTask.name!="empty") {
                val bundle = Bundle()
                bundle.putSerializable("passed_task", currentTask)
                homeFragment.findNavController().navigate(R.id.action_homeFragment_to_editFragment, bundle)
            }else
                Toast.makeText(homeFragment.context,"You need to add a task", Toast.LENGTH_SHORT).show()
        }

        //move to next Task Button
        homeFragment.binding.nextImageView.setOnClickListener{
            if(checkSelected()){
                if(itemCount>1 && tasks[position].status!="completed"){
                    when (currentPos+1) {

                        itemCount -> {
                            currentPos = 1
                            setSelected(currentPos,holder)
                        }
                        else -> {
                            currentPos+=1
                            setSelected(currentPos,holder)
                        }
                    }
                }else{
                    Toast.makeText(homeFragment.context,"You need to add more tasks", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(homeFragment.context,"Select a task to start", Toast.LENGTH_SHORT).show()
            }

        }

        //move to previous Task Button
        homeFragment.binding.previousImageView.setOnClickListener{

            if(checkSelected()){
                if(itemCount>1){
                    when (currentPos-1) {

                        0 -> {
                            currentPos = itemCount-1
                            setSelected(currentPos,holder)
                        }
                        else -> {
                            currentPos-=1
                            setSelected(currentPos,holder)
                        }
                    }
                }else{
                    Toast.makeText(homeFragment.context,"You need to add more tasks", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(homeFragment.context,"Select a task to start", Toast.LENGTH_SHORT).show()
            }

        }

        holder.itemView.setOnLongClickListener {
            var myInfoDialog = Dialog(mainActivity)
            myInfoDialog.setContentView(R.layout.choices_layout)
            myInfoDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myInfoDialog.show()

            myInfoDialog.findViewById<LinearLayout>(R.id.startTaskLayout).setOnClickListener{
                homeFragment.binding.tvTimeHeader.text= homeFragment.timeFormat(tasks[position].currentTime)
                homeFragment.binding.tvTaskHeader.text= tasks[position].name
                homeFragment.binding.tvTaskCard.text=tasks[position].name
                homeFragment.binding.tvRemainingTimeCard.text="Remaining time: "+ homeFragment.timeFormat(tasks[position].currentTime)
                homeFragment.binding.tvTotalTimeCard.text=tasks[position].description+"\nDuration: "+tasks[position].totalTime/60000 +" min"
                holder.binding.apply {cardView.background= (getDrawable(mainActivity, R.drawable.item_row_selected)) }

            }

            myInfoDialog.findViewById<LinearLayout>(R.id.editTaskLayout).setOnClickListener{
                val bundle = Bundle()
                bundle.putSerializable("passed_task", tasks[position])
                Navigation.findNavController(homeFragment.requireView()).navigate(R.id.action_homeFragment_to_editFragment,bundle)
                myInfoDialog.dismiss()
            }

            myInfoDialog.findViewById<TextView>(R.id.closeDialogButton).setOnClickListener{
                myInfoDialog.dismiss()
            }
            true
        }
    }

    fun checkSelected():Boolean{
        var flag = false //check if there is any selected item
        tasks.forEach{
            if (it.status=="selected"){
                it.status="new"
                flag=true
            }
            homeFragment.myViewModel.updateTask(it)
        }
        return flag
    }
    fun isSelected():Boolean{
        var flag = false //check if there is any selected item
        tasks.forEach{
                flag=true
        }
        return flag
    }
    private fun setSelected(position: Int,holder: ItemViewHolder) {
        if (position != 0){
            homeFragment.myViewModel.updateTask(tasks[position])
            currentTask = tasks[position]
            homeFragment.myDBSeconds = tasks[position].currentTime
            homeFragment.binding.tvTimeHeader.text= homeFragment.timeFormat((tasks[position].currentTime))
            homeFragment.binding.tvTaskHeader.text= tasks[position].name
            homeFragment.binding.tvTaskCard.text=tasks[position].name
            homeFragment.binding.tvTotalTimeCard.text=tasks[position].description+"\nDuration: "+tasks[position].totalTime/60000 +" min"
            homeFragment.binding.tvRemainingTimeCard.text="Remaining time: "+ homeFragment.timeFormat(tasks[position].currentTime)

            when(tasks[position].image){
                Constants.IMAGES_PLANT[0]-> { homeFragment.binding.characterImageView.setAnimation(Constants.IMAGES_PLANT[0]) }
                Constants.IMAGES_PLANT[1] -> { homeFragment.binding.characterImageView.setAnimation(Constants.IMAGES_PLANT[1]) }
                Constants.IMAGES_PLANT[2] -> { homeFragment.binding.characterImageView.setAnimation(Constants.IMAGES_PLANT[2]) }
            }
            homeFragment.binding.characterImageView.playAnimation()
            homeFragment.binding.characterImageView.loop(true)

            checkSelected()

            holder.binding.apply {
                tasks[position].status = "selected"
                homeFragment.toggle=true
                cardView.background = (getDrawable(mainActivity, R.drawable.item_row_selected))
                notifyDataSetChanged()
            }

        }


    }

    override fun getItemCount() = tasks.size


    fun update(taskList: List<Task>) {
        tasks.clear()
        tasks.add(addTask)
        tasks.addAll(taskList)
        if (itemCount<2){
            homeFragment.binding.emptyCardLayout.visibility=View.VISIBLE
        }else{
            homeFragment.binding.emptyCardLayout.visibility=View.GONE
        }
        notifyDataSetChanged()
    }

}