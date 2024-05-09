package com.example.favoriteprofessorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import org.json.JSONArray
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {
    // hardcoding searched_class for now but this will be whatever is searched in MainActivity
    private var searched_class : String = MainActivity.search_query
    private lateinit var textView : TextView
    private lateinit var listView : ListView
    private var returned_professors : ArrayList<String> = ArrayList<String>()
    private val SA : String = "SearchActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        
        var button2 : ImageButton = findViewById(R.id.star)
        button2.x = -60.0f

        var button3 : ImageButton = findViewById(R.id.home)
        button3.x = 80.0f
        listView = findViewById(R.id.professor_list)

        textView = findViewById(R.id.professor_search)
        textView.text = searched_class + " Professors"

        var valueObject = MainActivity.classes_snapshot?.value
        if (valueObject != null) {
            Log.w(SA, "Found searched classes!")
            var value: String = valueObject.toString()
            var jsonObject : JSONObject = JSONObject(value)
            displayProfessors(jsonObject)
        } else {
            Log.w(SA, "Did not find the searched classes")
        }
    }

    fun displayProfessors(jsonObject : JSONObject) {
        Log.w(SA, "Displaying professors...")
        // get list of professors for the given class

        var jsonArray : JSONArray = jsonObject.getJSONArray(searched_class)

        // add professors to Professor list if not already in it
        for (i in 0 until jsonArray.length()) {
            var name = jsonArray.getString(i)
            if (MainActivity.professorsForClassQuery.getProfessor(name) == null) {
                MainActivity.professorsForClassQuery.addProfessor(name)
            }
            returned_professors.add(name)
        }

        // display the professors
        var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, returned_professors)
        listView.adapter = adapter

        // set up event handling
        var lih : ListItemHandler = ListItemHandler()
        listView.setOnItemClickListener(lih)
    }

    inner class ListItemHandler : AdapterView.OnItemClickListener {
        override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            val profName = returned_professors.get(p2)
            Log.w(SA, "User clicked professor: " + profName)
            var myIntent : Intent = Intent(this@SearchActivity, ReviewActivity::class.java)
            myIntent.putExtra("professorName", profName)
            startActivity(myIntent)
        }

    }

    fun goHome(v : View){
        Log.w("MainActivity","home from search activity")
        var myIntent : Intent = Intent(this@SearchActivity, MainActivity::class.java)
        startActivity(myIntent)
    }

    fun goFavs(v:View){
        var myIntent : Intent = Intent(this@SearchActivity, FavoritesActivity::class.java)
        startActivity(myIntent)
    }

    fun goReview(v: View){
        var myIntent : Intent = Intent(this@SearchActivity, AddReview::class.java)
        startActivity(myIntent)
    }
}