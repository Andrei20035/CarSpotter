package com.example.carspotter.ui.screens.profile_customization

import android.app.DatePickerDialog
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.example.carspotter.R
import com.example.carspotter.domain.model.CountryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun PictureContainer(
    modifier: Modifier = Modifier,
    currentStep: ProfileStep,
    picture: ImageSource?,
    text: String,
    onImageSelected: (Uri) -> Unit,
    onBackPress: ((ProfileCustomizationAction) -> Unit)? = null,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let(onImageSelected)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (currentStep == ProfileStep.Car) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable {
                                onBackPress?.invoke(ProfileCustomizationAction.PreviousStep)
                            }
                    )
                }
                Text(
                    text = text,
                    color = Color(0xFFDBB8B8),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                    )
            }
        }

        Box(
            modifier = when (currentStep) {
                ProfileStep.Personal -> Modifier
                    .size(140.dp)
                    .clip(CircleShape)

                ProfileStep.Car -> Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            }
                .background(Color.White.copy(alpha = 0.9f).copy(alpha = 0.9f))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            when (picture) {
                is ImageSource.Local -> {
                    AsyncImage(
                        model = picture.uri,
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(if (currentStep == ProfileStep.Car) RoundedCornerShape(12.dp) else CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                is ImageSource.Remote -> {
                    AsyncImage(
                        model = picture.url,
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(if (currentStep == ProfileStep.Car) RoundedCornerShape(12.dp) else CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
        }
    }
}

@Composable
fun DropdownField(
    selectedItem: String,
    items: List<String>,
    label: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color(0xFFDBB8B8),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp)
        )

        Box {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                interactionSource = interactionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop down",
                        modifier = Modifier.size(24.dp)
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }

    // Toggle dropdown expansion when the TextField is clicked
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press) {
                expanded = !expanded
            }
        }
    }

    if (expanded) {
        Popup(
            onDismissRequest = { expanded = false },
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { expanded = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                    ) {
                        items(items) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onItemSelected(item)
                                        expanded = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Divider(color = Color.Gray.copy(alpha = 0.1f), thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        color = Color(0xFFDBB8B8),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 8.dp)
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = Color.Gray.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .height(55.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.White.copy(alpha = 0.9f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    )
}


@Composable
fun BirthDateField(
    birthDate: LocalDate?,
    onBirthDateChanged: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }


    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onBirthDateChanged(LocalDate.of(year, month + 1, dayOfMonth))
            },
            birthDate?.year ?: 2000,
            birthDate?.monthValue?.minus(1) ?: 0,
            birthDate?.dayOfMonth ?: 1
        )
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    datePickerDialog.show()
                }
            }
        }
    }

    Column(modifier = modifier) {
        Text(
            text = "Birthdate",
            color = Color(0xFFDBB8B8),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp)
        )

        OutlinedTextField(
            value = birthDate?.format(
                DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy",
                    Locale.ENGLISH
                )
            ) ?: "",
            placeholder = {
                Text(
                    text = "01/12/2002",
                    color = Color.Gray.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            },
            onValueChange = {},
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(55.dp),
            readOnly = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.White.copy(alpha = 0.9f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                disabledContainerColor = Color.White.copy(alpha = 0.9f)
            ),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
        )
    }
}

@Composable
fun CountryDropdown(
    selectedCountry: String,
    onCountrySelected: (CountryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var countries by remember { mutableStateOf<List<CountryItem>?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }


    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (countries == null && !isLoading) {
            isLoading = true
            val countryList = withContext(Dispatchers.IO) {
                Locale.getISOCountries().map { code ->
                    val flag = getFlagEmoji(code)
                    val name = Locale("", code).getDisplayCountry(Locale.getDefault())
                    CountryItem(name = name, code = code, flag = flag)
                }.sortedBy { it.name }
            }
            countries = countryList
            isLoading = false
        }
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    Log.d("CountryDropdown", "TextField clicked, expanded: $expanded")
                    expanded = !expanded
                }
            }
        }
    }


    Column(modifier = modifier) {
        Text(
            text = "Country",
            color = Color(0xFFDBB8B8),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp)
        )

        Box {
            val countryCode = getCountryCodeByName(selectedCountry)
            val flag = countryCode?.let { getFlagEmoji(it) } ?: ""

            OutlinedTextField(
                value = "$flag  $selectedCountry",
                onValueChange = {},
                readOnly = true,
                interactionSource = interactionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                ),
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_drop_down_circle_24px),
                        contentDescription = "Drop down",
                        modifier = Modifier.size(24.dp)
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
            )

        }
    }
    if (expanded) {
        Popup(
            onDismissRequest = { expanded = false },
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        expanded = false
                    }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .align(Alignment.Center)
                        .height(400.dp)
                        .zIndex(1000f)
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    countries?.let { countryList ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                        ) {
                            items(countryList) { country ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onCountrySelected(country)
                                            expanded = false
                                        }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = country.flag,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = country.name,
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                HorizontalDivider(
                                    color = Color.Gray.copy(alpha = 0.1f),
                                    thickness = 0.5.dp
                                )
                            }
                        }
                    } ?: run {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun NextStepButton(
    text: String,
    onClick: (ProfileCustomizationAction) -> Unit
) {
    Button(
        onClick = {
            onClick(ProfileCustomizationAction.NextStep)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF0AB25),
            disabledContainerColor = Color(0xFFF0AB25).copy(alpha = 0.7f),
            disabledContentColor = Color.Black.copy(alpha = 0.7f)
        ),
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun getFlagEmoji(countryCode: String): String {
    return countryCode.uppercase().map {
        val base = 0x1F1E6
        val offset = it.code - 'A'.code
        Character.toChars(base + offset)
    }.joinToString("") { it.concatToString() }
}

private fun getCountryCodeByName(name: String): String? {
    return Locale.getISOCountries().firstOrNull { code ->
        Locale("", code).getDisplayCountry(Locale.getDefault()) == name
    }
}
