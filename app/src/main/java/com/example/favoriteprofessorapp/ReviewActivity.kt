package com.example.favoriteprofessorapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.common.internal.FallbackServiceBroker
import org.json.JSONArray
import org.json.JSONObject
import java.math.RoundingMode

class ReviewActivity : AppCompatActivity() {
    private lateinit var textView : TextView
    private lateinit var listView : ListView
    private lateinit var favoritesImage : ImageView
    private lateinit var averageRating : TextView
    private var favorited : Boolean = false
    private var reviews : ArrayList<String> = ArrayList<String>()
    private var ratings : ArrayList<String> = ArrayList<String>()
    private var ad : InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        textView = findViewById(R.id.professor_searched)
        textView.setText(SearchActivity.clicked_professor)

        listView = findViewById(R.id.review_list)
        averageRating = findViewById(R.id.average_rating)

        favoritesImage = findViewById(R.id.add_to_favorites)

        // determine whether professor is already favorited or not
        if (MainActivity.favorites.checkForProfessor(SearchActivity.clicked_professor)) {
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

    fun favoritesPressed() {
        if (favorited) {
            // unfavorite and remove from favorites
            favoritesImage.setImageResource(R.drawable.pink_outline)
            if (MainActivity.professors.getProfessor(SearchActivity.clicked_professor) != null) {
                var professor =
                    MainActivity.professors.getProfessor(SearchActivity.clicked_professor)
                MainActivity.favorites.removeProfessor(professor!!)
                var toast : Toast = Toast.makeText(this, SearchActivity.clicked_professor + " removed from favorites", Toast.LENGTH_SHORT)
                toast.show()
                favorited = false
            }

        } else {
            // favorite and add to favorites
            favoritesImage.setImageResource(R.drawable.red_star)
            if (MainActivity.professors.getProfessor(SearchActivity.clicked_professor) != null) {
                var professor = MainActivity.professors.getProfessor(SearchActivity.clicked_professor)
                MainActivity.favorites.addProfessor(professor!!)
                var toast : Toast = Toast.makeText(this, SearchActivity.clicked_professor + " added to favorites", Toast.LENGTH_SHORT)
                toast.show()
                favorited = true
            }
            // show interstitial ad
            var adUnitId : String = "ca-app-pub-3940256099942544/1033173712"
            var adRequest : AdRequest = AdRequest.Builder().build()
            var adLoad : AdLoad = AdLoad()
            InterstitialAd.load(this, adUnitId, adRequest, adLoad)
        }
    }

    fun displayReviews(jsonObject : JSONObject) {
        Log.w("MainActivity", "in display reviews")
        // get the list of reviews to display in list view
        var jsonObject2 : JSONObject = jsonObject.getJSONObject(SearchActivity.clicked_professor)
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
        startActivity(myIntent)
    }
    fun goHome(v : View){
      var myIntent : Intent = Intent(this@ReviewActivity, MainActivity::class.java)
      startActivity(myIntent)
    }
//
//    fun goFavs{
//      var myIntent : Intent = Intent(this@ReviewActivity, Favorites::class.java)
//      startActivity(myIntent)
//    }


    fun goReview(v: View){
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