package com.example.favoriteprofessorapp

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class SearchActivity : AppCompatActivity() {
    // hardcoding searched_class for now but this will be whatever is searched in MainActivity
    private var searched_class : String = "CMSC335"
    private lateinit var professor_search : TextView
    private var mainActivity = MainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        professor_search = findViewById(R.id.professor_search)
        professor_search.text = searched_class + " Professors"

        loadProfessors(searched_class)
    }

    fun loadProfessors(searched_class : String) {
        val classesData : JSONArray = mainActivity.getClasses(searched_class)
        Log.w("MainActivity", "" + classesData.getString(0))
    }
}