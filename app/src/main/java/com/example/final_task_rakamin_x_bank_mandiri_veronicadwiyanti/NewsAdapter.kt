package com.example.final_task_rakamin_x_bank_mandiri_veronicadwiyanti

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class NewsAdapter(private var newsList: List<HomeFragment.NewsItem>,
                  private var clickListener: OnClickListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADLINE = 1
        private const val VIEW_TYPE_LIST = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADLINE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
                HeadlineViewHolder(view)
            }
            VIEW_TYPE_LIST -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_news, parent, false)
                ListViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newsItem = newsList[position]

        when (holder) {
            is HeadlineViewHolder -> {
                holder.bind(newsItem)
            }
            is ListViewHolder -> {
                holder.bind(newsItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADLINE
        } else {
            VIEW_TYPE_LIST
        }
    }

    fun getData(): List<HomeFragment.NewsItem> {
        return newsList
    }

    fun updateData(newData: List<HomeFragment.NewsItem>) {
        newsList = newData
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onItemClick(newsItem: HomeFragment.NewsItem)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.clickListener = onClickListener
    }


    inner class HeadlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleHeadline)
        private val newsImageView: ImageView = itemView.findViewById(R.id.newsImageHeadline)
        private val sourceDateTextView: TextView = itemView.findViewById(R.id.sourceDateHeadNews)


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = newsList[position]
                    clickListener?.onItemClick(clickedItem)
                }
            }
        }

        fun bind(newsItem: HomeFragment.NewsItem) {
            titleTextView.text = newsItem.title

            // Load image using Glide library
            Glide.with(itemView.context)
                .load(newsItem.urlToImage)
                .into(newsImageView)

            // Set source and release date
            val sourceDateText = "${newsItem.author} • ${newsItem.publishedAt}"
            sourceDateTextView.text = sourceDateText
        }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleList)
        private val newsImageView: ImageView = itemView.findViewById(R.id.newsListImage)
        private val sourceDateTextView: TextView = itemView.findViewById(R.id.sourceDateListNews)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = newsList[position]
                    clickListener?.onItemClick(clickedItem)
                }
            }
        }

        fun bind(newsItem: HomeFragment.NewsItem) {
            titleTextView.text = newsItem.title

            titleTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))


            // Load image using Glide library
            Glide.with(itemView.context)
                .load(newsItem.urlToImage)
                .into(newsImageView)

            // Set source and release date
            val sourceDateText = "${newsItem.author} • ${newsItem.publishedAt}"
            sourceDateTextView.text = sourceDateText
        }
    }
}

