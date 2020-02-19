package com.tiizzer.narz.pandasoft.challenge.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiizzer.narz.pandasoft.challenge.R
import com.tiizzer.narz.pandasoft.challenge.details.DetailsActivity
import com.tiizzer.narz.pandasoft.challenge.home.model.HomeListViewData
import com.tiizzer.narz.pandasoft.challenge.home.viewmodel.VMHome
import kotlinx.android.synthetic.main.home_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment(),
    OnItemClickListener {
    private val vmHome: VMHome by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupValue()
        this.setupView()
        this.setupObserver()
    }

    override fun onItemClicked(position: Int) {
        this.vm().openDetailPageFromPosition(position)
    }

    private fun setupValue() {
        this.vm().getNewsItems()
    }

    private fun setupObserver() {
        this.vm().retrieveSuccess.observe(this, Observer {
            this.setHomeListData(it)
        })

        this.vm().errorMessage.observe(this, Observer {
            Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
        })

        this.vm().openDetailPage.observe(this, Observer {
            if(!this.vm().isInteractionTimeout()) {
                this.context!!.startActivity(
                    Intent(this.context, DetailsActivity::class.java).apply {
                        putExtra(vm().getParcelKey(), it)
                    }
                )
            }
        })
    }

    private fun setupView(){
        view?.item_list?.layoutManager = LinearLayoutManager(this.context).apply { orientation = LinearLayoutManager.VERTICAL }
    }

    private fun setHomeListData(data: List<HomeListViewData>){
        view?.item_list?.adapter = HomeListAdapter(
            this
        ).apply {
            addAll(data)
        }
    }

    private fun vm() = this.vmHome
}
