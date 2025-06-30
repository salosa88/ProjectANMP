package com.ubaya.projectanmp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.ubaya.projectanmp.databinding.FragmentSignInBinding
import com.ubaya.projectanmp.viewmodel.AuthViewModel

class SignInFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding.btnSignIn.setOnClickListener {
            val user = binding.edtUser.text.toString()
            val pass = binding.edtPass.text.toString()

            viewModel.login(user, pass) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        }

        binding.txtToSignUp.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(SignInFragmentDirections.actionToSignUp())
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.messageLD.observe(viewLifecycleOwner, Observer { msg ->
            msg?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }
}