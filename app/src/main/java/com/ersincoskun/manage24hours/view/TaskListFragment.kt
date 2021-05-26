package com.ersincoskun.manage24hours.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.adapter.TaskAdapter
import com.ersincoskun.manage24hours.databinding.FragmentTaskListBinding
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.viewmodel.TaskListViewModel
import com.ersincoskun.manage24hours.viewmodel.TaskListViewModelFactory
import java.util.*


class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val adapter = TaskAdapter(mutableListOf())
    private lateinit var viewModel: TaskListViewModel
    private lateinit var viewModelFactory: TaskListViewModelFactory
    private val myTaskList = mutableListOf<Task>()
    private lateinit var mCountDownTimer: CountDownTimer
    private val CHANNEL_ID: String by lazy {
        "channelID"
    }
    private val CHANNEL_NAME: String by lazy {
        "channelName"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModelFactory = TaskListViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskListViewModel::class.java)
        clickActions()
        observeData()
        binding.taskListRecyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(binding.taskListRecyclerView)

    }

    private fun observeData() {
        viewModel.allTask.observe(viewLifecycleOwner, Observer<MutableList<Task>> {
            adapter.addTask(it)
            myTaskList.addAll(it)
            it.forEach { task ->
                val currentHour =
                    Calendar.getInstance().getTime().toString().split(" ")[3].split(":")[0]
                val currentMinute = Calendar.getInstance().getTime().toString().split(" ")[3].split(
                    ":"
                )[1]
                val taskHour = task.startTime.split(":")[0]
                val taskMinute = task.startTime.split(":")[1]
                val endHour = task.endTime.split(":")[0]
                val endMinute = task.endTime.split(":")[1]
                var hour = 0
                var minute = 0
                var second = 0
                val leftTime = calculateTimeLeftToStart(
                    currentHour.toInt(),
                    currentMinute.toInt(),
                    endHour.toInt(),
                    endMinute.toInt()
                )
                if (currentHour == taskHour && currentMinute >= taskMinute && currentHour <= endHour) {
                    if (currentHour == endHour) {

                        if (currentMinute < endMinute) {
                            binding.runningTaskCardView.visibility = View.VISIBLE
                            binding.runningTaskTitleTV.visibility = View.VISIBLE
                            binding.titleTv.text = task.title
                            binding.descriptionTv.text = task.comment

                            val leftHour = leftTime.split(":")[0].toInt()
                            val leftMinute = leftTime.split(":")[1].toInt()
                            val totalSecond = (leftHour * 60 * 60 + leftMinute * 60).toLong()

                            mCountDownTimer = object : CountDownTimer(totalSecond * 1000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    val timerSecond = ((millisUntilFinished / 1000) % 60)
                                    val timerMinute = ((millisUntilFinished / 1000) / 60)
                                    val timerHour = (((millisUntilFinished / 1000) / 60)) / 60
                                    binding.startAndEndTimeTv.text =
                                        "${if (timerHour >= 10) "${timerHour}" else "0${timerHour}"}:${if (timerMinute >= 10) "${timerMinute}" else "0${timerMinute}"}:${if (timerSecond >= 10) "${timerSecond}" else "0${timerSecond}"}"

                                }

                                override fun onFinish() {
                                    binding.runningTaskCardView.visibility = View.GONE
                                    binding.runningTaskTitleTV.visibility = View.GONE
                                    this.cancel()
                                }
                            }.start()

                        }
                    } else {
                        binding.runningTaskCardView.visibility = View.VISIBLE
                        binding.runningTaskTitleTV.visibility = View.VISIBLE
                        binding.titleTv.text = task.title
                        binding.descriptionTv.text = task.comment

                        val leftHour = leftTime.split(":")[0].toInt()
                        val leftMinute = leftTime.split(":")[1].toInt()
                        val totalSecond = (leftHour * 60 * 60 + leftMinute * 60).toLong()

                        mCountDownTimer = object : CountDownTimer(totalSecond * 1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                val timerSecond = ((millisUntilFinished / 1000) % 60)
                                val timerMinute = ((millisUntilFinished / 1000) / 60)
                                val timerHour = (((millisUntilFinished / 1000) / 60)) / 60
                                binding.startAndEndTimeTv.text =
                                    "${if (timerHour >= 10) "${timerHour}" else "0${timerHour}"}:${if (timerMinute >= 10) "${timerMinute}" else "0${timerMinute}"}:${if (timerSecond >= 10) "${timerSecond}" else "0${timerSecond}"}"

                            }

                            override fun onFinish() {
                                binding.runningTaskCardView.visibility = View.GONE
                                binding.runningTaskTitleTV.visibility = View.GONE
                                this.cancel()
                            }
                        }.start()

                    }

                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        mCountDownTimer.cancel()
        binding.runningTaskCardView.visibility = View.GONE
        binding.runningTaskTitleTV.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickActions() {
        binding.addTaskFAB.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }
    }

    val itemTouchHelper by lazy {
        // 1. Note that I am specifying all 4 directions.
        //    Specifying START and END also allows
        //    more organic dragging than just specifying UP and DOWN.
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or
                        ItemTouchHelper.DOWN or
                        ItemTouchHelper.START or
                        ItemTouchHelper.END, ItemTouchHelper.LEFT
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    val adapter = recyclerView.adapter as TaskAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    // 2. Update the backing model. Custom implementation in
                    //    MainRecyclerViewAdapter. You need to implement
                    //    reordering of the backing model inside the method.
                    // 3. Tell adapter to render the model update.
                    adapter.notifyItemMoved(from, to)
                    viewModel.updateList(from, to)
                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val position = viewHolder.adapterPosition
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            viewModel.deleteTask(myTaskList[position].taskId)
                            adapter.deleteTask(position)
                            WorkManager.getInstance(requireContext())
                                .cancelAllWorkByTag("task${myTaskList[position].taskId}")
                        }
                    }
                }
            }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    private fun calculateTimeLeftToStart(
        currentHour: Int,
        currentMinute: Int,
        endHour: Int,
        endMinute: Int
    ): String {
        var hour = 0
        var minute = 0

        when {
            currentHour > endHour -> {
                if (currentMinute != 0) {
                    hour = (23 - currentHour) + endHour
                    minute = (60 - currentMinute) + endMinute
                } else {
                    hour = (24 - currentHour) + endHour
                    minute = endMinute
                }
            }
            currentHour < endHour -> {
                if (currentMinute > endMinute) {
                    hour = (endHour - 1) - currentHour
                    minute = (60 - currentMinute) + endMinute
                } else {
                    hour = endHour - currentHour
                    minute = currentMinute + endMinute
                }
            }
            currentHour == endHour -> {
                hour = 0
                if (currentMinute < endMinute) {
                    minute = endMinute - currentMinute
                } else {
                    hour = 23
                    minute = 60 - (currentMinute - endMinute)
                }
            }
        }

        return "$hour:$minute"
    }

}