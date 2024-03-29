package com.pubnub.components.example.telehealth.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.pubnub.components.example.telehealth.ui.theme.*
import com.pubnub.components.example.telehealth.viewmodel.LoginViewModel
import com.pubnub.components.example.telehealth_example.R
import kotlinx.coroutines.launch

object Login {
    @Composable
    fun View(
        onLoginSuccess: (userId: String, userType: String) -> Unit = { _: String, _: String -> },
    ) {
        val loginViewModel: LoginViewModel = LoginViewModel.default()
        var visible = remember { mutableStateOf(false) }
        var login by rememberSaveable { mutableStateOf("") }
        val pass by rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .size(140.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.logo)
                )
                Text(
                    text = stringResource(R.string.login),
                    style = Typography.h2,
                    modifier = Modifier.padding(start = 24.dp, bottom = 24.dp)
                )
                Text(
                    text = stringResource(R.string.username),
                    style = Typography.h3,
                    modifier = Modifier.padding(start = 24.dp)
                )
                OutlinedTextField(modifier = Modifier
                    .padding(start = 24.dp, bottom = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                    value = login,
                    onValueChange = {
                        login = it
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_login),
                            contentDescription = "logo"
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FocusedBorderColor,
                        unfocusedBorderColor = UnfocusedBorderColor,
                        backgroundColor = Color.White
                    ),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        loginViewModel.viewModelScope.launch {
                            tryToLogin(loginViewModel, visible, login, onLoginSuccess)
                        }
                    })
                )
                Text(
                    text = stringResource(R.string.password),
                    style = Typography.h3,
                    modifier = Modifier.padding(start = 24.dp)
                )
                OutlinedTextField(modifier = Modifier
                    .padding(start = 24.dp, bottom = 18.dp, end = 24.dp)
                    .fillMaxWidth(),
                    value = pass,
                    onValueChange = {},
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FocusedBorderColor,
                        unfocusedBorderColor = UnfocusedBorderColor,
                        backgroundColor = Color.White
                    ),
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_password),
                            contentDescription = "logo"
                        )
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        loginViewModel.viewModelScope.launch {
                            tryToLogin(loginViewModel, visible, login, onLoginSuccess)
                        }
                    })
                )
                Column(
                    modifier = Modifier
                        .size(width = 420.dp, height = 48.dp)
                        .padding(start = 24.dp, end = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier.fillMaxSize(),
                        onClick = {
                            loginViewModel.viewModelScope.launch {
                                tryToLogin(loginViewModel, visible, login, onLoginSuccess)
                            }
                        },
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(backgroundColor = HyperLinkColor)
                    ) {
                        Text(
                            text = stringResource(R.string.login),
                            style = Typography.body2,
                        )
                    }
                }
                AnimatedVisibility(
                    visible = visible.value,
                    enter = fadeIn(initialAlpha = 0.4f),
                    exit = fadeOut(animationSpec = tween(durationMillis = 250))
                ) {
                    Row(modifier = Modifier.padding(top = 12.dp, start = 24.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_icon),
                            contentDescription = stringResource(id = R.string.error_icon),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = stringResource(R.string.password_error),
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.hsl(2F, 0.72F, 0.53F, 1F)
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(LoginInfoBoxBackgroundColor)
            ) {
                HyperlinkText(
                    modifier = Modifier.padding(
                        top = 40.dp,
                        start = 72.dp,
                        end = 72.dp,
                        bottom = 30.dp
                    ),
                    fullText = stringResource(R.string.password_info),
                    hyperlink = Hyperlink(
                        "Demo page",
                        "https://github.com/pubnub/chat-components-android-examples/blob/telehealth-example/telehealth-example/README.md"
                    )
                )
            }
        }
    }

    suspend fun tryToLogin(
        loginViewModel: LoginViewModel,
        visible: MutableState<Boolean>,
        username: String,
        onLoginSuccess: (userId: String, userType: String) -> Unit,
    ) {
        loginViewModel.login(username).onSuccess {
            onLoginSuccess(it.id, it.type)
            visible.value = false
        }.onFailure {
            visible.value = true
        }
    }
}

