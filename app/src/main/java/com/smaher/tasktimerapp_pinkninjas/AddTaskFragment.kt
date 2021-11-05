package com.smaher.tasktimerapp_pinkninjas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smaher.tasktimerapp_pinkninjas.adapters.RVAdapter
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentAddTaskBinding
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentHomeBinding


class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        var view: View

        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        view = binding.root

        return view
    }



}