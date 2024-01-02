package com.example.simpletodo.todoList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpletodo.R
import com.example.simpletodo.generateQRCode
import com.example.simpletodo.ui.theme.addColor
import com.example.simpletodo.ui.theme.checkColor
import com.example.simpletodo.ui.theme.deleteColor
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
    val privateTodoList by mainViewModel.privateTodoList.observeAsState(initial = emptyList())
    val privateDoneList by mainViewModel.privateDoneList.observeAsState(initial = emptyList())
    val workTodoList by mainViewModel.workTodoList.observeAsState(initial = emptyList())
    val workDoneList by mainViewModel.workDoneList.observeAsState(initial = emptyList())
    val sharedTodoList by mainViewModel.sharedTodoList.observeAsState(initial = emptyList())
    val sharedDoneList by mainViewModel.sharedDoneList.observeAsState(initial = emptyList())

    MainScreen(
        mainViewModel::updateList,
        mainViewModel::updateList,
        listOf(privateTodoList, workTodoList, sharedTodoList),
        listOf(privateDoneList, workDoneList, sharedDoneList),
        mainViewModel::markAsDone,
        mainViewModel::changeTab
    )
}

@ExperimentalPagerApi
@Composable
fun MainScreen(
    updateList: (String, Boolean) -> Unit,
    replaceItem: (String, Boolean, String) -> Unit,
    todoLists: List<List<String>>,
    doneLists: List<List<String>>,
    markAsDone: (String) -> Unit,
    swapTab: (Int) -> Unit
) {
    var tabIndex by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState()
    LaunchedEffect(pagerState.currentPage) {
        val newTabIndex = pagerState.currentPage
        if (newTabIndex != tabIndex) {
            tabIndex = newTabIndex
            swapTab(tabIndex)
        }
    }

    LaunchedEffect(tabIndex) {
        pagerState.scrollToPage(tabIndex)
    }
    val tabs = listOf("Private", "Work", "Shared")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row( modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.Header),
                fontSize = 50.sp,
                style = TextStyle(
                    color = textColor,
                    fontWeight = FontWeight.ExtraBold,
                    textDecoration = TextDecoration.Underline, textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .weight(1f)
            )

            if(tabIndex == 2){
                SmallFloatingActionButton(
                    onClick = { generateQRCode("123", 64, 64) },
                    containerColor = deleteColor,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Filled.Share, "Generate QR")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
        ) {
            TabRow(
                selectedTabIndex = tabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(
                            pagerState,
                            tabPositions
                        )
                    )
                },
                backgroundColor = addColor, contentColor = textColor
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = {
                            tabIndex = index
                            swapTab(index)
                        },
                        selectedContentColor = checkColor, unselectedContentColor = textColor
                    )
                }
            }
            HorizontalPager(
                count = 3,
                state = pagerState,
            ) {
                Column {
                    ScrollableTodoList(
                        updateList = updateList,
                        doneList = doneLists[it],
                        todoList = todoLists[it],
                        markAsDone = markAsDone,
                        replaceItem = replaceItem
                    )
                    TextInputFieldWithAddButton(updateList)
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
@Preview
fun PreviewMainScreen() {
    MainScreen({ _, _ -> },
        { _, _,_ -> },
        listOf(listOf(
            "food",
            "eat an apple",
            "very very long text which could lead to problems",
            "code Apps"
        )),
        listOf( listOf("food")),
        { _ -> }, { _ -> })
}
