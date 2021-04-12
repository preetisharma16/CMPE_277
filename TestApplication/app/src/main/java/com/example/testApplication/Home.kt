package com.example.testApplication

//import com.example.NewsBuddy.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.news_fragment.recyclerView



class Home : AppCompatActivity(), NewsItemClick {

    private lateinit var mAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView.layoutManager = LinearLayoutManager(this)
        getData()
        mAdapter = NewsAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun getData() {
        val url = "https://api.nytimes.com/svc/mostpopular/v2/emailed/7.json?api-key=dBsByEhj2J6SWCwytqpzLLwN3wyHhBf2"
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                    //val newsJsonArray = it.getJSONArray("articles")
                    val newsJsonArray = it.getJSONArray("results")
                    val newsArray = ArrayList<NewsData>()
                    for(i in 0 until newsJsonArray.length()) {
                        val newsJsonObject = newsJsonArray.getJSONObject(i)

                        val temp = newsJsonObject.getJSONArray("media")
                        val temp1 = temp.getJSONObject(0).getJSONArray("media-metadata")
                        val news = NewsData(
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("source"),
                                newsJsonObject.getString("url"),
                                temp1.getJSONObject(1).getString("url")
                               // val temp = newsJsonObject.getString("media")
                                //newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }

                    mAdapter.notifyNews(newsArray)
                },
                Response.ErrorListener {

                }
        )
        NewsSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    override fun onItemClicked(item: NewsData) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}