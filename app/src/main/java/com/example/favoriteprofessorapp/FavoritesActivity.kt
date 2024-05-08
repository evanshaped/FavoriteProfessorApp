package com.example.favoriteprofessorapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class FavoritesActivity : AppCompatActivity() {
    private lateinit var textView : TextView
    private lateinit var listView : ListView
    private lateinit var favorite_professors : Array<String>
    private val FA : String = "FavoritesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        listView = findViewById(R.id.professor_list)

        textView = findViewById(R.id.professor_search)
        textView.text = " Favorite Professors"

        favorite_professors = MainActivity.favorites.getProfessorNamesArray()

        Log.w(FA, "Displaying professors...")

        // display the professors
        var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, favorite_professors)
        listView.adapter = adapter

        // set up event handling
        var lih : ListItemHandler = ListItemHandler()
        listView.setOnItemClickListener(lih)
    }


    inner class ListItemHandler : AdapterView.OnItemClickListener {
        override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            val clicked_professor = favorite_professors.get(p2)
            Log.w(FA, "User clicked professor: " + clicked_professor)
            var myIntent : Intent = Intent(this@FavoritesActivity, ReviewActivity::class.java)
            myIntent.putExtra("professorName", clicked_professor)
            startActivity(myIntent)
        }

    }

    fun goHome(v:View){
      finish()
    }

//    fun goFavs{
//      var myIntent : Intent = Intent(this@SearchActivity, Favorites::class.java)
//      startActivity(myIntent)
//    }

    fun goReview(v: View){
        var myIntent : Intent = Intent(this@FavoritesActivity, AddReview::class.java)
        startActivity(myIntent)
    }
}