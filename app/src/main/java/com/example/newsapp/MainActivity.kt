package com.example.newsapp

import android.content.SharedPreferences
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var madapter: NewListAdapter
    private var url = "https://saurav.tech/NewsAPI/top-headlines/category/technology/in.json"
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreference = getSharedPreferences("setting", MODE_PRIVATE)

        val str = readFrom("choice")
        str?.also {
            url = url.dropLast(18)
            url += it
            Log.d("@@@@","1 $url")
        }
        Log.d("@@@@","2 $url")
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData(url)
        madapter = NewListAdapter(this)
        recyclerView.adapter = madapter

    }

    private fun writeToShare(field: String, value: String) {

        val editor = sharedPreference.edit()
        editor.putString(field, value)
        editor.apply()
    }

    private fun readFrom(field: String): String? {

        return sharedPreference.getString(field, "technology/in.json")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var modifiedUrl = "https://saurav.tech/NewsAPI/top-headlines/category/"
        val str = when (item.itemId) {
            R.id.tech ->
            {
                "technology/in.json"
            }
            R.id.sports -> {
                "sports/in.json"
            }
            R.id.health -> {
                "health/in.json"
            }
            else -> {
                ""
            }

        }
        modifiedUrl += str
        writeToShare("choice", str)
        if (modifiedUrl.endsWith("json")) {
            fetchData(modifiedUrl)
        }

        return true
    }

    private fun fetchData(url: String) {
//       val url = "https://saurav.tech/NewsAPI/top-headlines/category/technology/in.json"
        // val url = "https://saurav.tech/NewsAPI/top-headlines/category/sports/in.json"
        // val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=28f68855b09f44d8892f0620b6a71237"
        val jsonobjectreq = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,

            {
                val newJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newJsonArray.length()) {
                    val newJsonObject = newJsonArray.getJSONObject(i)
                    val news = News(
                        newJsonObject.getString("title"),
                        newJsonObject.getString("author"),
                        newJsonObject.getString("url"),
                        newJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                madapter.updateNews(newsArray)
            },
            {
                Log.d("kkkk", it.message.toString())
            }
        )

        MySingleton.getInstance(this).addToRequestQueue(jsonobjectreq)
    }


    override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))


    }
}