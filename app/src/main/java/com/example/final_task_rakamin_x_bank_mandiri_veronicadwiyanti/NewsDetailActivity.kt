package com.example.final_task_rakamin_x_bank_mandiri_veronicadwiyanti

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra(EXTRA_TITLE)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)

        setTitle(title)
        showDetail(title, description, imageUrl)
    }

    private fun showDetail(title: String?, description: String?, imageUrl: String?) {
        val titleTextView: TextView = findViewById(R.id.titleDetailTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionDetailTextView)
        val imageView: ImageView = findViewById(R.id.detailImageView)

        titleTextView.text = title
        descriptionTextView.text = description

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}