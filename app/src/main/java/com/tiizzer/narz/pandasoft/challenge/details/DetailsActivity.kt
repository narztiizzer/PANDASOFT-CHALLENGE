package com.tiizzer.narz.pandasoft.challenge.details

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.tiizzer.narz.pandasoft.challenge.R
import com.tiizzer.narz.pandasoft.challenge.main.MainActivity
import com.tiizzer.narz.pandasoft.challenge.utils.DialogHelper
import kotlinx.android.synthetic.main.details_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity: AppCompatActivity() {
    private val vmDetails: VMDetails by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        this.setupValue()
        this.setupAction()
        this.setupObserver()
    }

    override fun onResume() {
        super.onResume()
        this.vm().checkInteractionTime()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        this.vm().checkInteractionTime()
    }

    private fun setupValue(){
        this.vm().prepareData(intent)
    }

    private fun setupAction() {
        like.setOnClickListener { this.vm().like()  }
    }

    private fun setupObserver() {
        this.vm().image.observe(this, Observer { Glide.with(this).load(it).into(content_image) })
        this.vm().title.observe(this, Observer { content_title.text = it })
        this.vm().details.observe(this, Observer { content_details.text = it })
        this.vm().date.observe(this, Observer { content_date.text = it })
        this.vm().showMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        this.vm().interactionTimeout.observe(this, Observer {
            DialogHelper.showSessionExpireDialog(this) {
                this.vm().clearSession()
                this.openNewActivity()
            }
        })
    }

    private fun openNewActivity() {
        this.startActivity(Intent(this.applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun vm() = this.vmDetails
}