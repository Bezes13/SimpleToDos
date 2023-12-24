package com.example.simpletodo

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.simpletodo.ui.theme.addColor
import com.example.simpletodo.ui.theme.textColor

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TextInputFieldWithAddButton (updateList: (String, Boolean) -> Unit){
    var newTodoText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        OutlinedTextField(
            value = newTodoText,
            onValueChange = { newTodoText = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .align(Alignment.CenterVertically),
            textStyle = TextStyle(
                color = textColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = textColor,
                unfocusedBorderColor = addColor,
            )
        )

        SmallFloatingActionButton(
            onClick = {
                updateList(newTodoText, true)
                newTodoText = ""
                keyboardController?.hide()
            },
            containerColor = addColor,
            contentColor = Color.Black,
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(Icons.Filled.Add, "Add new TODO")
        }
    }
}