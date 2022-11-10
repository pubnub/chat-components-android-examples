package com.pubnub.components.example.telehealth.ui.view

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewModelScope
import com.pubnub.components.example.getting_started.R
import com.pubnub.components.example.telehealth.ChannelActivity
import com.pubnub.components.example.telehealth.dto.ChatParameters
import com.pubnub.components.example.telehealth.dto.ChatParameters.Companion.PARAMETERS_BUNDLE_KEY
import com.pubnub.components.example.telehealth.ui.theme.*
import com.pubnub.components.example.telehealth.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

object Login {
    @Composable
    fun View(
    ) {
        val loginViewModel: LoginViewModel = LoginViewModel.default()
        var visible = remember { mutableStateOf(false) }
        var login by rememberSaveable { mutableStateOf("") }
        val pass by rememberSaveable { mutableStateOf("") }
        Box(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
            ) {
                val context = LocalContext.current
                Image(modifier = Modifier
                    .padding(top = 50.dp)
                    .size(140.dp)
                    .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.logo))
                Text(text = stringResource(R.string.login),
                    style = Typography.h2,
                    modifier = Modifier.padding(start = 24.dp, bottom = 24.dp))
                Text(text = stringResource(R.string.username),
                    style = Typography.h3,
                    modifier = Modifier.padding(start = 24.dp))
                OutlinedTextField(modifier = Modifier
                    .padding(start = 24.dp, bottom = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                    value = login,
                    onValueChange = {
                        login = it
                    },
                    leadingIcon = {
                        Image(painter = painterResource(id = R.drawable.ic_login),
                            contentDescription = "logo")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.hsl(
                        2F,
                        0.72F,
                        0.53F,
                        1F),
                        unfocusedBorderColor = Color.hsl(0F, 0F, 0.80F, 1F),
                        backgroundColor = Color.White),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        loginViewModel.viewModelScope.launch {
                            tryToLogin(loginViewModel, visible, login, context)
                        }
                    }))
                Text(text = stringResource(R.string.password),
                    style = Typography.h3,
                    modifier = Modifier.padding(start = 24.dp))
                OutlinedTextField(modifier = Modifier
                    .padding(start = 24.dp, bottom = 18.dp, end = 24.dp)
                    .fillMaxWidth(),
                    value = pass,
                    onValueChange = {},
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = FocusedBorderColor,
                        unfocusedBorderColor = UnfocusedBorderColor,
                        backgroundColor = Color.White),
                    leadingIcon = {
                        Image(painter = painterResource(id = R.drawable.ic_password),
                            contentDescription = "logo")
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        loginViewModel.viewModelScope.launch {
                            tryToLogin(loginViewModel, visible, login, context)
                        }
                    }))
                Column(modifier = Modifier
                    .size(width = 420.dp, height = 48.dp)
                    .padding(start = 24.dp, end = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(modifier = Modifier.fillMaxSize(),
                        onClick = {
                            loginViewModel.viewModelScope.launch {
                                tryToLogin(loginViewModel, visible, login, context)
                            }
                        },
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(backgroundColor = HyperLinkColor)) {
                        Text(
                            text = stringResource(R.string.login),
                            style = Typography.body2,
                        )
                    }
                }
                AnimatedVisibility(visible = visible.value,
                    enter = fadeIn(initialAlpha = 0.4f),
                    exit = fadeOut(animationSpec = tween(durationMillis = 250))) {
                    Row(modifier = Modifier.padding(top = 12.dp, start = 24.dp)) {
                        Image(painter = painterResource(id = R.drawable.ic_icon),
                            contentDescription = stringResource(id = R.string.error_icon),
                            modifier = Modifier.padding(top = 4.dp))
                        Text(text = stringResource(R.string.password_error),
                            style = TextStyle(fontSize = 16.sp,
                                color = Color.hsl(2F, 0.72F, 0.53F, 1F)),
                            modifier = Modifier.padding(start = 8.dp))
                    }
                }

            }
            Box(modifier = Modifier
                .background(LoginInfoBoxBackgroundColor)
                .align(Alignment.BottomCenter)) {
                HyperlinkText(modifier = Modifier.padding(top = 40.dp,
                    start = 72.dp,
                    end = 72.dp,
                    bottom = 30.dp),
                    fullText = stringResource(R.string.password_info),
                    hyperlink = Hyperlink("Demo page",
                        "https://github.com/pubnub/chat-components-android-examples/blob/telehealth-example/telehealth-example/README.md"))
            }
        }
    }

    suspend fun tryToLogin(
        loginViewModel: LoginViewModel,
        visible: MutableState<Boolean>,
        login: String,
        context: Context,
    ) {
        loginViewModel.login(login).onSuccess {
            openChannelActivity(context,
                ChatParameters(userId = it.id,
                    type = it.type,
                    channelId = "",
                    secondUserName = "",
                    secondUserId = ""))
            visible.value = false
        }.onFailure {
            visible.value = true
        }
    }

    private fun openChannelActivity(packageContext: Context, chatParameters: ChatParameters) {
        val intent = Intent(packageContext, ChannelActivity::class.java).apply {
            putExtra(PARAMETERS_BUNDLE_KEY, chatParameters)
        }
        startActivity(packageContext, intent, null)
    }
}