package com.tiizzer.narz.pandasoft.challenge.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tiizzer.narz.pandasoft.challenge.R
import com.tiizzer.narz.pandasoft.challenge.home.model.HomeListViewData
import kotlinx.android.synthetic.main.home_list_item.view.*

class HomeListAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<HomeListViewHolder>() {
    private val data = ArrayList<HomeListViewData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return HomeListViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = this.data.size

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        holder.container.setOnClickListener { this.listener.onItemClicked(position) }
        holder.title.text = this.data[position].title
        Glide
            .with(holder.itemView.context)
            .load(this.data[position].imageURL)
            .into(holder.image)
    }

    fun add(item: HomeListViewData) = this.data.add(item)
    fun addAll(items: List<HomeListViewData>) = this.data.addAll(items)
}

class HomeListViewHolder(private val view: View): RecyclerView.ViewHolder(view){
    val container = this.view.item_container
    val image = this.view.image
    val title = this.view.title
}