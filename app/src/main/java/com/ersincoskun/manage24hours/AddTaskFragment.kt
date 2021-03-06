package com.ersincoskun.manage24hours

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ersincoskun.manage24hours.databinding.FragmentAddTaskBinding
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
        binding.addTaskBtn.setOnClickListener {
            viewModel.notification(requireContext(), requireActivity())
            validation()
        }

    }

    fun timePicker() {

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

    fun validation() {
        if (binding.commentEditText.text.isNullOrEmpty()) binding.commentEditText.error =
            "it must not be empty"

        if (binding.titleEditText.text.isNullOrEmpty()) binding.titleTextInputLayout.error =
            "it must not be empty"
    }

}