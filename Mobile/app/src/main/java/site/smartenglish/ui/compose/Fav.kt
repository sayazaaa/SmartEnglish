package site.smartenglish.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import site.smartenglish.R
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightOrange
import site.smartenglish.ui.theme.SmartEnglishTheme
import site.smartenglish.ui.theme.White

data class FavBottomSheetsItem(
    val id: Int,
    val title: String,
    val cover: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavBottomSheets(
    title: String,
    onDismiss: () -> Unit,
    onAddToFav: (Int) -> Unit,
    onCreateFav: () -> Unit,
    items: List<FavBottomSheetsItem> = emptyList(),
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Grey,
        modifier = Modifier.statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                color = White,
            )
            items.forEach {
                Spacer(modifier = Modifier.height(30.dp))
                FavBottomSheetsItem(it, onAddToFav)
            }
            Spacer(modifier = Modifier.height(30.dp))

            // Add FavButton
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 33.dp,end = 33.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(R.drawable.new_window),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .size(50.dp)
                )
                Spacer(Modifier.width(20.dp))
                Text(
                    text = "新建收藏夹",
                    fontSize = 16.sp,
                    color = LightOrange,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

}

@Composable
fun FavBottomSheetsItem(
    item: FavBottomSheetsItem,
    onAddToFav: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 33.dp,end = 33.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = item.cover,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )
        Spacer(Modifier.width(20.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            color = White,
            modifier = Modifier.weight(1f)
        )
        Icon(painter = painterResource(R.drawable.kid_star),
            contentDescription = "Add to Fav",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(30.dp)
                .clickable {
                    onAddToFav(item.id)
                },
            tint = White,
        )
    }
}

@Preview
@Composable
fun FavBottomSheetsPreview() {
    SmartEnglishTheme {
        var showBottomSheet by remember { mutableStateOf(true) }
        Box(modifier = Modifier.statusBarsPadding()) {
            Button(onClick = { showBottomSheet = true }) { Text("open") }
        }
        if (showBottomSheet) {
            FavBottomSheets(
                title = "将单词收藏至",
                onDismiss = { showBottomSheet = false },
                onAddToFav = {},
                onCreateFav = {  },
                items = listOf(
                    FavBottomSheetsItem(1, "常用单词", "https://temp.im/50"),
                    FavBottomSheetsItem(2, "生僻单词", "https://temp.im/50"),
                    FavBottomSheetsItem(3, "英语口语", "https://temp.im/50")
                )
            )
        }
    }
}