package com.smaher.tasktimerapp_pinkninjas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.smaher.tasktimerapp_pinkninjas.Constants.IMAGES_PLANT
import com.smaher.tasktimerapp_pinkninjas.database.Task
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentAddTaskBinding

import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import kotlin.time.toDuration


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


        //check the type of plant
        var type=IMAGES_PLANT[0]

        binding.plantPicker.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                when(position){
                   0 -> { binding.plantImageAdd.setAnimation(IMAGES_PLANT[0])
                       type=IMAGES_PLANT[0]}
                   1 -> { binding.plantImageAdd.setAnimation(IMAGES_PLANT[1])
                       type=IMAGES_PLANT[1]}
                   2 -> { binding.plantImageAdd.setAnimation(IMAGES_PLANT[2])
                       type=IMAGES_PLANT[2]}
                }

                binding.plantImageAdd.playAnimation()
                binding.plantImageAdd.loop(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        binding.btAdd.setOnClickListener{
            title= binding.taskTitleET
            description= binding.taskDescriptionET
            time= binding.totalTimeET

            //if not empty
            if(title.text.isNotBlank()
                &&description.text.isNotBlank()
                &&time.text.isNotBlank()){
                myViewModel.addTask(Task(0,title.text.toString(),description.text.toString(),type,"new",time.text.toString().toLong()*60000,time.text.toString().toLong()*60000))
                Toast.makeText(this.context,"Task Added successfully",Toast.LENGTH_SHORT).show()
                hideKeyboard()
                Navigation.findNavController(view).navigate(R.id.action_addTaskFragment_to_homeFragment)
            }else
                Toast.makeText(this.context,"Do not leave them empty!",Toast.LENGTH_SHORT).show()
        }

        binding.btBack.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_addTaskFragment_to_homeFragment)
        }

        return view
    }

    fun hideKeyboard()
    {
        // Hide Keyboard
        val hideKeyboard = ContextCompat.getSystemService( requireContext(), InputMethodManager::class.java)
        hideKeyboard?.hideSoftInputFromWindow( getActivity()?.currentFocus?.windowToken, 0)
    }


}