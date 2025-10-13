package com.o7878x.todolistapp.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.o7878x.todolistapp.R
import com.o7878x.todolistapp.ui.theme.LargeDp
import com.o7878x.todolistapp.ui.theme.MediumDp
import com.o7878x.todolistapp.ui.theme.TodoInputBarBackgroundColor
import com.o7878x.todolistapp.ui.theme.TodoInputBarFabColor
import com.o7878x.todolistapp.ui.theme.TodoInputBarFabSize
import com.o7878x.todolistapp.ui.theme.TodoInputBarHeight
import com.o7878x.todolistapp.ui.theme.TodoInputBarTextStyle

@Composable
fun TodoInputBar(
    modifier: Modifier = Modifier,
    onAddButtonClick: (String) -> Unit = {},
) {
    val input = remember {
        mutableStateOf("")
    }

    Card(
        shape = RoundedCornerShape(size = MediumDp),
        modifier = modifier
            .padding(MediumDp)
            .height(TodoInputBarHeight)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = LargeDp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = TodoInputBarBackgroundColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input.value,
                modifier = Modifier.weight(1.0f),
                textStyle = TodoInputBarTextStyle,
                onValueChange = { newText -> input.value = newText },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.todo_input_bar_hint),
                        style = TodoInputBarTextStyle.copy(color = Color.White.copy(alpha = 0.5f))
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    disabledTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            FloatingActionButton(
                containerColor = TodoInputBarFabColor,
                onClick = {
                    if (input.value.isEmpty()) {
                        return@FloatingActionButton
                    }
                    onAddButtonClick(input.value)
                    input.value = ""
                },
                shape = CircleShape,
                modifier = Modifier.size(TodoInputBarFabSize),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    tint = TodoInputBarBackgroundColor
                )
            }
            Spacer(modifier = Modifier.width(LargeDp))
        }
    }
}

@Preview
@Composable
fun TodoInputBarPreview() {
    TodoInputBar()
}