package com.example.simpletodo.todoList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpletodo.ui.theme.addColor
import com.example.simpletodo.ui.theme.checkColor
import com.example.simpletodo.ui.theme.deleteColor
import com.example.simpletodo.ui.theme.textColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoListPosition(
    item: String,
    doneList: List<String>,
    updateList: (String, Boolean) -> Unit,
    replaceItem: (String, Boolean, String) -> Unit,
    markAsDone: (String) -> Unit
) {
    var edit by remember { mutableStateOf(false) }
    Divider()
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (edit) {
            var newTodoText by remember { mutableStateOf(item) }
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
                        color = textColor
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = textColor,
                        unfocusedBorderColor = addColor,
                    )
                )

                SmallFloatingActionButton(
                    onClick = {
                        replaceItem(newTodoText, true, item)
                        edit = false
                        keyboardController?.hide()
                    },
                    containerColor = addColor,
                    contentColor = Color.Black,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Filled.Create, "Add new TODO")
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        Text(
                            text = item,
                            style = TextStyle(
                                color = textColor,
                                fontSize = (getFontSize(item.length)).sp,
                                textDecoration = if (doneList.contains(item)) TextDecoration.LineThrough else TextDecoration.None,

                                ),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(10.dp), // Set a maximum width for the text
                        )
                    }
                }
                SmallFloatingActionButton(
                    onClick = { markAsDone(item) },
                    containerColor = checkColor,
                    modifier = Modifier
                        .align(Alignment.CenterVertically).size(30.dp)
                ) {
                    Icon(Icons.Filled.Check, "Mark as Done")
                }

                SmallFloatingActionButton(
                    onClick = { edit = true },
                    containerColor = checkColor,
                    modifier = Modifier
                        .align(Alignment.CenterVertically).size(30.dp)
                ) {
                    Icon(Icons.Filled.Edit, "Edit")
                }

                SmallFloatingActionButton(
                    onClick = { updateList(item, false) },
                    containerColor = deleteColor,
                    modifier = Modifier
                        .align(Alignment.CenterVertically).size(30.dp)
                ) {
                    Icon(Icons.Filled.Clear, "Delete Todo")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewItem() {
    TodoListPosition(
        item = "Again, we want this to be an very long text, so the Preview has something todo",
        doneList = emptyList(),
        updateList = { _, _ -> },
        replaceItem = { _, _, _ -> },
        markAsDone = {})
}