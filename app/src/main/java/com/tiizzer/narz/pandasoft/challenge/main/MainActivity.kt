package com.tiizzer.narz.pandasoft.challenge.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.tiizzer.narz.pandasoft.challenge.R
import com.tiizzer.narz.pandasoft.challenge.authentication.LoginFragment
import com.tiizzer.narz.pandasoft.challenge.home.view.HomeFragment
import com.tiizzer.narz.pandasoft.challenge.utils.DialogHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val vmMain: VMMain by viewModel()
    private val HOME_KEY = "HOME"
    private val LOGIN_KEY = "LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setupValue()
        this.setupObserver()
    }

    override fun onResume() {
        super.onResume()
        if(this.isShouldCheckInteract()) this.vm().checkInteractionTime()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if(this.isShouldCheckInteract()) this.vm().checkInteractionTime()
    }

    private fun setupValue(){
        this.vm().prepare()
    }

    private fun setupObserver(){
        this.vm().showLoginPage.observe(this, Observer {
            this.openLoginPage()
        })

        this.vm().showHomePage.observe(this, Observer {
            this.openHomePage()
        })

        this.vm().interactionTimeout.observe(this, Observer {
            DialogHelper.showSessionExpireDialog(this) {
                this.vm().clearSession()
                this.openNewActivity()
            }
        })
    }

    fun isShouldCheckInteract(): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(HOME_KEY)
        return fragment?.isVisible == true
    }

    fun openLoginPage() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, LoginFragment(), LOGIN_KEY)
            .commit()
    }

    fun openHomePage() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container,
                HomeFragment(), HOME_KEY)
            .commit()
    }

    fun openNewActivity() {
        this.startActivity(Intent(this.applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun vm() = this.vmMain
}
