package com.dicoding.dicodingevents.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevents.R
import com.dicoding.dicodingevents.data.response.Event
import com.dicoding.dicodingevents.databinding.ActivityDetailBinding
import com.dicoding.dicodingevents.model.DetailViewModel
import com.dicoding.dicodingevents.utils.changeFormatDate
import com.dicoding.dicodingevents.utils.cleanerHtmlText

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val viewModel : DetailViewModel by viewModels()
    private var activeEvent: Int = 0

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
        const val EXTRA_EVENT_ACTIVE = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)
        activeEvent = intent.getIntExtra(EXTRA_EVENT_ACTIVE, 0)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.detailEvent.observe(this) {
            if (it != null) {
                showDetailEvent(it)
            }
        }

        viewModel.getDetailEvent(id = eventId)
    }

    private fun showDetailEvent(event : Event) {
        with(binding) {
            tvEventName.setText(event.name)
            tvTypeEvent.setText(event.category)
            tvOwnerEvent.setText(String.format(this@DetailActivity.getString(R.string.format_owner), event.ownerName))
            tvTimeEvent.setText(changeFormatDate(event.beginTime))
            tvLocationEvent.setText(event.cityName)
            tvPatiesEvent.setText(String.format(this@DetailActivity.getString(R.string.format_parties), event.registrants, event.quota))
            val remainingQuote = event.quota - event.registrants
            tvQuoteEvent.setText(String.format(this@DetailActivity.getString(R.string.format_quote), remainingQuote))
            tvDescription.setText(cleanerHtmlText(event.description))
            if (activeEvent == 1 && remainingQuote != 0) {
                btnRegister.isEnabled = true
                btnSeeEvent.isEnabled = true
                btnRegister.setBackgroundColor(ContextCompat.getColor(this@DetailActivity, R.color.selected))
                btnSeeEvent.setBackgroundColor(ContextCompat.getColor(this@DetailActivity, R.color.selected))
                btnRegister.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } catch (e : Exception) {
                        Toast.makeText(
                            this@DetailActivity,
                            "Gagal membuka tautan error : ${e.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                btnSeeEvent.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } catch (e : Exception) {
                        Toast.makeText(
                            this@DetailActivity,
                            "Gagal membuka tautan error : ${e.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                btnRegister.isEnabled = false
                btnSeeEvent.isEnabled = true
                btnRegister.setBackgroundColor(ContextCompat.getColor(this@DetailActivity, R.color.unselected))
                btnSeeEvent.setBackgroundColor(ContextCompat.getColor(this@DetailActivity, R.color.selected))
                btnSeeEvent.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } catch (e : Exception) {
                        Toast.makeText(
                            this@DetailActivity,
                            "Gagal membuka tautan error : ${e.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.imgEvent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.progressDrawable = ContextCompat.getDrawable(this@DetailActivity, R.drawable.custom_progress_bar)
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}