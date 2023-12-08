package com.example.simpletodo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private var sharedPreferencesManager: SharedPreferencesManager) : ViewModel() {
    private val _retrievedList = MutableLiveData<List<String>>()

    val retrievedList: LiveData<List<String>> get() = _retrievedList

    init {
        _retrievedList.value =
            sharedPreferencesManager.getList(sharedPreferencesManager.context.getString(R.string.todoList))
    }

    fun updateList(text: String, addItem: Boolean) {
        val currentList = _retrievedList.value?.toMutableList() ?: mutableListOf()

        if (addItem) {
            currentList.add(text)
        } else {
            currentList.remove(text)
        }

        _retrievedList.value = currentList
        saveList(currentList)
    }

    private fun saveList(myList: List<String>) {
        sharedPreferencesManager.saveList(
            sharedPreferencesManager.context.getString(R.string.todoList),
            myList
        )
    }
}