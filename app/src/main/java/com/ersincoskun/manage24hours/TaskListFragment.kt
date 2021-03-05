package com.ersincoskun.manage24hours

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.ersincoskun.manage24hours.adapter.TaskAdapter
import com.ersincoskun.manage24hours.databinding.FragmentTaskListBinding
import com.ersincoskun.manage24hours.viewmodel.TaskListViewModel


class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val adapter = TaskAdapter(listOf())
    private lateinit var viewModel: TaskListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }

        observeData()
        binding.taskListRecyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel = ViewModelProviders.of(this).get(TaskListViewModel::class.java)
        viewModel.addTask()
        viewModel.tasks.observe(viewLifecycleOwner, Observer {

            it?.let {
                adapter.bindList(it)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}