package com.example.favoriteprofessorapp

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomListAdapter(private val context : Activity, private val description : ArrayList<String>, private val imgid : ArrayList<Int>) : ArrayAdapter<String>(context, R.layout.activity_custom_list, description) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var inflater = context.layoutInflater
        var rowView = inflater.inflate(R.layout.activity_custom_list, null, true)

        var imageView = rowView.findViewById<ImageView>(R.id.list_icon)
        var descriptionText = rowView.findViewById<TextView>(R.id.list_description)

        Log.w("MainActivity", "in here")
        descriptionText.text = description[position]
        imageView.setImageResource(imgid[position])

        return rowView
    }
}