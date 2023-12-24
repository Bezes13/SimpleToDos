package com.example.simpletodo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel
) {
    val retrievedList by mainViewModel.retrievedList.observeAsState(initial = emptyList())
    val doneList by mainViewModel.doneList.observeAsState(initial = emptyList())
    MainScreen(mainViewModel::updateList, retrievedList, doneList, mainViewModel::markAsDone, mainViewModel::changeTab)
}

@ExperimentalPagerApi
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
    val pagerState = rememberPagerState() // 2.
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
                indicator = { tabPositions -> // 3.
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(
                            pagerState,
                            tabPositions
                        )
                    )},
                backgroundColor = addColor, contentColor = textColor) {
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
            HorizontalPager( // 4.
                count = 3,
                state = pagerState,
            ) {

            ScrollableTodoList(
                updateList = updateList,
                doneList = doneList,
                todoList = todoList,
                markAsDone = markAsDone,
            )}
            TextInputFieldWithAddButton(updateList)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
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
