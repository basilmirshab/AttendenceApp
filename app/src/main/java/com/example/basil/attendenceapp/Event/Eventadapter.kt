package com.example.basil.attendenceapp.Event

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basil.attendenceapp.R
import kotlinx.android.synthetic.main.activity_main.view.*

class Eventadapter (val events:ArrayList<Events> ):RecyclerView.Adapter<Eventadapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false)
        return ViewHolder(layout)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events.get(position))
    }

    override fun getItemCount(): Int {
        return events.size
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(event:Events){
            view.name.text=event.name
            view.venue.text=event.venue
            view.date.text=event.date

        }


    }
}