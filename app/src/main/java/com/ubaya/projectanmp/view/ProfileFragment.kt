package com.ubaya.projectanmp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ubaya.projectanmp.databinding.FragmentProfileBinding
import com.ubaya.projectanmp.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.vm = vm
        binding.lifecycleOwner = viewLifecycleOwner

        vm.messageLD.observe(viewLifecycleOwner, Observer { msg ->
            Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
        })

        binding.btnSignOut.setOnClickListener {
            vm.signOut(requireActivity().application)
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}