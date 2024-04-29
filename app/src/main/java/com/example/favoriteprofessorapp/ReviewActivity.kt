package com.example.favoriteprofessorapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class ReviewActivity : AppCompatActivity() {
    private lateinit var textView : TextView
    private lateinit var listView : ListView
    private var reviews : ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        textView = findViewById(R.id.professor_searched)
        textView.setText(SearchActivity.clicked_professor)

        listView = findViewById(R.id.review_list)

        var valueObject = MainActivity.professors_snapshot?.value
        if (valueObject != null) {
            var value: String = valueObject.toString()
            var jsonObject : JSONObject = JSONObject(value)
            displayReviews(jsonObject)
        }
    }

    fun displayReviews(jsonObject : JSONObject) {
        // get the list of reviews to display in list view
        var jsonObject2 : JSONObject = jsonObject.getJSONObject(SearchActivity.clicked_professor)
        var jsonArray : JSONArray = jsonObject2.getJSONArray("Reviews")

        for (i in 0 until jsonArray.length()) {
            reviews.add(jsonArray.getString(i))
        }

        var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, reviews)
        listView.adapter = adapter
    }
}