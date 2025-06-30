package com.ubaya.projectanmp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.ubaya.projectanmp.databinding.FragmentSignUpBinding
import com.ubaya.projectanmp.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding.btnSignUp.setOnClickListener {
            viewModel.register(
                binding.edtUser.text.toString(),
                binding.edtFirst.text.toString(),
                binding.edtLast.text.toString(),
                binding.edtPass.text.toString(),
                binding.edtRepeat.text.toString()
            )
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.messageLD.observe(viewLifecycleOwner, Observer { msg ->
            msg?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                if (it.startsWith("Registrasi berhasil")) {
                    Navigation.findNavController(binding.root).popBackStack()
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
