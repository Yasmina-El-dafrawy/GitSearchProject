package com.example.gitsearchproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private var currentPage = 1
    private var totalResults = 0
    private val apiService = RetrofitClient.apiService
      val mQuery = MutableLiveData<String>()
    private val _users = MutableLiveData<List<User>>(emptyList())
    val users: LiveData<List<User>> = _users

    fun searchUsers(query: String) {

        mQuery.value=query
        viewModelScope.launch {
          val response = apiService.searchUsers(query, page = currentPage)

           // _users.value = response.body()?.items ?: emptyList()

            val searchResponse = response.body()
            totalResults = searchResponse?.total_count ?: 0
            val newUsers = searchResponse?.items ?: emptyList()
            _users.value = _users.value.orEmpty() + newUsers
        }
    }

    fun loadMore(query: String) {
        if (_users.value?.size ?: 0 < totalResults) {
            currentPage++
            viewModelScope.launch {
                searchUsers(query)
            }
        }
    }
}

