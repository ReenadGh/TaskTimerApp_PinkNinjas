package com.smaher.tasktimerapp_pinkninjas

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.smaher.tasktimerapp_pinkninjas.adapters.RVAdapter
import com.smaher.tasktimerapp_pinkninjas.databinding.FragmentHomeBinding
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.lang.String
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {
    private val sharedPrefFile = "kotlinsharedpreference"
    private lateinit var sharedPreferences : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var rvAdapter:RVAdapter
    private lateinit var animations :List<ObjectAnimator>
    var _binding: FragmentHomeBinding? = null

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
    var myDBSeconds = -1L




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var view: View
        //for binding view
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        view = binding.root




        //shared preferences to check if this is the first time the user opens the app
        sharedPreferences = requireContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        editor=  sharedPreferences.edit()
        val sharedIdValue = sharedPreferences.getBoolean("start_instruction",Constants.Start_Instruction)
        if (sharedIdValue){
            //myscreen.visiblity = View.Visible
            editor.putBoolean("start_instruction",Constants.Start_Instruction)
            editor.apply()
            editor.commit()


            var instructionImgs = arrayOf(R.drawable.inst_1,R.drawable.inst_2,R.drawable.inst_3,R.drawable.inst_4,R.drawable.inst_5,R.drawable.inst_6);
            var index = 1
            binding.instrutionImg.setOnClickListener {
                binding.instrutionImg.setImageResource(instructionImgs[index]);
                index++
                if (index==instructionImgs.size){
                    binding.instrutionImg.isVisible = false
                    binding.homeLayout.isVisible = true

                }
            }

            Constants.Start_Instruction=false
            editor.putBoolean("start_instruction",Constants.Start_Instruction)
            editor.apply()
            editor.commit()

        }else{
           // Constants.Start_Instruction=true
           // Toast.makeText(context, "value is $sharedIdValue",Toast.LENGTH_LONG).show()
            //myscreen.visiblity = View.Gone

            binding.instrutionImg.isVisible = false
            binding.homeLayout.isVisible = true

            }




        /*when the fragment get the ViewModelProvider, it received the same
        SharedViewModel instance,*/
        myViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        //we get the list of tasks from the database
        myViewModel.tasks.observe(viewLifecycleOwner, {list->
            list?.let { rvAdapter.update(it) }
        })

        setRV()

        // recyclerview moving up & down
        binding.expandLayout.setOnClickListener{
            if(toggle){
                binding.tvExpand.setBackgroundResource(R.drawable.ic_baseline_keyboard_double_arrow_up_24)
                animations = arrayOf(0f, -470F).map { translation ->
                    ObjectAnimator.ofFloat(binding.rvLayout, "translationY", translation).apply { duration = 2000 }
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


        //to navigate to edit/delete fragment
        binding.editImageView.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_editFragment)
        }

        //to navigate to setting fragment
        binding.settingsImageView.setOnClickListener{
           // Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        //to navigate to statistics fragment
        binding.statisticImageButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_statisticsFragment)
        }


        //play and pause toggle on click
        binding.playImageView.setOnClickListener{
            if(rvAdapter.currentTask.name!="empty") {
                if(rvAdapter.isSelected()){
                    paused = if (paused) {
                        rvAdapter.isActive=true
                        binding.nextImageView.isVisible=false
                        binding.previousImageView.isVisible=false
                        startTimer(myDBSeconds)
                        binding.playImageView.setImageResource(R.drawable.pause_button)
                        false
                    } else {
                        rvAdapter.isActive=false
                        countdown_timer.cancel()
                        rvAdapter.currentTask.currentTime = myDBSeconds
                        myViewModel.updateTask(rvAdapter.currentTask)
                        binding.nextImageView.isVisible=true
                        binding.previousImageView.isVisible=true
                        binding.playImageView.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                        true
                    }
                }else{
                    Toast.makeText(context,"Select a task to start",Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(context,"Select a task to start",Toast.LENGTH_LONG).show()
            }
        }

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
                myDBSeconds = -1L
                rvAdapter.currentTask.status="completed"
                rvAdapter.currentTask.currentTime=0
                rvAdapter.notifyDataSetChanged()
                myViewModel.updateTask(rvAdapter.currentTask)
                loadConfetti()
            }

            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                myDBSeconds = millisUntilFinished
                binding.tvTimeHeader.setText(timeFormat(millisUntilFinished)) //set text
                //time_in_milli_seconds=millisUntilFinished
                //rvAdapter.currentTask.currentTime=time_in_milli_seconds/60000
                //myViewModel.updateTask(rvAdapter.currentTask)
            }

        }
        countdown_timer.start()
    }

    private fun loadConfetti() {
        //restart the seconds count
        time_in_milli_seconds = START_MILLI_SECONDS
        binding.myConfetti.build()
            .addColors(Color.RED, Color.WHITE, Color.MAGENTA, Color.YELLOW)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(Size(12))
            .setPosition(-50f, binding.myConfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }

    //Convert milliseconds into hour,minute and seconds
    @SuppressLint("DefaultLocale")
    fun timeFormat(millisUntilFinished: Long): kotlin.String {
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    millisUntilFinished
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    millisUntilFinished
                )
            )
        )
    }


}