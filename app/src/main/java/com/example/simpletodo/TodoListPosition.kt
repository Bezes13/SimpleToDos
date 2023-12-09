package com.example.simpletodo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpletodo.ui.theme.checkColor
import com.example.simpletodo.ui.theme.textColor
import com.example.simpletodo.ui.theme.deleteColor

@Composable
fun TodoListPosition(
    item: String,
    doneList: List<String>,
    updateList: (String, Boolean) -> Unit,
    markAsDone: (String) -> Unit
) {
    Divider()
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp) // Increase the height for the text fields
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
                        maxLines = 1,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(10.dp)
                            .widthIn(max = 230.dp), // Set a maximum width for the text
                    )

                    SmallFloatingActionButton(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterVertically),
                        onClick = { markAsDone(item) },
                        containerColor = checkColor
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Mark as Done")
                    }
                }
            }

            SmallFloatingActionButton(
                onClick = { updateList(item, false) },
                containerColor = deleteColor,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Filled.Clear, "Delete Todo")
            }
        }
    }
}

@Preview
@Composable
fun PreviewItem() {
    TodoListPosition(
        item = "Leben machen",
        doneList = emptyList(),
        updateList = { _, _ -> },
        markAsDone = {})
}