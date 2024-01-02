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
import com.google.gson.reflect.TypeToken

class MainViewModel(private var sharedPreferencesManager: SharedPreferencesManager) : ViewModel() {
    private val database = Firebase.database
    private val _todoList = MutableLiveData<List<String>>()
    private val _doneList = MutableLiveData<List<String>>()

    private val _todoListStringIds =
        listOf(R.string.todoList, R.string.workTodoList, R.string.sharedTodoList)
    private val _doneListStringIds =
        listOf(R.string.doneList, R.string.workDoneList, R.string.sharedDoneList)

    val todoList: LiveData<List<String>> get() = _todoList
    val doneList: LiveData<List<String>> get() = _doneList

    private var tabIndex = 0

    init {
        // read private and work from local space
        _todoList.value = sharedPreferencesManager.getList(_todoListStringIds[0])
        _doneList.value = sharedPreferencesManager.getList(_todoListStringIds[0])
        //saveSharedList(listOf(), _todoListStringIds[2])
        //saveSharedList(listOf(), _doneListStringIds[2])

        //val type = object : TypeToken<List<String>>() {}.type
        //database.getReference(sharedPreferencesManager.context.getString(_todoListStringIds[0])).get().addOnSuccessListener { _retrievedList.value = Gson().fromJson(it.value.toString(), type) }
        //database.getReference(sharedPreferencesManager.context.getString(_doneListStringIds[0])).get().addOnSuccessListener { _doneList.value = Gson().fromJson(it.value.toString(), type) }
    }

    fun markAsDone(text: String) {
        val currentList = _doneList.value?.toMutableList() ?: mutableListOf()

        if (currentList.contains(text)) {
            currentList.remove(text)
        } else {
            currentList.add(text)
        }
        _doneList.value = currentList
        saveList(currentList, _doneListStringIds[tabIndex])
    }

    fun updateList(text: String, addItem: Boolean, replace: String = "") {
        if (text == "") {
            return
        }
        val currentList = _todoList.value?.toMutableList() ?: mutableListOf()
        val doneList = _doneList.value?.toMutableList() ?: mutableListOf()
        if (addItem) {
            if (replace == "") {
                currentList.add(text)
            } else {
                currentList.replaceAll { x ->
                    if (x == replace) {
                        text
                    } else {
                        x
                    }
                }
                if (doneList.contains(replace))
                    doneList.replaceAll { x ->
                        if (x == replace) {
                            text
                        } else {
                            x
                        }
                    }
            }

        } else {
            currentList.remove(text)
            if (doneList.contains(text))
                doneList.remove(text)
        }
        _doneList.value = doneList
        _todoList.value = currentList
        saveList(currentList, _todoListStringIds[tabIndex])
        saveList(doneList, _doneListStringIds[tabIndex])
    }

    fun changeTab(index: Int) {
        if (index <= 1) {
            _todoList.value = sharedPreferencesManager.getList(_todoListStringIds[index])
            _doneList.value = sharedPreferencesManager.getList(_doneListStringIds[index])
        } else {
            readSharedList(
                sharedPreferencesManager.context.getString(_todoListStringIds[index]),
                true
            )
            readSharedList(
                sharedPreferencesManager.context.getString(_doneListStringIds[index]),
                false
            )
        }
        tabIndex = index
    }

    private fun saveList(myList: List<String>, listID: Int) {
        if (tabIndex <= 1) {
            sharedPreferencesManager.saveList(
                sharedPreferencesManager.context.getString(listID),
                myList
            )
        } else {
            saveSharedList(myList, listID)
        }
    }

    private fun readSharedList(key: String, isTodoList: Boolean) {
        val type = object : TypeToken<List<String>>() {}.type
        if (isTodoList) {
            database.getReference(key)
                .get()
                .addOnSuccessListener {
                    _todoList.value = Gson().fromJson(it.value.toString(), type)
                }
                .addOnFailureListener { _todoList.value = listOf() }
        } else {
            database.getReference(key)
                .get()
                .addOnSuccessListener {
                    _doneList.value = Gson().fromJson(it.value.toString(), type)
                }
                .addOnFailureListener { _doneList.value = listOf() }
                .addOnCanceledListener { _doneList.value = listOf() }
        }

    }

    private fun saveSharedList(myList: List<String>, listID: Int) {
        val myRef = database.getReference(sharedPreferencesManager.context.getString(listID))
        val gson = Gson()
        val json = gson.toJson(myList)
        myRef.setValue(json)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // TODO
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}