package site.smartenglish.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.smartenglish.R
import site.smartenglish.ui.theme.White

data class WordSearchItemData(
    val title: String,
    val description: String,
)

@Composable
fun WordSearchItem(
    title: String,
    description: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 11.dp, end = 21.dp)
            .height(50.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = White,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = description,
            fontSize = 13.sp,
            color = White.copy(alpha = 0.5f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .weight(1f)
        )
//        Icon(
//            painter =
//                if (isFav) painterResource(R.drawable.kid_star_fill) else painterResource(R.drawable.kid_star),
//            contentDescription = "Favorite",
//            tint = White,
//            modifier = Modifier
//                .size(22.dp)
//                .clickable {
//                    onFavButtonClick()
//                }
//        )


    }

}