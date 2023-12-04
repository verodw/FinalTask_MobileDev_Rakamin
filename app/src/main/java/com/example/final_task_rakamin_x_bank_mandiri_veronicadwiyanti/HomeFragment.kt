package com.example.final_task_rakamin_x_bank_mandiri_veronicadwiyanti

import android.annotation.SuppressLint
import android.content.Intent
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private val apiKey = "599f73bb3eff4b618f51c5e67cd1a17f"
    private val country = "us"
    private val category = "general"
    private val language = "en"

    // Deklarasikan variabel untuk elemen
    private lateinit var logoImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var headlineRecyclerView: RecyclerView
    private lateinit var newsListRecyclerView: RecyclerView

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val newsApiService: NewsApiService = retrofit.create()

    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false


    // TODO: Rename and change types of parameters
    private var headlineAdapter: NewsAdapter? = null
    private var newsListAdapter: NewsAdapter? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        logoImageView = view.findViewById(R.id.logo_mandiri)
        titleTextView = view.findViewById(R.id.titleTextView)

        headlineRecyclerView = view.findViewById(R.id.headlineRecyclerView)
        headlineRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        headlineAdapter = NewsAdapter(emptyList())
        headlineRecyclerView.adapter = headlineAdapter

        newsListRecyclerView = view.findViewById(R.id.newsListRecyclerView)
        newsListRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        newsListAdapter = NewsAdapter(emptyList())
        newsListRecyclerView.adapter = newsListAdapter

        // Set listener untuk Endless/Infinite Scrolling
        newsListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE
                    ) {
                        loadMoreItems()
                    }
                }
            }
        })
        // Set listener untuk membuka NewsDetailActivity
        newsListAdapter?.setOnClickListener(object : NewsAdapter.OnClickListener {
            override fun onItemClick(newsItem: NewsItem) {
                openNewsDetailActivity(newsItem)
            }
        })

        getTopHeadlinesAndNews()

        return view
    }

    private fun openNewsDetailActivity(newsItem: NewsItem) {
        val intent = Intent(requireContext(), NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailActivity.EXTRA_TITLE, newsItem.title)
        intent.putExtra(NewsDetailActivity.EXTRA_DESCRIPTION, newsItem.description)
        intent.putExtra(NewsDetailActivity.EXTRA_IMAGE_URL, newsItem.urlToImage)
        startActivity(intent)
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage += 1
        getTopHeadlinesAndNews()
    }

    private fun getTopHeadlinesAndNews() {
        // Get top headlines
        val headlineCall = newsApiService.getTopHeadlines(apiKey, country, category)

        headlineCall.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    val headlineItems = newsResponse?.articles ?: emptyList()
                    headlineAdapter?.updateData(headlineItems)
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Handle failure
            }
        })

        // Get all news
        val newsCall = newsApiService.getEverything(apiKey, country, language)

        newsCall.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    val newsItems = newsResponse?.articles ?: emptyList()
//                    newsListAdapter?.updateData(newsItems)
                    // Jika currentPage == 1, update headlineAdapter
                    if (currentPage == 1) {
                        newsListAdapter?.updateData(newsItems)
                    } else {
                        // Jika currentPage > 1, tambahkan ke data yang sudah ada pada newsListAdapter
                        val currentData = newsListAdapter?.getData() ?: emptyList()
                        newsListAdapter?.updateData(currentData + newsItems)

                    }

                    isLoading = false

                } else {
                    // Handle unsuccessful response
                    isLoading = false
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Handle failure
                isLoading = false
            }
        })
    }

    private fun <T> Call<T>.enqueue(callback: Callback<NewsResponse>) {
        enqueue(callback)
    }

    companion object {
        private const val PAGE_SIZE = 20
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    data class NewsResponse(
        val articles: List<NewsItem>,
    )

    data class NewsItem(
        val title: String,
        val description: String,
        val urlToImage: String,
        val author: String,
        val publishedAt: String,
    ) {


    }
}



//    private fun getTopHeadlines(adapter: NewsAdapter) {
//        val call = newsApiService.getTopHeadlines(apiKey, country, category)
//
//        call.enqueue(object : Callback<NewsResponse> {
//            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
//                if (response.isSuccessful) {
//                    val newsResponse = response.body()
//                    val newsItems = newsResponse?.articles ?: emptyList()
//                    adapter.updateData(newsItems)
//                    // TODO: Handle response, update UI with headlines


                    // Ambil headlineRecyclerView dari setiap item
//                    val headlineRecyclerViews: List<RecyclerView> = newsResponse?.articles?.mapNotNull {
//                        val itemView = layoutInflater.inflate(R.layout.item_news, null, false)
//                        itemView.findViewById<RecyclerView>(R.id.headlineRecyclerView)
//                    } ?: emptyList()
//
//                    headlineRecyclerViews.forEach { recyclerView ->
//                        newsListRecyclerView.addView(recyclerView)
//                    }

//                } else {
//                    // Handle unsuccessful response
//                }
//            }






