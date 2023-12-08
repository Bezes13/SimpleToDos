package com.example.simpletodo

import android.content.Context
import androidx.lifecycle.ViewModel

class MainViewModel(private var sharedPreferencesManager: SharedPreferencesManager,
                    private var context: Context
) : ViewModel(){
    var _retrievedList = mutableListOf<String>()

    init {
        _retrievedList = sharedPreferencesManager.getList(context.getString(R.string.todoList)).toMutableList()
    }

    fun updateList(text : String, addItem: Boolean){
        if(addItem){
            _retrievedList.add(text)
        }else{
            _retrievedList.remove(text)
        }
        saveList(sharedPreferencesManager, _retrievedList, context)
    }

    fun saveList(
        sharedPreferencesManager: SharedPreferencesManager,
        myList: List<String>,
        context: Context
    ) {
        sharedPreferencesManager.saveList(context.getString(R.string.todoList), myList)
    }

}