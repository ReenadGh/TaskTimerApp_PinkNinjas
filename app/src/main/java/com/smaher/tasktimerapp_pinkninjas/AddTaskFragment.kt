package com.smaher.tasktimerapp_pinkninjas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.smaher.tasktimerapp_pinkninjas.adapters.RVAdapter
import com.smaher.tasktimerapp_pinkninjas.database.Task
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentAddTaskBinding
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentHomeBinding


class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val myViewModel by lazy{ ViewModelProvider(this).get(TaskViewModel::class.java) }
    lateinit var title:EditText
    lateinit var description:EditText
    lateinit var time:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        var view: View

        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        view = binding.root

        binding.btAdd.setOnClickListener{
            title= binding.taskTitleET
            description= binding.taskDescriptionET
            time= binding.totalTimeET
            //if not empty
            if(title.text.isNotBlank()
                &&description.text.isNotBlank()
                &&time.text.isNotBlank()){
                myViewModel.addTask(Task(0,title.text.toString(),description.text.toString(),null,"new",time.text.toString().toInt()))
                Toast.makeText(this.context,"Task Added successfully",Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(this.context,"Do not leave them empty!",Toast.LENGTH_SHORT).show()
        }
        return view
    }



}