package com.tiizzer.narz.pandasoft.challenge.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tiizzer.narz.pandasoft.challenge.R
import com.tiizzer.narz.pandasoft.challenge.main.MainActivity
import kotlinx.android.synthetic.main.login_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment: Fragment() {
    private val vmLogin: VMLogin by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupAction()
        this.setupObserver()
    }

    private fun setupAction(){
        view?.button?.setOnClickListener {
            this.vm().authenticate(
                view?.username?.text.toString(),
                view?.password?.text.toString()
            )
        }
    }

    private fun setupObserver() {
        this.vm().authenticationSuccess.observe(this, Observer {
            (this.activity!! as MainActivity).openHomePage()
        })

        this.vm().errorMessage.observe(this, Observer {
            Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun vm() = this.vmLogin
}