package com.app.learningcoroutine.thermometer

import android.R.attr.end
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.learningcoroutine.R
import com.app.learningcoroutine.ui.theme.textDisabled


@Preview(
    showBackground = true
)
@Composable
fun TemperatureMonitorScreenPreview() {
    TemperatureMonitorScreen()
}

@Composable
fun TemperatureMonitorScreen(
    modifier: Modifier = Modifier,
    viewModel: TemperatureMonitorViewModel = viewModel(),
) {

    val firstList = viewModel.items.collectAsState().value
    var isCountReached by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(firstList.totalCount) {
        isCountReached = firstList.totalCount == 20
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Text(
                text = "Temperature Treck",
                modifier = modifier
                    .wrapContentSize()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = 0.5.sp
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_check),
                    tint = if(isCountReached) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.textDisabled,
                    modifier = modifier
                        .wrapContentSize()
                        .align(alignment = Alignment.CenterVertically),
                    contentDescription = null
                )
                Spacer(
                    modifier = modifier.width(5.dp)
                )
                Text(
                    text = "${firstList.totalCount} / 20",
                    modifier = modifier
                        .wrapContentSize(),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }


            ColumnTemperature(firstList.data)

            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(isCountReached) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface
                ),
                onClick = {
                    if (isCountReached) viewModel.clear()
                    else viewModel.observeData()
                }
            ) {
                Text(
                    text = if (isCountReached) "Restart"
                    else "Tracking...",
                    color = if(isCountReached) MaterialTheme.colorScheme.surfaceContainerHigh
                    else  MaterialTheme.colorScheme.textDisabled
                )
            }

        }
    }


}

@Composable
fun ColumnTemperature(tempData: List<Temperature>) {

    val first10 = tempData.take(10)
    val second10 = tempData.drop(10).take(10)

    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(start = 10.dp, end = 10.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .weight(1f)
        ) {
            items(first10) {
                RowTemperature(temperature = it)
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(second10) {
                RowTemperature(temperature = it)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowTemperature(
    modifier: Modifier = Modifier,
    temperature: Temperature,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_thermometer),
            tint = if (temperature.isEnabled) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.textDisabled,
            modifier = modifier
                .wrapContentSize()
                .align(alignment = Alignment.CenterVertically),
            contentDescription = null
        )
        Spacer(modifier = modifier.width(5.dp))
        Text(
            text = temperature.tempValue,
            modifier = modifier
                .wrapContentSize()
                .align(alignment = Alignment.CenterVertically),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}