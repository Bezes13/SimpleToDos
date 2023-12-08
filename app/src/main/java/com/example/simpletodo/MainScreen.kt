package com.example.simpletodo


import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen(
    retrievedList: MutableList<String>,
    sharedPreferencesManager: SharedPreferencesManager,
    context: Context,
    updateList:(String, Boolean) -> Unit
) {
    var newTodoText by remember { mutableStateOf("") }
    Column {
        Text(text = stringResource(id = R.string.Header))
        retrievedList.forEach {
            Row {
                Text(text = it)
                Button(onClick = {updateList(it, false)
                }) {

                }
            }
        }
        OutlinedTextField(
            value = newTodoText,
            onValueChange = { newTodoText = it },
            label = { Text(stringResource(id = R.string.newTodo)) }
        )
        Button(onClick = {updateList(newTodoText, true)
        }) {

        }
    }
}

@Composable
@Preview
fun PreviewMainScreen() {

}
