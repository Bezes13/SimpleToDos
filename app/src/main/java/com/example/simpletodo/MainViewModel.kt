package com.example.simpletodo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MainViewModel(private var sharedPreferencesManager: SharedPreferencesManager) : ViewModel() {
    //private val database = Firebase.database("https://simpletodo-6b2a6-default-rtdb.europe-west1.firebasedatabase.app/")

    private val database = Firebase.database
    private val _retrievedList = MutableLiveData<List<String>>()
    private val _doneList = MutableLiveData<List<String>>()

    private val _todoListStringIds =
        listOf(R.string.todoList, R.string.workTodoList, R.string.sharedTodoList)
    private val _doneListStringIds =
        listOf(R.string.doneList, R.string.workDoneList, R.string.sharedDoneList)

    val retrievedList: LiveData<List<String>> get() = _retrievedList
    val doneList: LiveData<List<String>> get() = _doneList

    private var tabIndex = 0

    init {
        _retrievedList.value = sharedPreferencesManager.getList(sharedPreferencesManager.context.getString(R.string.todoList))
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
        saveList(currentList, _doneListStringIds[tabIndex])
    }

    fun updateList(text: String, addItem: Boolean) {
        if(text == ""){
            return
        }
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
        saveList(currentList, _todoListStringIds[tabIndex])
        saveList(doneList, _doneListStringIds[tabIndex])
    }

    fun changeTab(index: Int){
        _retrievedList.value = sharedPreferencesManager.getList(sharedPreferencesManager.context.getString(_todoListStringIds[index]))
        _doneList.value = sharedPreferencesManager.getList(sharedPreferencesManager.context.getString(_doneListStringIds[index]))
        tabIndex = index
    }

    private fun saveList(myList: List<String>, listID: Int) {
        sharedPreferencesManager.saveList(
            sharedPreferencesManager.context.getString(listID),
            myList
        )
        saveInFirebase(myList, listID)
    }

    private fun saveInFirebase(myList: List<String>, listID: Int){
        val myRef = database.getReference(listID.toString())
        val gson = Gson()
        val json = gson.toJson(myList)
        myRef.setValue(json)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun readFromFirebase(){
        // Read from the database

    }
}