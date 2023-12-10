package com.example.simpletodo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.simpletodo.ui.theme.addColor
import com.example.simpletodo.ui.theme.checkColor
import com.example.simpletodo.ui.theme.textColor

@Composable
fun MainScreen(
    mainViewModel: MainViewModel
) {
    val retrievedList by mainViewModel.retrievedList.observeAsState(initial = emptyList())
    val doneList by mainViewModel.doneList.observeAsState(initial = emptyList())
    MainScreen(mainViewModel::updateList, retrievedList, doneList, mainViewModel::markAsDone, mainViewModel::changeTab)
}

@Composable
fun MainScreen(
    updateList: (String, Boolean) -> Unit,
    todoList: List<String>,
    doneList: List<String>,
    markAsDone: (String) -> Unit,
    swapTab: (Int) -> Unit
) {
    var tabIndex by remember {
        mutableIntStateOf(0)
    }
    val tabs = listOf("Private", "Work", "Shared")
    Column {
        Text(
            text = stringResource(id = R.string.Header),
            fontSize = 50.sp,
            style = TextStyle(
                color = textColor,
                fontWeight = FontWeight.ExtraBold,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = tabIndex,
                containerColor = addColor, contentColor = textColor) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index
                            swapTab(index)
                        },
                        selectedContentColor = checkColor, unselectedContentColor = textColor
                    )
                }
            }
            ScrollableTodoList(
                updateList = updateList,
                doneList = doneList,
                todoList = todoList,
                markAsDone = markAsDone,
            )
            TextInputFieldWithAddButton(updateList)
        }
    }
}

@Composable
@Preview
fun PreviewMainScreen() {
    MainScreen({ _, _ -> },
        listOf(
            "Koks essen",
            "Kokain Bär sehen",
            "sehr sehr sehr extrem langer text, der Probleme macht",
            "Apps Coden"
        ),
        listOf("Kokain Bär sehen"),
        { _ -> },{ _ -> })
}
