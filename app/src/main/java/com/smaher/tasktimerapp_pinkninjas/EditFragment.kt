package com.smaher.tasktimerapp_pinkninjas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.smaher.tasktimerapp_pinkninjas.database.Task
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentAddTaskBinding
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentEditBinding


class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var myViewModel:TaskViewModel
    lateinit var title: EditText
    lateinit var description: EditText
    lateinit var time: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View

        _binding = FragmentEditBinding.inflate(inflater, container, false)
        view = binding.root

        /*when the fragment get the ViewModelProvider, it received the same
       SharedViewModel instance,*/
        myViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        val args = arguments
        val task: Task? = args?.getSerializable("passed_task") as Task?

            //check the type of plant
            var type = task!!.image

        binding.plantPickerSPinnerEdit.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when(position){
                    0 -> { binding.plantImageEdit.setAnimation(Constants.IMAGES_PLANT[0])
                        type= Constants.IMAGES_PLANT[0]}
                    1 -> { binding.plantImageEdit.setAnimation(Constants.IMAGES_PLANT[1])
                        type= Constants.IMAGES_PLANT[1]}
                    2 -> { binding.plantImageEdit.setAnimation(Constants.IMAGES_PLANT[2])
                        type= Constants.IMAGES_PLANT[2]}
                }

                binding.plantImageEdit.playAnimation()
                binding.plantImageEdit.loop(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        //set edit text to current data
        binding.taskTitleETUD.setText(task.name)
        binding.taskDescriptionETUD.setText(task.description)
        binding.totalTimeETUD.setText((task.totalTime/60000).toString())

        binding.btUpdate.setOnClickListener{
            title= binding.taskTitleETUD
            description= binding.taskDescriptionETUD
            time= binding.totalTimeETUD

            //if not empty
            if(title.text.isNotBlank()
                &&description.text.isNotBlank()
                &&time.text.isNotBlank()){
                myViewModel.updateTask(Task(task.id,title.text.toString(),description.text.toString(),type,"new",time.text.toString().toLong()*60000,time.text.toString().toLong()*60000))
                Toast.makeText(this.context,"Task Added successfully",Toast.LENGTH_SHORT).show()
                hideKeyboard()
                Navigation.findNavController(view).navigate(R.id.action_editFragment_to_homeFragment)
            }else
                Toast.makeText(this.context,"Do not leave them empty!",Toast.LENGTH_SHORT).show()
        }

            binding.btDelete.setOnClickListener {
                myViewModel.deleteTask(task)
                findNavController().navigate(R.id.action_editFragment_to_homeFragment)
                Toast.makeText(this.context, "Deleted Successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.btBackUD.setOnClickListener {

                findNavController().navigate(R.id.action_editFragment_to_homeFragment)
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