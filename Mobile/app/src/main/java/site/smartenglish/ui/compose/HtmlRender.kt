package site.smartenglish.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import site.smartenglish.R
import site.smartenglish.ui.theme.White

// 扩展支持的元素类型
enum class HtmlElementType {
    PARAGRAPH,
    IMAGE,
    HEADLINE,
    LIST,
    CAPTION,
    LINK
}

data class HtmlBlock(
    val type: HtmlElementType,
    val content: String,
    val attributes: Map<String, String> = emptyMap()  // 保存额外属性
)

fun parseHtmlToBlocks(html: String): List<HtmlBlock> {
    val document = Jsoup.parse(html)
    return parseElement(document.body())
}

private fun parseElement(element: Element): List<HtmlBlock> {
    val result = mutableListOf<HtmlBlock>()

    // 处理不同元素类型
    when {
        element.tagName().equals("h1", ignoreCase = true) -> {
            element.text().takeIf { it.isNotBlank() }?.let {
                result.add(HtmlBlock(HtmlElementType.HEADLINE, it))
            }
        }

        element.tagName().equals("p", ignoreCase = true) -> {
            element.text().takeIf { it.isNotBlank() }?.let {
                result.add(HtmlBlock(HtmlElementType.PARAGRAPH, it))
            }
        }

        element.tagName().equals("img", ignoreCase = true) -> {
            element.attr("src").takeIf { it.isNotBlank() }?.let {
                result.add(HtmlBlock(HtmlElementType.IMAGE, it))
            }
        }

        element.hasAttr("data-component") -> {
            when (element.attr("data-component")) {
                "text-block" -> {
                    element.select("p").forEach { p ->
                        p.text().takeIf { it.isNotBlank() }?.let {
                            result.add(HtmlBlock(HtmlElementType.PARAGRAPH, it))
                        }
                    }
                }

                "image-block" -> {
                    element.select("img").firstOrNull()?.attr("src")
                        ?.takeIf { it.isNotBlank() }
                        ?.let {
                            result.add(HtmlBlock(HtmlElementType.IMAGE, it))
                        }
                }

                "caption-block" -> {
                    element.select("figcaption").text()
                        .takeIf { it.isNotBlank() }
                        ?.let {
                            result.add(HtmlBlock(HtmlElementType.CAPTION, it))
                        }
                }
            }
        }

        element.tagName().equals("ul", ignoreCase = true) -> {
            val items =
                element.select("li").mapNotNull { it.text().takeIf { txt -> txt.isNotBlank() } }
            if (items.isNotEmpty()) {
                result.add(HtmlBlock(HtmlElementType.LIST, items.joinToString("\n• ")))
            }
        }

        element.tagName().equals("a", ignoreCase = true) -> {
            element.attr("href").takeIf { it.isNotBlank() }?.let { url ->
                val text = element.text().takeIf { it.isNotBlank() } ?: url
                result.add(HtmlBlock(HtmlElementType.LINK, text, mapOf("url" to url)))
            }
        }
    }

    // 递归处理子元素
    element.children().forEach { child ->
        result.addAll(parseElement(child))
    }

    return result
}

@Composable
fun HtmlRender(html: String) {
    val blocks = parseHtmlToBlocks(html)
    val density = LocalDensity.current

    Column(Modifier.fillMaxWidth()) {
        blocks.forEach { block ->
            when (block.type) {
                HtmlElementType.HEADLINE -> {
                    Text(
                        text = block.content,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = with(density) { 36.dp.toSp() },
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = White,
                        fontFamily = FontFamily(Font(R.font.times))
                    )
                    Spacer(Modifier.height(24.dp))
                }

                HtmlElementType.PARAGRAPH -> Text(
                    text = block.content,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = with(density) { 30.dp.toSp() },
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = White,
                    fontFamily = FontFamily(Font(R.font.times))
                )

                HtmlElementType.IMAGE -> AsyncImage(
                    model = block.content,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                else -> Unit
            }
        }
    }
}