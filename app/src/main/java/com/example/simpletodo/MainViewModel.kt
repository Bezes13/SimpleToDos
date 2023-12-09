package com.example.simpletodo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private var sharedPreferencesManager: SharedPreferencesManager) : ViewModel() {
    private val _retrievedList = MutableLiveData<List<String>>()
    private val _doneList = MutableLiveData<List<String>>()

    val retrievedList: LiveData<List<String>> get() = _retrievedList
    val doneList: LiveData<List<String>> get() = _doneList

    init {
        _retrievedList.value =
            sharedPreferencesManager.getList(sharedPreferencesManager.context.getString(R.string.todoList))
        _doneList.value = sharedPreferencesManager.getList(sharedPreferencesManager.context.getString(R.string.doneList))
    }

    fun markAsDone(text: String){
        val currentList = _doneList.value?.toMutableList() ?: mutableListOf()

        if(currentList.contains(text)){
            currentList.remove(text)
        }else{
            currentList.add(text)
        }
        _doneList.value = currentList
        saveList(currentList, R.string.doneList)
    }

    fun updateList(text: String, addItem: Boolean) {
        val currentList = _retrievedList.value?.toMutableList() ?: mutableListOf()
        val doneList = _doneList.value?.toMutableList() ?: mutableListOf()

        if (addItem) {
            currentList.add(text)
        } else {
            currentList.remove(text)
            if(doneList.contains(text))
                doneList.remove(text)
        }
        _doneList.value = doneList
        _retrievedList.value = currentList
        saveList(currentList, R.string.todoList)
        saveList(doneList, R.string.doneList)
    }

    private fun saveList(myList: List<String>, listID: Int) {
        sharedPreferencesManager.saveList(
            sharedPreferencesManager.context.getString(listID),
            myList
        )
    }
}