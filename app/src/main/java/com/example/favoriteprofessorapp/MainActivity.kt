package com.example.favoriteprofessorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var classes_db : DatabaseReference
    private lateinit var professors_db : DatabaseReference
    private lateinit var classes_listener : ClassesListener
    private lateinit var search_bar_listener : SearchView.OnQueryTextListener
    private lateinit var search_bar : SearchView
    private lateinit var preferences : SharedPreferences
    private val BRIGHTNESS_PREFERENCE_KEY : String = "brightness"
    private lateinit var last_search : String
    private val LAST_SEARCH_KEY : String = "last_search"
    private lateinit var last_search_view : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.w("MainActivity", "starting MainActivity")

        setFavoriteProfessorsFromSharedPreferences()

        setContentView(R.layout.activity_main)

        search_bar = findViewById(R.id.search_bar)
        search_bar.isSubmitButtonEnabled = true
        search_bar_listener = SearchBarListener()
        search_bar.setOnQueryTextListener(search_bar_listener)

        last_search_view = findViewById(R.id.lastSearch)

        getBrightnessPreference()
        last_search_view.text = "Last search: " + getLastSearch()

        var firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
        classes_db = firebase.getReference("Classes")
        professors_db = firebase.getReference("Professors")

        classes_listener = ClassesListener()
        classes_db.addValueEventListener(classes_listener)
        classes_db.orderByValue()

        var professor_listener : ProfessorListener = ProfessorListener()
        professors_db.addValueEventListener(professor_listener)

        //loadSearchResults()
    }

    fun setFavoriteProfessorsFromSharedPreferences() {
        favoriteProfessors = Professors()
        val sharedPreferences = applicationContext.getSharedPreferences(applicationContext.packageName + "_preferences", Context.MODE_PRIVATE)
        val savedString = sharedPreferences.getString(FAVORITES_PREFERENCE_KEY, "")
        if (savedString != "") {
            val arrayOfFavoriteProfNames: Array<String> = savedString?.split(",")?.toTypedArray() ?: arrayOf()
            for (profName in arrayOfFavoriteProfNames) {
                Log.w("MainActivity", "Found favorited prof in shared prefs: $profName")
                favoriteProfessors.addProfessor(profName)
            }
        } else {
            Log.w("MainActivity", "No favorited profs found in shared preferences")
        }
    }
    fun loadSearchResults() {
        var searchIntent : Intent = Intent(this, SearchActivity::class.java)
        startActivity(searchIntent)
    }

    fun getBrightnessPreference() {
        preferences = this.getSharedPreferences(this.packageName + "_preferences", Context.MODE_PRIVATE)
        brightness = preferences.getString(BRIGHTNESS_PREFERENCE_KEY, "light").toString()
    }

    fun setBrightnessPreference() {
        var editor = preferences.edit()
        editor.putString(BRIGHTNESS_PREFERENCE_KEY, brightness)
        editor.commit()
    }

    fun getLastSearch() : String {
        preferences = this.getSharedPreferences(this.packageName + "_preferences", Context.MODE_PRIVATE)
        last_search = preferences.getString(LAST_SEARCH_KEY, "").toString()
        return last_search
    }

    fun setLastSearch(query : String) {
        var editor = preferences.edit()
        editor.putString(LAST_SEARCH_KEY, query)
        editor.commit()
        Log.w("MainActivity", "Shared prefs for Last Search set as: " + query)
        last_search_view.text = "Last search: " + query
    }



    fun setBright(mode : String) {
        if (mode === "dark") {
            brightness = "dark"
            findViewById<TextView>(R.id.welcome).setTextAppearance(R.style.ProfessorTitleDark)
            findViewById<RelativeLayout>(R.id.background).setBackgroundColor(resources.getColor(R.color.black, null))
            findViewById<TextView>(R.id.lastSearch).setTextAppearance(R.style.TextDark)
            setBrightnessPreference()
        } else if (mode === "light") {
            brightness = "light"
            findViewById<TextView>(R.id.welcome).setTextAppearance(R.style.ProfessorTitleLight)
            findViewById<RelativeLayout>(R.id.background).setBackgroundColor(resources.getColor(R.color.white, null))
            findViewById<TextView>(R.id.lastSearch).setTextAppearance(R.style.TextLight)
            setBrightnessPreference()
        }
    }

    inner class ClassesListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            professorsForClassQuery = Professors()
            classes_snapshot = snapshot
            var key : String? = snapshot.key
            var valueObject = snapshot.value
            if (valueObject != null) {
                var value : String = valueObject.toString()
                var jsonObject : JSONObject = JSONObject(value)
                try {
                    var jsonArray : JSONArray = jsonObject.getJSONArray(search_query)
                    Log.w("MainActivity", "Adding professors for query $search_query...")
                    for (i in 0..jsonArray.length() - 1) {
                        val newProfName = jsonArray.getString(i)
                        professorsForClassQuery.addProfessor(newProfName)
                        Log.w("MainActivity", "Added professor $newProfName")
                    }
                } catch (e : Exception) {
                    Log.w("MainActivity", "Query not found: $search_query")
                }

                // this will eventually be called based on when the user clicks search
                // but just keeping it here for now to get it to work
            } else {
                Log.w("MainActivity", "No value found for classes snapshot")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("MainActivity", "reading failure: " + error.message)
        }
    }

    inner class SearchBarListener : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            Log.w("MainActivity", "text submitted")
            if (!query.equals("Search for a class...") && query != null) {
                Log.w("MainActivity", "QUERY IS " + query)
                search_query = query
                setLastSearch(query)
                classes_listener.onDataChange(classes_snapshot!!)
                loadSearchResults()
                return true
            } else {
                return false
            }
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    inner class ProfessorListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            professors_snapshot = snapshot
            var key : String? = snapshot.key
            var valueObject : Any? = snapshot.value
            if (valueObject != null) {
                var value : String = valueObject.toString()
                Log.w("MainActivity", "prof snapshot string value: $value")
//                var jsonObject : JSONObject = JSONObject(value)
//                Log.w("MainActivity", "prof snapshot json object: " + jsonObject)
//                var jsonObject2 : JSONObject = jsonObject.getJSONObject("Nelson Padua-Perez")
//                var jsonArray : JSONArray = jsonObject2.getJSONArray("Reviews")
//                var review1 : String = jsonArray.getString(0)
            } else {
                Log.w("MainActivity", "No value found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("MainActivity", "reading failure: " + error.message)
        }
    }

    fun goHome(v: View){
      var myIntent : Intent = Intent(this@MainActivity, MainActivity::class.java)
      startActivity(myIntent)
    }
    fun goFavs(v:View){
      var myIntent : Intent = Intent(this@MainActivity, FavoritesActivity::class.java)
      startActivity(myIntent)
    }

    fun goReview(v: View){
        var myIntent : Intent = Intent(this@MainActivity, AddReview::class.java)
        startActivity(myIntent)
    }
    companion object {
        var classes_snapshot : DataSnapshot? = null
        var professors_snapshot : DataSnapshot? = null
        var professorsForClassQuery : Professors = Professors()
        var favoriteProfessors : Professors = Professors()
        val FAVORITES_PREFERENCE_KEY : String = "favorites"
        var search_query : String = "Search for a class..."
        var brightness : String = "light"
    }
}