package site.smartenglish.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import site.smartenglish.ui.theme.DarkGrey
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.WordBookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordBookDetailScreen(
    wordBookId: Int, navigateBack: () -> Unit,
    viewModel: WordBookViewModel = hiltViewModel()
) {
    val wordList = viewModel.wordList.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val wordBookName = viewModel.currentWordBookName.collectAsState().value

    LaunchedEffect(wordBookId) {
        viewModel.getWordBookDetail(wordBookId)
    }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkGrey
                ),
                title = {
                    Text(
                        text = wordBookName, color = White, fontSize = 17.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = "返回",
                            tint = White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
            )
        }) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Orange)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(start = 32.dp)
            ) {
                wordList.forEach { word ->
                    Text(
                        word,
                        color = White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = White.copy(0.05f))
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

        }
    }
}