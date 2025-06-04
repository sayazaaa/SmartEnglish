package site.smartenglish.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import site.smartenglish.R
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.LightOrange
import site.smartenglish.ui.theme.White

data class FavBottomSheetsItemData(
    val setId: Int,
    val title: String,
    val cover: String,
    val onAddToFav: () -> Unit = {},
    val onRemoveFromFav: () -> Unit = {},
    val onLongPress: () -> Unit = {},
    val isFav: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavBottomSheets(
    title: String,
    onDismiss: () -> Unit,
    onCreateFav: () -> Unit,
    error: Painter,
    items: List<FavBottomSheetsItemData> = emptyList(),
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
                FavBottomSheetsItem(it, error)
            }
            Spacer(modifier = Modifier.height(30.dp))

            // Add FavButton
            Row(
                modifier = Modifier
                    .clickable { onCreateFav() }
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 33.dp, end = 33.dp),
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
    item: FavBottomSheetsItemData,
    error: Painter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 33.dp, end = 33.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { item.onLongPress() }
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = item.cover,
            contentScale = ContentScale.Fit,
            error = error,
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        )
        Spacer(Modifier.width(20.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            color = White,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = if (item.isFav)
                painterResource(R.drawable.kid_star_fill)
            else painterResource(
                R.drawable.kid_star
            ),
            contentDescription = "Add to Fav",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(30.dp)
                .clickable {
                    if(item.isFav) {
                        item.onRemoveFromFav()
                    } else {
                        item.onAddToFav()
                    }
                },
            tint = White,
        )
    }
}

//@Preview
//@Composable
//fun FavBottomSheetsPreview() {
//    SmartEnglishTheme {
//        var showBottomSheet by remember { mutableStateOf(true) }
//        Box(modifier = Modifier.statusBarsPadding()) {
//            Button(onClick = { showBottomSheet = true }) { Text("open") }
//        }
//        if (showBottomSheet) {
//            FavBottomSheets(
//                title = "将单词收藏至",
//                onDismiss = { showBottomSheet = false },
//                onAddToFav = {},
//                onCreateFav = {  },
//                error = painterResource(R.drawable.words),
//                items = listOf(
//                    FavBottomSheetsItemData(1, "常用单词", "https://temp.im/"),
//                    FavBottomSheetsItemData(2, "生僻单词", "https://temp.im/50"),
//                    FavBottomSheetsItemData(3, "英语口语", "https://temp.im/50")
//                )
//            )
//        }
//    }
//}