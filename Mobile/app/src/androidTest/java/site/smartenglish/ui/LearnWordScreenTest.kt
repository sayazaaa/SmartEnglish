package site.smartenglish.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import site.smartenglish.ui.viewmodel.AudioPlayerViewModel
import site.smartenglish.ui.viewmodel.BackgroundImageViewmodel
import site.smartenglish.ui.viewmodel.ReviewViewmodel
import site.smartenglish.ui.viewmodel.ReviewWordInfo
import site.smartenglish.remote.data.GetWordResponse
import site.smartenglish.ui.viewmodel.NWordBookViewmodel
import site.smartenglish.remote.data.Example
import site.smartenglish.remote.data.SynonymsOrAntonyms

class ReviewWordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val reviewViewmodel = mockk<ReviewViewmodel>(relaxed = true)
    private val bgViewmodel = mockk<BackgroundImageViewmodel>(relaxed = true)
    private val nWordBookViewmodel = mockk<NWordBookViewmodel>(relaxed = true)
    private val audioPlayerViewModel = mockk<AudioPlayerViewModel>(relaxed = true)

    @Before
    fun setup() {
        // 模拟单词详情数据
        val wordDetail = ReviewWordInfo(
            stage = 0,
            word = GetWordResponse(
                word = "test",
                phonetic = "/test/",
                pronunciation = "test.mp3",
                explanations = listOf("n.测试", "v.测试"),
                examples = listOf(
                    Example(
                        english = "This is a test.",
                        chinese = "这是一个测试。",
                        audio = "test_example.mp3"
                    )
                ),
                synonyms = SynonymsOrAntonyms(
                    n = listOf("examination", "trial"),
                    v = listOf("examine", "try"),
                    a = null
                ),
                antonyms = null
            ),
            similarWords = listOf(
                GetWordResponse(word = "test", explanations = listOf("n.测试")),
                GetWordResponse(word = "rest", explanations = listOf("n.休息")),
                GetWordResponse(word = "best", explanations = listOf("adj.最好的")),
                GetWordResponse(word = "nest", explanations = listOf("n.巢"))
            )
        )

        // 模拟 ViewModel 状态
        every { reviewViewmodel.wordDetail } returns MutableStateFlow(wordDetail)
        every { reviewViewmodel.reviewedWordNum } returns MutableStateFlow(1)
        every { reviewViewmodel.targetReviewCount } returns MutableStateFlow(10)
        every { reviewViewmodel.isLoading } returns MutableStateFlow(false)
        every { reviewViewmodel.navigateBackSelection } returns MutableStateFlow(false)
        every { reviewViewmodel.navigateToFinish } returns MutableStateFlow(false)
        every { reviewViewmodel.snackBar } returns MutableStateFlow("")

        every { nWordBookViewmodel.isAnyNWordBook } returns MutableStateFlow(false)
        every { nWordBookViewmodel.isNWordBook } returns MutableStateFlow(mapOf())
    }

    @Test
    fun testStage0_DisplaysCorrectContent() {
        composeTestRule.setContent {
            ReviewWordScreen(
                reviewViewmodel = reviewViewmodel,
                bgViewmodel = bgViewmodel,
                nWordBookViewmodel = nWordBookViewmodel,
                audioPlayerViewModel = audioPlayerViewModel
            )
        }

        // 验证单词显示
        composeTestRule.onNodeWithText("test").assertIsDisplayed()

        // 验证音标显示
        composeTestRule.onNodeWithText("/test/").assertIsDisplayed()

        // 验证提示文本
        composeTestRule.onNodeWithText("请从下列 4 个选项中选择正确词义").assertIsDisplayed()

        // 验证选项显示
        composeTestRule.onNodeWithText("n.测试").assertIsDisplayed()
        composeTestRule.onNodeWithText("n.休息").assertIsDisplayed()
        composeTestRule.onNodeWithText("adj.最好的").assertIsDisplayed()
        composeTestRule.onNodeWithText("n.巢").assertIsDisplayed()

        // 验证"看答案"按钮存在
        composeTestRule.onNodeWithText("看答案").assertIsDisplayed()
    }

    @Test
    fun testStage0_SelectCorrectAnswer() {
        composeTestRule.setContent {
            ReviewWordScreen(
                reviewViewmodel = reviewViewmodel,
                bgViewmodel = bgViewmodel,
                nWordBookViewmodel = nWordBookViewmodel,
                audioPlayerViewModel = audioPlayerViewModel
            )
        }

        // 选择正确答案
        composeTestRule.onNodeWithText("n.测试").performClick()

        // 验证是否调用了 onCorrect 逻辑
        verify(timeout = 1500) { reviewViewmodel.passWord() }
    }

    @Test
    fun testStage0_SelectWrongAnswer() {
        composeTestRule.setContent {
            ReviewWordScreen(
                reviewViewmodel = reviewViewmodel,
                bgViewmodel = bgViewmodel,
                nWordBookViewmodel = nWordBookViewmodel,
                audioPlayerViewModel = audioPlayerViewModel
            )
        }

        // 选择错误答案
        composeTestRule.onNodeWithText("n.休息").performClick()

        // 验证是否调用了 onWrong 逻辑
        verify(timeout = 1500) { reviewViewmodel.failWord() }
    }

    @Test
    fun testSeeAnswerButton() {
        composeTestRule.setContent {
            ReviewWordScreen(
                reviewViewmodel = reviewViewmodel,
                bgViewmodel = bgViewmodel,
                nWordBookViewmodel = nWordBookViewmodel,
                audioPlayerViewModel = audioPlayerViewModel
            )
        }

        // 点击"看答案"按钮
        composeTestRule.onNodeWithText("看答案").performClick()

        // 验证是否调用了失败处理逻辑
        verify(timeout = 1500) { reviewViewmodel.failWord() }
    }

    @Test
    fun testDetailScreen_ShowsCorrectInfo() {
        // 设置状态为显示详情页面
        every { reviewViewmodel.wordDetail } returns MutableStateFlow(
            ReviewWordInfo(
                stage = 0,  // 设置为任意阶段
                word = GetWordResponse(
                    word = "test",
                    phonetic = "/test/",
                    pronunciation = "test.mp3",
                    explanations = listOf("n.测试", "v.测试"),
                    examples = listOf(
                        Example(
                            english = "This is a test.",
                            chinese = "这是一个测试。",
                            audio = "test_example.mp3"
                        )
                    ),
                    synonyms = SynonymsOrAntonyms(
                        n = listOf("examination"),
                        v = null,
                        a = null
                    ),
                    antonyms = null
                ),
                similarWords = listOf()
            )
        )

        composeTestRule.setContent {
            ReviewWordScreen(
                reviewViewmodel = reviewViewmodel,
                bgViewmodel = bgViewmodel,
                nWordBookViewmodel = nWordBookViewmodel,
                audioPlayerViewModel = audioPlayerViewModel
            )
        }

        // 先查看详情页面
        composeTestRule.onNodeWithText("看答案").performClick()

        // 等待详情页面显示
        composeTestRule.waitForIdle()

        // 验证详情页面的标签是否存在
        composeTestRule.onNodeWithText("例句").assertIsDisplayed()
        composeTestRule.onNodeWithText("同义词").assertIsDisplayed()

        // 验证例句内容
        composeTestRule.onNodeWithText("This is a test.").assertIsDisplayed()

        // 验证同义词内容
        composeTestRule.onNodeWithText("examination").assertIsDisplayed()

        // 验证下一词按钮
        composeTestRule.onNodeWithText("下一词").assertIsDisplayed()
    }

    @Test
    fun testNextWordButton() {
        // 设置状态为显示详情页面
        composeTestRule.setContent {
            ReviewWordScreen(
                reviewViewmodel = reviewViewmodel,
                bgViewmodel = bgViewmodel,
                nWordBookViewmodel = nWordBookViewmodel,
                audioPlayerViewModel = audioPlayerViewModel
            )
        }

        // 先查看详情页面
        composeTestRule.onNodeWithText("看答案").performClick()

        // 等待详情页面显示
        composeTestRule.waitForIdle()

        // 点击下一词按钮
        composeTestRule.onNodeWithText("下一词").performClick()

        // 验证是否调用了下一个单词的方法
        verify { reviewViewmodel.nextWord() }
    }
}