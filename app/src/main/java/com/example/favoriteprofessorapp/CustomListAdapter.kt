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
import org.w3c.dom.Text

class CustomListAdapter(private val context : Activity, private val description : ArrayList<String>, private val rating : ArrayList<String>) : ArrayAdapter<String>(context, R.layout.activity_custom_list, description) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var inflater = context.layoutInflater
        var rowView = inflater.inflate(R.layout.activity_custom_list, null, true)

        var ratingText = rowView.findViewById<TextView>(R.id.list_rating)
        var imageView = rowView.findViewById<ImageView>(R.id.list_icon)
        var descriptionText = rowView.findViewById<TextView>(R.id.list_description)

        ratingText.text = rating[position]
        descriptionText.text = description[position]

        imageView.setImageResource(R.drawable.pink_outline)

        return rowView
    }
}