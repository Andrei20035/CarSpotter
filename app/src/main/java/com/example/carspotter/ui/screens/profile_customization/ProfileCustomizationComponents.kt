package com.example.carspotter.ui.screens.profile_customization

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FullNameField(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Full name",
        color = Color.Gray,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 8.dp)
    )

    OutlinedTextField(
        value = fullName,
        onValueChange = onFullNameChange,
        placeholder = {
            Text(
                text = "Josh Michael",
                color = Color.Gray.copy(alpha = 0.6f)
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            unfocusedContainerColor = Color.Gray.copy(alpha = 0.2f)
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp
        )
    )
}

@Composable
fun UsernameField(
    username: String,
    onUsernameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Username",
        color = Color.Gray,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 8.dp)
    )

    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        placeholder = {
            Text(
                text = "Josh Michael",
                color = Color.Gray.copy(alpha = 0.6f)
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.Gray.copy(alpha = 0.2f),
            unfocusedContainerColor = Color.Gray.copy(alpha = 0.2f)
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp
        )
    )
}

@Composable
fun PictureContainer(
    picture: ImageSource?,
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.Gray,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 8.dp)
    )

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.9f))
            .clickable {  }, // TODO: Handle image selection
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add image",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add an image",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}