package com.example.favoriteprofessorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
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

        Log.w(FA, "FavoritesActivity started")

        listView = findViewById(R.id.professor_list)

        textView = findViewById(R.id.professor_favorite)
        textView.text = "Favorite Professors"

        favorite_professors = MainActivity.favoriteProfessors.getProfessorNamesArray()

        Log.w(FA, "Favorite profs detected are...")
        for (profName in favorite_professors) {
            Log.w(FA, "profName: $profName")
        }

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
            Log.w(FA, "User clicked professor for review: " + clicked_professor)
            var myIntent : Intent = Intent(this@FavoritesActivity, ReviewActivity::class.java)
            myIntent.putExtra("professorName", clicked_professor)
            startActivity(myIntent)
        }

    }

    fun processEmail(v:View) {
        var emailAddressEditText: EditText = findViewById<EditText>(R.id.emailAddress)
        var emailAddress: String = emailAddressEditText.text.toString()
        if (emailAddress != "") {
            val emailText = getProfessorClassesString()
            if (emailText != null) {
                var emailIntent: Intent = Intent(Intent.ACTION_SEND)
                emailIntent.setType("text/plain")
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Classes taught by your favorite professors!")
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailText)
                startActivity( Intent.createChooser( emailIntent, "Share favorite professor classes" ) )
            }
        }
    }

    fun getProfessorClassesString(): String? {
        var valueObject = MainActivity.classes_snapshot?.value
        if (valueObject != null) {
            Log.w(FA, "Found searched classes!")
            var value: String = valueObject.toString()
            var classesObject : JSONObject = JSONObject(value)

            val emailText: String? = formatProfessorClassesString(findClassesByProfessor(classesObject, favorite_professors))
            if (emailText == null) {
                Log.w(FA, "No classes found with these favorited professors")
                Toast.makeText(this, "No classes found with these favorited professors!", Toast.LENGTH_LONG).show()
            } else {
                Log.w(FA, "emailText is: " + emailText)
            }
            return emailText
        } else {
            Log.w(FA, "Could not retrive classes")
            Toast.makeText(this, "Could not retrive classes", Toast.LENGTH_LONG).show()
            return null
        }
    }

    fun findClassesByProfessor(classesObject: JSONObject, professors: Array<String>): MutableMap<String, ArrayList<String>> {
        val professorClassesMap = mutableMapOf<String, ArrayList<String>>()

        // Initialize the map with empty lists for each target
        for (target in professors) {
            professorClassesMap[target] = arrayListOf()
        }

        // Check each target against all keys
        val keys = classesObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val array = classesObject.getJSONArray(key)

            // Loop through each element in the JSON array
            for (i in 0 until array.length()) {
                val item = array.getString(i)

                // If the item is one of the targets, add the key to the appropriate list in the map
                if (professors.contains(item)) {
                    professorClassesMap[item]?.add(key)
                }
            }
        }
        return professorClassesMap
    }

    fun formatProfessorClassesString(professorClassesMap: MutableMap<String, ArrayList<String>>): String? {
        if (professorClassesMap.all { it.value.isEmpty() }) {
            return null
        }

        val builder = StringBuilder()
        // Iterate over the map entries
        professorClassesMap.forEach { (prof, classes) ->
            builder.append("Favorite Professor: $prof\n")  // Append the target (Professor)
            classes.forEach { single_class ->
                builder.append("+ Class: $single_class\n")  // Append each key (Class)
            }
            builder.append("\n")  // Add a newline for spacing between groups
        }

        return builder.toString()
    }

    fun goHome(v:View){
        var myIntent : Intent = Intent(this@FavoritesActivity, MainActivity::class.java)
        startActivity(myIntent)
    }

    fun goFavs(v:View){
        // Do nothing since we're already here
    }

    fun goReview(v: View){
        var myIntent : Intent = Intent(this@FavoritesActivity, AddReview::class.java)
        startActivity(myIntent)
    }
}