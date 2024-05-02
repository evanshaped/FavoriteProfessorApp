package com.example.favoriteprofessorapp

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class CustomListAdapter(private val context : Activity, private val description : ArrayList<String>) : ArrayAdapter<String>(context, R.layout.activity_custom_list, description) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var images = intArrayOf(R.drawable.purple_outline, R.drawable.pink_outline)
        var inflater = context.layoutInflater
        var rowView = inflater.inflate(R.layout.activity_custom_list, null, true)

        var imageView = rowView.findViewById<ImageView>(R.id.list_icon)
        var descriptionText = rowView.findViewById<TextView>(R.id.list_description)

        descriptionText.text = description[position]
        //imageView.setImageResource(imgid[position])

        if (position % 2 == 0) {
            rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.purple))
            imageView.setImageResource(images[0])
        } else {
            imageView.setImageResource(images[1])
        }

        return rowView
    }
}