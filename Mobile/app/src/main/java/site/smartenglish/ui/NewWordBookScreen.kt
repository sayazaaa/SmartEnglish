package site.smartenglish.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import site.smartenglish.remote.data.WordBook
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightGrey
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.WordBookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewWordBookScreen(
    navigateToHome: () -> Unit = {},
    viewModel: WordBookViewModel = hiltViewModel()
) {
    val wordbooks = viewModel.wordbooks.collectAsState().value
    val searchQuery by remember { mutableStateOf("") }
    var filteredWordbooks by remember { mutableStateOf<List<WordBook>>(emptyList()) }

    LaunchedEffect(searchQuery, wordbooks) {
        filteredWordbooks = if (searchQuery.isNotEmpty()) {
            wordbooks.filter { it.name?.contains(searchQuery, ignoreCase = true)?:false }
        } else {
            wordbooks
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Grey
                ), title = {
                    Text(
                        text = "选择您的词书", color = White, fontSize = 17.sp
                    )
                })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {


            // 词书列表
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
            ) {
                if (filteredWordbooks.isEmpty() && searchQuery.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "没有找到相关词书",
                        color = LightGrey,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    filteredWordbooks.forEach { wordbook ->
                        WordbookItem(
                            name = wordbook.name?:"",
                            cover = wordbook.cover,
                            wordCount = wordbook.wordcount?:0,
                            onClick = {
                                viewModel.selectWordBook(wordbook.id?:0)
                                navigateToHome()
                            })
                    }
                }
            }
        }
    }
}