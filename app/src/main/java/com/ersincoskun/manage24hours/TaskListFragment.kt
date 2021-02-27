package com.ersincoskun.manage24hours

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ersincoskun.manage24hours.databinding.FragmentTaskListBinding
import com.ersincoskun.manage24hours.model.Task


class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val view= binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exapmleList= listOf<Task>(
            Task("exapmle 1","asdasa","12:22AM"),
            Task("exapmle 2","aasdada","12:00PM"),
            Task("exapmle 3","dfgfhfgfj","08:12AM"),
            Task("exapmle 4","asdasd","12:22AM"),
            Task("exapmle 5","gjfjfg","12:22AM"),
        )
        val adapter=TaskAdapter(exapmleList)
        binding.taskListRecyclerView.adapter=adapter
        binding.button.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}