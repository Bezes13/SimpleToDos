package com.example.simpletodo.todoList

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpletodo.R
import com.example.simpletodo.manager.SharedPreferencesManager
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
    private val _todoListStringIds =
        listOf(R.string.todoList, R.string.workTodoList, R.string.sharedTodoList)
    private val _doneListStringIds =
        listOf(R.string.doneList, R.string.workDoneList, R.string.sharedDoneList)

    private val _privateTodoList = MutableLiveData<List<String>>()
    private val _privateDoneList = MutableLiveData<List<String>>()
    private val _workTodoList = MutableLiveData<List<String>>()
    private val _workDoneList = MutableLiveData<List<String>>()
    private val _sharedTodoList = MutableLiveData<List<String>>()
    private val _sharedDoneList = MutableLiveData<List<String>>()

    val privateTodoList: LiveData<List<String>> get() = _privateTodoList
    val privateDoneList: LiveData<List<String>> get() = _privateDoneList
    val workTodoList: LiveData<List<String>> get() = _workTodoList
    val workDoneList: LiveData<List<String>> get() = _workDoneList
    val sharedTodoList: LiveData<List<String>> get() = _sharedTodoList
    val sharedDoneList: LiveData<List<String>> get() = _sharedDoneList

    private var tabIndex = 0

    init {
        readLocalLists()
        readFirebaseLists()
    }

    private fun readLocalLists() {
        _privateTodoList.value = sharedPreferencesManager.getList(_todoListStringIds[0])
        _privateDoneList.value = sharedPreferencesManager.getList(_doneListStringIds[0])
        _workTodoList.value = sharedPreferencesManager.getList(_todoListStringIds[1])
        _workDoneList.value = sharedPreferencesManager.getList(_doneListStringIds[1])
    }

    private fun readFirebaseLists() {
        readSharedList(_todoListStringIds[2], isTodoList = true, destination = _sharedTodoList)
        readSharedList(_doneListStringIds[2], isTodoList = false, destination = _sharedDoneList)
    }

    private fun readSharedList(
        key: Int,
        isTodoList: Boolean,
        destination: MutableLiveData<List<String>>
    ) {
        val type = object : TypeToken<List<String>>() {}.type
        database.getReference(sharedPreferencesManager.context.getString(key))
            .get()
            .addOnSuccessListener {
                destination.value = Gson().fromJson(it.value.toString(), type)
            }
            .addOnFailureListener {
                if (isTodoList) _privateTodoList.value = emptyList() else _privateDoneList.value =
                    emptyList()
            }
    }

    fun markAsDone(text: String) {
        val currentList = getDoneList()

        if (currentList.contains(text)) {
            currentList.remove(text)
        } else {
            currentList.add(text)
        }
        when (tabIndex) {
            0 -> _privateDoneList.value = currentList
            1 -> _workDoneList.value = currentList
            else -> _sharedDoneList.value = currentList
        }

        saveList(currentList, _doneListStringIds[tabIndex])
    }

    fun updateList(text: String, addItem: Boolean, replace: String = "") {
        if (text.isEmpty()) return

        val currentList = getCurrentList()
        val doneList = getDoneList()

        if (addItem) {
            updateListsOnAdd(currentList, doneList, text, replace)
        } else {
            updateListsOnRemove(currentList, doneList, text)
        }

        updateLiveData(currentList, doneList)
        saveLists(currentList, doneList)
    }

    private fun getCurrentList(): MutableList<String> {
        return when (tabIndex) {
            0 -> _privateTodoList.value?.toMutableList() ?: mutableListOf()
            1 -> _workTodoList.value?.toMutableList() ?: mutableListOf()
            else -> _sharedTodoList.value?.toMutableList() ?: mutableListOf()
        }
    }

    private fun getDoneList(): MutableList<String> {
        return when (tabIndex) {
            0 -> _privateDoneList.value?.toMutableList() ?: mutableListOf()
            1 -> _workDoneList.value?.toMutableList() ?: mutableListOf()
            else -> _sharedDoneList.value?.toMutableList() ?: mutableListOf()
        }
    }

    private fun updateListsOnAdd(
        currentList: MutableList<String>,
        doneList: MutableList<String>,
        text: String,
        replace: String
    ) {
        if (replace.isEmpty()) {
            currentList.add(text)
        } else {
            currentList.replaceAll { if (it == replace) text else it }
            if (doneList.contains(replace)) doneList.replaceAll { if (it == replace) text else it }
        }
    }

    private fun updateListsOnRemove(
        currentList: MutableList<String>,
        doneList: MutableList<String>,
        text: String
    ) {
        currentList.remove(text)
        if (doneList.contains(text)) doneList.remove(text)
    }

    private fun updateLiveData(todos: List<String>, doneItems: List<String>) {
        when (tabIndex) {
            0 -> {
                _privateTodoList.value = todos
                _privateDoneList.value = doneItems
            }

            1 -> {
                _workTodoList.value = todos
                _workDoneList.value = doneItems
            }

            else -> {
                _sharedTodoList.value = todos
                _sharedDoneList.value = doneItems
            }
        }
    }

    private fun saveLists(currentList: List<String>, doneList: List<String>) {
        saveList(currentList, _todoListStringIds[tabIndex])
        saveList(doneList, _doneListStringIds[tabIndex])
    }

    fun changeTab(index: Int) {
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

    private fun saveSharedList(myList: List<String>, listID: Int) {
        val myRef = database.getReference(sharedPreferencesManager.context.getString(listID))
        val gson = Gson()
        val json = gson.toJson(myList)
        myRef.setValue(json)
        val type = object : TypeToken<List<String>>() {}.type
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                if (_todoListStringIds.contains(listID)){
                    _sharedTodoList.value = Gson().fromJson(value, type) ?: emptyList()
                }else{
                    _sharedDoneList.value = Gson().fromJson(value, type) ?: emptyList()
                }

                Log.d(TAG, "Value is: $value")
                Log.d(TAG, "Value is: ${_sharedTodoList.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}