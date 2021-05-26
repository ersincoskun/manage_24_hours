package com.ersincoskun.manage24hours.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.work.Data
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.databinding.FragmentAddTaskBinding
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.viewmodel.AddTaskViewModel


class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val viewModel: AddTaskViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
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
            val title = binding.titleEditText.text.toString()
            val comment = binding.commentEditText.text.toString()
            val startTime = binding.startTimeEditText.text.toString()
            val endTime = binding.endTimeEditText.text.toString()
            val validation = validation()

            if (startTime == endTime) binding.startTimeTextInputLayout.error =
                "must not equal to end time"
            else binding.startTimeTextInputLayout.error = null

            if (validation) {
                val timeTake = viewModel.timeTakeGenerator(
                    binding.startTimeEditText.text.toString(),
                    binding.endTimeEditText.text.toString()
                )
                val sharedPreferences =
                    requireActivity().getSharedPreferences("taskSharedPref", Context.MODE_PRIVATE)
                val taskId = sharedPreferences.getLong("taskId", 1)
                sharedPreferences.edit().putLong("taskId", taskId + 1).apply()
                val task = Task(title, comment, startTime, endTime, timeTake, taskId)
                viewModel.storeTaskInSQLite(requireContext(), task)
                viewModel.setWorker(task, requireContext(),startTime,endTime)
                Navigation.findNavController(it)
                    .navigate(R.id.action_addTaskFragment_to_taskListFragment)
            }
        }

        binding.backButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_addTaskFragment_to_taskListFragment)
        }

    }

    private fun validation(): Boolean {
        val startTimeValidation =
            viewModel.validation(binding.startTimeTextInputLayout)

        val endTimeValidation =
            viewModel.validation(binding.endTimeTextInputLayout)

        val titleValidation =
            viewModel.validation(binding.titleTextInputLayout)

        val commentValidation =
            viewModel.validation(binding.commentTextInputLayout)

        val startEndTimeEqual = viewModel.validationStartEndTime(
            binding.startTimeTextInputLayout,
            binding.endTimeTextInputLayout
        )

        return startTimeValidation && endTimeValidation && titleValidation && commentValidation && startEndTimeEqual
    }

}