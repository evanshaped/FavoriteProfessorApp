package com.example.favoriteprofessorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import org.json.JSONArray
import org.json.JSONObject
import java.math.RoundingMode

class ReviewActivity : AppCompatActivity() {
    private lateinit var profName: String
    private lateinit var textView : TextView
    private lateinit var listView : ListView
    private lateinit var favoritesImage : ImageView
    private lateinit var averageRating : TextView
    private var favorited : Boolean = false
    private var reviews : ArrayList<String> = ArrayList<String>()
    private var ratings : ArrayList<String> = ArrayList<String>()
    private var ad : InterstitialAd? = null
    private val RA : String = "ReviewActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        profName = intent.getStringExtra("professorName")!!

        Log.w(RA, "Starting ReviewActivity for professor: " + profName)

        textView = findViewById(R.id.professor_searched)
        textView.setText(profName)

        listView = findViewById(R.id.review_list)
        averageRating = findViewById(R.id.average_rating)

        favoritesImage = findViewById(R.id.add_to_favorites)

        // determine whether professor is already favorited or not
        if (MainActivity.favoriteProfessors.getProfessor(profName) != null) {
            favorited = true
            favoritesImage.setImageResource(R.drawable.red_star)
        } else {
            favorited = false
            favoritesImage.setImageResource(R.drawable.pink_outline)
        }

        favoritesImage.setOnClickListener{favoritesPressed()}

        var valueObject = MainActivity.professors_snapshot?.value
        if (valueObject != null) {
            var value: String = valueObject.toString()
            var jsonObject : JSONObject = JSONObject(value)
            displayReviews(jsonObject)
        }
    }

    override fun onResume() {
        super.onResume()

        var valueObject = MainActivity.professors_snapshot?.value
        if (valueObject != null) {
            var value: String = valueObject.toString()
            var jsonObject : JSONObject = JSONObject(value)
            displayReviews(jsonObject)
        }
    }

    fun favoritesPressed() {
        if (favorited) {
            // unfavorite and remove from favorites
            favoritesImage.setImageResource(R.drawable.pink_outline)
            MainActivity.favoriteProfessors.removeProfessor(profName)
            MainActivity.favoriteProfessors.updateProfessorsPreferences(MainActivity.FAVORITES_PREFERENCE_KEY, applicationContext)
            var toast : Toast = Toast.makeText(this, profName + " removed from favorites", Toast.LENGTH_SHORT)
            toast.show()
            favorited = false

        } else {
            // favorite and add to favorites
            favoritesImage.setImageResource(R.drawable.red_star)
            MainActivity.favoriteProfessors.addProfessor(profName)
            MainActivity.favoriteProfessors.updateProfessorsPreferences(MainActivity.FAVORITES_PREFERENCE_KEY, applicationContext)
            var toast : Toast = Toast.makeText(this, profName + " added to favorites", Toast.LENGTH_SHORT)
            toast.show()
            favorited = true
            // show interstitial ad
            var adUnitId : String = "ca-app-pub-3940256099942544/1033173712"
            var adRequest : AdRequest = AdRequest.Builder().build()
            var adLoad : AdLoad = AdLoad()
            InterstitialAd.load(this, adUnitId, adRequest, adLoad)
        }
    }

    fun displayReviews(jsonObject : JSONObject) {
        Log.w("ReviewActivity", "Fetching review info for professor")
        // get the list of reviews to display in list view
        var jsonObject2 : JSONObject = jsonObject.getJSONObject(profName)
        var jsonReviewsArray : JSONArray = jsonObject2.getJSONArray("Reviews")
        var jsonRatingsArray : JSONArray = jsonObject2.getJSONArray("Ratings")
        var sum : Double = 0.0

        for (i in 0 until jsonReviewsArray.length()) {
            reviews.add(jsonReviewsArray.getString(i))
            ratings.add(jsonRatingsArray.getString(i))
            sum += jsonRatingsArray.getString(i).toDouble()
        }

        sum = (sum / jsonReviewsArray.length()).toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        averageRating.text = sum.toString()

        var adapter : CustomListAdapter = CustomListAdapter(this, reviews, ratings)
        listView.adapter = adapter
    }

    fun processReview(v : View){
        var myIntent : Intent = Intent(this@ReviewActivity, AddReview::class.java)
        myIntent.putExtra("professorName", profName)
        startActivity(myIntent)
    }

    fun goHome(v : View){
      var myIntent : Intent = Intent(this@ReviewActivity, MainActivity::class.java)
      startActivity(myIntent)
    }
    fun goFavs(v:View){
        var myIntent : Intent = Intent(this@ReviewActivity, FavoritesActivity::class.java)
        startActivity(myIntent)
    }

    fun goReview(v: View){
        // Goes to review page. Used by nav bar
        var myIntent : Intent = Intent(this@ReviewActivity, AddReview::class.java)
        startActivity(myIntent)
    }

    inner class AdLoad : InterstitialAdLoadCallback() {
        override fun onAdLoaded(p0: InterstitialAd) {
            super.onAdLoaded(p0)
            ad = p0
            ad!!.show(this@ReviewActivity)
            var manageAd: ManageAdShowing = ManageAdShowing()
            ad!!.fullScreenContentCallback = manageAd
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            Log.w("MainActivity", "error loading the ad: " + p0.message)
        }
    }

    inner class ManageAdShowing : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
        }

        override fun onAdClicked() {
            super.onAdClicked()
        }

        override fun onAdImpression() {
            super.onAdImpression()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
        }
    }

}