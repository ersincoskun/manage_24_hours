package com.ersincoskun.manage24hours.view

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.databinding.FragmentAddTaskBinding
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.viewmodel.AddTaskViewModel


class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private lateinit var viewModel: AddTaskViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddTaskViewModel::class.java)
        timePicker()
        clickActions()
    }

    private fun timePicker() {

        binding.startTimeEditText.setOnClickListener {
            viewModel.showTimePicker(
                requireActivity().supportFragmentManager,
                binding.startTimeEditText
            )
        }

        binding.endTimeEditText.setOnClickListener {
            viewModel.showTimePicker(
                requireActivity().supportFragmentManager,
                binding.endTimeEditText
            )
        }
    }

    private fun clickActions() {
        binding.addTaskBtn.setOnClickListener {
            //viewModel.notification(requireContext(), requireActivity())
            val title = binding.titleEditText.text.toString()
            val comment = binding.commentEditText.text.toString()
            val startTime = binding.startTimeEditText.text.toString()
            val endTime = binding.endTimeEditText.text.toString()
            val validation = validation()


            if (validation) {
                val timeTake = timeTakeGenerator()
                val task = Task(title, comment, startTime, endTime, timeTake)
                viewModel.storeTaskInSQLite(requireContext(), task)
                System.out.println(task.uuid)
                val action=AddTaskFragmentDirections.actionAddTaskFragmentToTaskListFragment(task.uuid)
                Navigation.findNavController(it).navigate(R.id.action_addTaskFragment_to_taskListFragment)
            }
        }

        binding.backButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_addTaskFragment_to_taskListFragment)
        }

        binding.button.setOnClickListener {
            viewModel.deleteAllTask(requireContext())
        }

    }

    private fun timeTakeGenerator(): String {
        val time = "${binding.startTimeEditText.text}:${binding.endTimeEditText.text}"
        val timeTake = viewModel.calculateTimeTake(time).split(":")
        val newMinute = timeTake[1].toInt() % 60
        val plusHour = timeTake[1].toInt() / 60
        val newHour = timeTake[0].toInt() + plusHour
        val newTime =
            "${if (newHour < 10) "0$newHour" else "$newHour"}:${if (newMinute < 10) "0$newMinute" else "$newMinute"}:00"
        return newTime
    }

    private fun validation(): Boolean {
        val startTimeValidation =
            viewModel.validation(binding.startTimeEditText, binding.startTimeTextInputLayout)
        val endTimeValidation =
            viewModel.validation(binding.endTimeEditText, binding.endTimeTextInputLayout)
        val titleValidation =
            viewModel.validation(binding.titleEditText, binding.titleTextInputLayout)
        val commentValidation =
            viewModel.validation(binding.commentEditText, binding.commentTextInputLayout)

        return startTimeValidation && endTimeValidation && titleValidation && commentValidation
    }

}