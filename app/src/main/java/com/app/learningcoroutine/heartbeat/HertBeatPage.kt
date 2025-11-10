package com.app.learningcoroutine.heartbeat

import android.R.attr.mode
import android.R.attr.onClick
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeartBeatScreen(
    modifier: Modifier = Modifier,
    heartBeatViweModel: HeartBeatViewModel = HeartBeatViewModel(),
) {
    val data = heartBeatViweModel.uiState.collectAsState().value

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(data) { station ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ){
                CardView(station)
            }
        }
    }

    /*   Button(
           modifier = modifier,
           onClick = {
               heartBeatViweModel.startHeatBeat()
           }
       ) {

       }*/
}

@Composable
fun CardView(
    station: Stations,
) {

        val circleColor = if (station.mode == Mode.ONLINE) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.error

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Canvas(
                modifier = Modifier.size(100.dp),
                onDraw = {
                    drawCircle(
                        color = circleColor,
                        radius = 50f
                    )
                }
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = station.name.toString(),
            )
        }
    }

@Preview(
    showBackground = true
)
@Composable
fun HeartBeatScreenPreview() {
    HeartBeatScreen()
}