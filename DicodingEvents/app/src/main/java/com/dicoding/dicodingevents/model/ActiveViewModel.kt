package com.dicoding.dicodingevents.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevents.data.response.ListEventsItem
import com.dicoding.dicodingevents.data.response.ListEventsResponse
import com.dicoding.dicodingevents.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActiveViewModel : ViewModel() {

    private val _listEventActive = MutableLiveData<List<ListEventsItem>?>()
    val listEventActive : LiveData<List<ListEventsItem>?> = _listEventActive

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "ActiveViewModel"
    }

    init {
        getActiveList()
    }

    fun getActiveList(query : String? = null, limit : Int = 40) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListEvents(1, query, limit)
        client.enqueue(object : Callback<ListEventsResponse> {
            override fun onResponse(
                call: Call<ListEventsResponse>,
                response: Response<ListEventsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body()?.listEvents != null) {
                    _listEventActive.value = response.body()?.listEvents
                } else if (response.isSuccessful && response.body()?.listEvents == null) {
                    _listEventActive.value = null
                } else {
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListEventsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}