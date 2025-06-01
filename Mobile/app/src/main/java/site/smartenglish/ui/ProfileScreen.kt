package site.smartenglish.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import site.smartenglish.R
import site.smartenglish.ui.compose.CenterAlignedBackArrowTopAppBar
import site.smartenglish.ui.theme.Grey
import site.smartenglish.ui.theme.White
import site.smartenglish.ui.viewmodel.UserViewmodel


@Composable
fun ProfileScreen(
    navigateBack: () -> Unit = {}, viewmodel: UserViewmodel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") {
            ProfileContent(
                navigateModifyName = {
                    navController.navigate("modify_name")
                },
                navigateModifyDescription = {
                    navController.navigate("modify_description")
                },
                navigateFeedback = {
                    navController.navigate("feedback")
                },
                navigateBack = navigateBack
            )
        }
        composable("modify_name") {
            ModifyNameScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                viewmodel = viewmodel
            )
        }
        composable("modify_description") {
            ModifyDescriptionScreen(
                navigateBack = {
                    navController.navigateUp()
                }, viewmodel = viewmodel
            )
        }
        composable("feedback") {
            UserFeedbackScreen(
                navigateBack = {
                    navController.navigateUp()
                }, viewmodel = viewmodel
            )
        }
    }
}

@Composable
fun ProfileContent(
    navigateModifyName: () -> Unit = {},
    navigateModifyDescription: () -> Unit = {},
    navigateFeedback: () -> Unit = {},
    navigateBack: () -> Unit = {}, viewmodel: UserViewmodel = hiltViewModel()
) {
    val userProfile = viewmodel.userProfile.collectAsState().value

    val avatarUrl = userProfile?.avatar
    val username = userProfile?.name ?: ""
    val description = userProfile?.description ?: "暂无描述"

    // 图片选择器
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            // 上传图片并更新头像
            viewmodel.changeAvatar(it.toString())
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedBackArrowTopAppBar(
                "个人信息",
                onBackClick = navigateBack,
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(10.dp))
                // 头像区域
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(147.dp)
                        .background(Grey),
                    contentAlignment = Alignment.Center,

                    ) {
                    IconButton(
                        modifier = Modifier
                            .size(96.dp)
                            .background(
                                Color.White.copy(alpha = 0.1f), shape = CircleShape
                            ),
                        onClick = { }) {
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = "Profile",
                            error = painterResource(R.drawable.outline_person_24),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(87.dp)
                                .clip(CircleShape)
                                .background(White)
                        )
                    }
                    // 更换头像按钮
                    IconButton(
                        onClick = {
                            imagePicker.launch("image/*")
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (-10).dp)
                            .background(Grey, CircleShape)
                            .size(33.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "更换头像",
                            tint = White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                // 昵称区域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .background(Grey)
                        .clickable {
                            navigateModifyName()
                        }
                        .padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "昵称",
                        color = White,
                        fontSize = 17.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = username,
                            color = Color(0xFF878278),
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "编辑昵称",
                            tint = White, modifier = Modifier
                                .size(12.dp)
                                .offset(y = 1.dp)
                        )
                    }
                }
                // 描述区域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .background(Grey)
                        .clickable {
                            navigateModifyDescription()
                        }
                        .padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "简介",
                        color = White,
                        fontSize = 17.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = description,
                            color = Color(0xFF878278),
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "编辑昵称",
                            tint = White,
                            modifier = Modifier
                                .size(12.dp)
                                .offset(y = 1.dp)
                        )
                    }
                }
                // 反馈区域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .background(Grey)
                        .clickable {
                            navigateFeedback()
                        }
                        .padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "用户反馈",
                        color = White,
                        fontSize = 17.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "编辑昵称",
                            tint = White, modifier = Modifier
                                .size(12.dp)
                                .offset(y = 1.dp)
                        )
                    }
                }
            }
        }
    )
}