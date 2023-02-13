package com.example.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmicalculator.ui.theme.BMICalculatorTheme
import com.example.bmicalculator.utils.bmiCalculate
import com.example.bmicalculator.utils.getString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BMICalculator()
                }
            }
        }
    }
}

@Composable
fun BMICalculator() {

    var weightState by rememberSaveable {
        mutableStateOf("")
    }

    var heightState by rememberSaveable {
        mutableStateOf("")
    }

    var expandState by remember {
        mutableStateOf(false)
    }

    var bmiResultState by remember {
        mutableStateOf(0.0)
    }

    var errorWeightState by remember {
        mutableStateOf(false)
    }

    var errorHeightState by remember {
        mutableStateOf(false)
    }

    // Objeto que controla a requisição de foco (RequestFocus)
    val weightFocusRequester = FocusRequester()

    Column( // Container
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Column( // Header
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bmi_icon),
                contentDescription = "BMI Icon",
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = stringResource(id = R.string.app_title),
                color = MaterialTheme.colors.primary,
                fontSize = 36.sp,
                letterSpacing = 4.sp
            )
        }

        Column( // Form
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 36.dp)
        ) {
            Text(
                text = stringResource(id = R.string.weight),
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 16.sp
            )
            OutlinedTextField(
                value = weightState,
                onValueChange = { newWeight ->
                    val lastChar =
                         if (newWeight.isEmpty()) {
                             errorWeightState = true
                             newWeight
                         }
                         else {
                             newWeight.get(newWeight.length - 1)
                             errorWeightState = false
                         }
                    val newValue =
                        if (lastChar == '.' || lastChar == ',') newWeight.dropLast(1)
                        else newWeight
                    weightState = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(weightFocusRequester),
                trailingIcon = {if (errorWeightState) Icon(imageVector = Icons.Default.Error, contentDescription = "")},
                isError = errorWeightState,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            AnimatedVisibility(
                visible = errorWeightState
            ) {
                Text(
                    text = stringResource(id = R.string.weight_help),
                    color = Color.Red,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.height),
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 16.sp
            )
            OutlinedTextField(
                value = heightState,
                onValueChange = { newHeight ->
                    val lastChar =
                        if (newHeight.isEmpty()) {
                            errorHeightState = true
                            newHeight
                        }
                        else {
                            newHeight.get(newHeight.length - 1)
                            errorHeightState = false
                        }
                    val newValue =
                        if (lastChar == '.' || lastChar == ',') newHeight.dropLast(1)
                        else newHeight
                    heightState = newValue
                },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {if (errorHeightState) Icon(imageVector = Icons.Default.Error, contentDescription = "")},
                isError = errorHeightState,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp)
            )
            AnimatedVisibility(
                visible = errorHeightState
            ) {
                Text(
                    text = stringResource(id = R.string.height_help),
                    color = Color.Red,
                    fontSize = 10.sp
                )
            }


            Button(
                onClick = {
                    errorWeightState = weightState.isEmpty()
                    errorHeightState = heightState.isEmpty()

                    if (!errorWeightState && !errorHeightState) {
                        bmiResultState = bmiCalculate(weightState.toInt(), heightState.toDouble())
                        expandState = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            ) {
                Text(
                    text = stringResource(id = R.string.button_calculate),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        AnimatedVisibility(
            visible = expandState
//            enter = slideIn(tween(400, easing = FastOutSlowInEasing)) {
//                IntOffset(1000, 100)
//            },
//            exit = slideOut(tween(200, easing = FastOutSlowInEasing)) {
//                IntOffset(-1000, 100)
//            }
        ) {
            Card( // Footer
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.your_score),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = String.format("%.2f", bmiResultState),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = getString(bmiResultState),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Row {

                        Button(
                            onClick = {
                                expandState = false
                                weightState = ""
                                heightState = ""
                                weightFocusRequester.requestFocus()
                            },
                            colors = ButtonDefaults.buttonColors(Color(153, 111, 221, 255))
                        ) {
                            Text(
                                text = stringResource(id = R.string.reset_button),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(Color(153, 111, 221, 255))
                        ) {
                            Text(
                                text = stringResource(id = R.string.share_button),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun BMICalculatorPreview() {
    BMICalculator()
}
