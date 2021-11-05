package com.smaher.tasktimerapp_pinkninjas

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smaher.tasktimerapp_pinkninjas.adapters.RVAdapter
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentAddTaskBinding
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var rvAdapter:RVAdapter
    private lateinit var animations :List<ObjectAnimator>
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //declare tasks view model
    lateinit var myViewModel: TaskViewModel
    var toggle = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view: View
        //for binding view
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        view = binding.root

        binding.expandLayout.setOnClickListener{
            if(toggle){
                binding.tvExpand.setBackgroundResource(R.drawable.ic_baseline_keyboard_double_arrow_up_24)
                animations = arrayOf(0f, -430F).map { translation ->
                    ObjectAnimator.ofFloat(binding.rvLayout, "translationY", translation).apply {
                        duration = 2000
                    }
                }
                toggle = false

            }else{
                binding.tvExpand.setBackgroundResource(R.drawable.ic_baseline_keyboard_double_arrow_down_24)
                animations = arrayOf(0f, 0F).map { translation ->
                    ObjectAnimator.ofFloat(binding.rvLayout, "translationY", translation).apply {
                        duration = 2000
                    }
                }
                toggle = true
            }

            val set = AnimatorSet()
            set.playTogether(animations)
            set.start()
        }

        //to navigate to setting fragment
        binding.settingsImageView.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        //to navigate to statistics fragment
        binding.statisticImageButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_statisticsFragment)
        }

        /*when the fragment get the ViewModelProvider, it received the same
         SharedViewModel instance,*/
        myViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        //we get the list of tasks from the database
        myViewModel.tasks.observe(viewLifecycleOwner, {list->
            list?.let { rvAdapter.update(it) }
        })

        setRV()

        return view
    }

    fun setRV(){
        rvAdapter = RVAdapter( requireContext() as MainActivity,this)
        binding.rvList.adapter = rvAdapter
        binding.rvList.layoutManager = GridLayoutManager(context,3)
    }

}