package com.smaher.tasktimerapp_pinkninjas

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.smaher.tasktimerapp_pinkninjas.adapters.RVAdapter
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var rvAdapter:RVAdapter
    private lateinit var animations :List<ObjectAnimator>
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!
    //declare tasks view model
    lateinit var myViewModel: TaskViewModel
    var toggle = true
    var paused = true
    lateinit var countdown_timer :CountDownTimer
    var START_MILLI_SECONDS = 60000L
    var time_in_milli_seconds = 60000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view: View
        //for binding view
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        view = binding.root

        //play and pause toggle on click
        binding.playImageView.setOnClickListener{
            paused = if (paused){
                binding.playImageView.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                startTimer(START_MILLI_SECONDS)
                false
            }else{
                binding.playImageView.setImageResource(R.drawable.pause_button)
                countdown_timer.cancel()
                true
            }

        }

        // recyclerview moving up & down
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

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                loadConfetti()
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                val seconds = (time_in_milli_seconds / 1000) % 60
                binding.tvTimeHeader.text = "00:$seconds"
            }
        }
        countdown_timer.start()
    }

    private fun loadConfetti() {
        //restart the seconds count
        time_in_milli_seconds = START_MILLI_SECONDS
       /* binding.myConfetti.build()
            .addColors(Color.RED, Color.WHITE, Color.MAGENTA, Color.YELLOW)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(Size(12))
            .setPosition(-50f, binding.myConfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)*/
    }

}