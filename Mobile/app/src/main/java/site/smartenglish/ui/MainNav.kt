package site.smartenglish.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import site.smartenglish.manager.SessionManager
import site.smartenglish.ui.viewmodel.AccountViewmodel
import site.smartenglish.ui.viewmodel.WordBookDetailScreen


@Serializable
object Login

@Serializable
object Register

@Serializable
object ResetPassword

@Serializable
object Home

@Serializable
object Profile

@Serializable
object Article

@Serializable
class ArticleDetail(
    val articleId: String
)

@Serializable
object Favorite

@Serializable
class FavoriteDetail(
    val setId: Int,
    val title: String
)

@Serializable
object DashBoard

@Serializable
object ChangeWordBook

@Serializable
object NewWordBook

@Serializable
class WordBookDetail(
    val wordBookId: Int
)

@Serializable
object LearnedWord


@Serializable
object LearnWord

@Serializable
class LearnWordFinished(
    val wordList: List<String>
)


@Serializable
object ReviewWord

@Serializable
class ReviewWordFinished(
    val wordList: List<String>
)



@Composable
fun MainNav(
    accountViewmodel: AccountViewmodel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authState by accountViewmodel.authState.collectAsState()

    // 根据token状态决定起始目的地
    val startDestination = remember {
        if (accountViewmodel.hasToken()) Home else Login
    }

    // 监听认证状态变化
    LaunchedEffect(authState) {
        if (authState == SessionManager.AuthState.UNAUTHORIZED) {
            navController.navigate(Login) {
                // 清除返回栈，防止用户按返回按钮回到需要授权的页面
                popUpTo(0) { inclusive = true }
            }
            accountViewmodel.resetAuthState()
        }
    }
    // 用户词书检查逻辑
    LaunchedEffect(Unit) {
        // 如果已登录，检查用户是否有词书
        if (accountViewmodel.hasToken()) {
            accountViewmodel.checkUserHasWordbook { hasWordbook ->
                if (!hasWordbook) {
                    navController.navigate(NewWordBook) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Login> {
            LoginScreen(
                navigateToRegister = { navController.navigate(Register) },
                navigateToResetPassword = { navController.navigate(ResetPassword) },
                navigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                })
        }
        composable<Register> { RegisterScreen(
            navigateToLogin = { navController.navigate(Login) { popUpTo(0) { inclusive = true } } },
        ) }
        composable<ResetPassword> { ResetPasswordScreen(
            navigateBack = { navController.navigate(Login) { popUpTo(0) { inclusive = true } } },
            navigateToLogin = { navController.navigate(Login) { popUpTo(0) { inclusive = true } } }
        ) }
        composable<Home> { HomeScreen(
            navigateToProfile = { navController.navigate(Profile) },
            navigateToArticle = { navController.navigate(Article){

            } },
            navigateToDashBoard = { navController.navigate(DashBoard) },
            navigateToLearnWord = { navController.navigate(LearnWord) },
            navigateToReviewWord = {
                navController.navigate(ReviewWord)
            }
        ) }
        composable<Profile> { ProfileScreen(
            navigateBack = { navController.popBackStack()},
        ) }
        composable<Article>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(150))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(150))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        ) {
            ArticleScreen(
                navigateArticleDetail = { articleId ->
                    navController.navigate(ArticleDetail(articleId))
                },
                navigateFavorite = {
                    navController.navigate(Favorite)
                },
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<ArticleDetail>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(150))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(150))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        ) { backStackEntry ->
            val articleDetail = backStackEntry.arguments?.let {
                ArticleDetail(it.getString("articleId") ?: "")
            }
            ArticleDetailScreen(
                articleId = articleDetail?.articleId ?: "",
                navigateBack = { navController.popBackStack() }
            )
        }
        composable<Favorite>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(150))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(150))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        ) {
            FavoriteScreen(
                navigateArticleDetail = { articleId ->
                    navController.navigate(ArticleDetail(articleId))
                },
                navigateFavoriteDetail = { id, title ->
                    navController.navigate(FavoriteDetail(id, title))
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<FavoriteDetail>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(150))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(150))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        ) { backStackEntry ->
            val favoriteDetail = backStackEntry.arguments?.let {
                FavoriteDetail(
                    it.getInt("setId"),
                    it.getString("title") ?: "收藏夹"
                )
            }
            FavoriteDetailScreen(
                setId = favoriteDetail?.setId ?: 0,
                title = favoriteDetail?.title ?: "",
                navigateToArticleDetail = { articleId ->
                    navController.navigate(ArticleDetail(articleId))
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<DashBoard>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(150))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(150))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        ) {
            DashBoardScreen(
                navigateToLearnedWords = {
                    navController.navigate(LearnedWord)
                },
                navigateToWordBookDetail = { wordBookId ->
                    navController.navigate(WordBookDetail(wordBookId))
                },
                navigateToChangeWordBook = {navController.navigate(ChangeWordBook)},
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<ChangeWordBook> {
            ChangeWordBookScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<WordBookDetail> { backStackEntry ->
            val wordDetail = backStackEntry.arguments?.let {
                WordBookDetail(it.getInt("wordBookId"))
            }
            WordBookDetailScreen(
                wordBookId = wordDetail?.wordBookId ?: 0,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable<LearnedWord> {
            LearnedWordScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable<LearnWordFinished> { backStackEntry ->
            val wordFinished = backStackEntry.arguments?.let {
                LearnWordFinished(it.getStringArray("wordList")?.toList() ?: emptyList())
            }
            LearnWordFinishedScreen(
                words = wordFinished?.wordList ?: emptyList(),
                navigationToHome = { navController.navigate(Home) {
                    popUpTo(0) { inclusive = true }
                }},
                navigationToLearn = { navController.navigate(LearnWord){
                    popUpTo(0) { inclusive = true }
                } }
            )
        }
        composable<ReviewWordFinished> { backStackEntry ->
            val wordFinished = backStackEntry.arguments?.let {
                ReviewWordFinished(it.getStringArray("wordList")?.toList() ?: emptyList())
            }
            ReviewWordFinishedScreen(
                words = wordFinished?.wordList ?: emptyList(),
                navigationToHome = {
                    navController.navigate(Home) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigationToReview = {
                    navController.navigate(ReviewWord) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable<LearnWord> {
            LearnWordScreen(
                navigateToLearnWordFinished = { wordList ->
                    navController.navigate(LearnWordFinished(wordList))
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<ReviewWord> {
            ReviewWordScreen(
                navigateToReviewWordFinished = { wordList ->
                    navController.navigate(ReviewWordFinished(wordList))
                },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable<NewWordBook> {
            NewWordBookScreen(
                navigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }






    }
}
