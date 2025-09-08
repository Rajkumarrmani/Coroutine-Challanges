package com.app.learningcoroutine.queue

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.learningcoroutine.R
import com.app.learningcoroutine.ui.theme.Error
import com.app.learningcoroutine.ui.theme.LearningCoroutineTheme
import com.app.learningcoroutine.ui.theme.OverLoaded
import com.app.learningcoroutine.ui.theme.surfaceHeight
import com.app.learningcoroutine.ui.theme.warning
import kotlinx.coroutines.delay

private enum class State {
    STARTED, START, PAUSE
}

@Composable
fun OrderPage(
    modifier: Modifier = Modifier,
    viewModel: QueueViewModel = viewModel(),
) {
    val items by remember(viewModel) {
        mutableStateOf(mutableStateListOf<Int>())
    }
    var currentState by remember { mutableStateOf(State.START) }
    var percentage by remember { mutableIntStateOf(0) }

    fun updateProgress(itemCount: Int) {
        percentage = ((itemCount.toFloat() / 25f) * 100).toInt()
    }

    LaunchedEffect(Unit) {
        viewModel.orderChannel.collect { value ->
            items.add(value)
        }
    }
    LaunchedEffect(currentState, items.size) {
        updateProgress(items.size)
        if (currentState == State.STARTED && items.isNotEmpty()) {
            val randomDelay = (100L..250L).random()
            delay(randomDelay)
            if (items.isNotEmpty()) items.removeAt(0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            shape = RoundedCornerShape(
                10.dp
            ),
            content = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center,
                    text = "Order Queue Outpost",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                AnimatedVisibility(
                    visible = currentState == State.START
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                        text = "Press Play to start processing orders.",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                }

                if (currentState != State.START) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Queue:${items.size}/25",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimary
                        )



                        Text(
                            modifier = Modifier,
                            text = "${percentage}%",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            ),
                            color = if(percentage > 100) MaterialTheme.colorScheme.OverLoaded
                            else MaterialTheme.colorScheme.onSecondary
                        )
                    }

                    SegmentedLinearProgressBar(
                        segment1Progress = when {
                            percentage < 33 -> (percentage.toFloat() / 33f).coerceIn(0f, 1f)
                            else            -> 1f
                        },
                        segment2Progress = when {
                            percentage < 34  -> 0f
                            percentage <= 66 -> ((percentage - 34).toFloat() / 34f).coerceIn(0f, 1f)
                            else             -> 1f
                        },
                        segment3Progress = when {
                            percentage < 67   -> 0f
                            percentage <= 100 -> ((percentage - 67).toFloat() / 33f).coerceIn(
                                0f,
                                1f
                            )

                            else              -> 1f
                        },
                        segment4Progress = when {
                            percentage < 100  -> 0f
                            percentage <= 120 -> ((percentage - 100).toFloat() / 20f).coerceIn(
                                0f,
                                1f
                            )

                            else              -> 1f
                        },
                        activeColor = when (percentage) {
                            in 1..33   -> MaterialTheme.colorScheme.primary
                            in 34..66  -> MaterialTheme.colorScheme.warning
                            in 67..100 -> MaterialTheme.colorScheme.Error
                            else       -> {
                                MaterialTheme.colorScheme.OverLoaded
                            }
                        }.also {
                            Log.d("color", "percentage: $percentage")
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }





                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(
                            align = Alignment.Center
                        )
                        .padding(10.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentState == State.PAUSE) MaterialTheme.colorScheme.surfaceHeight
                        else MaterialTheme.colorScheme.primary,
                        contentColor =
                            if (currentState == State.PAUSE) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.surface
                    ),
                    onClick = {
                        when (currentState) {
                            State.START   -> {
                                viewModel.start()
                                currentState = State.STARTED
                            }

                            State.STARTED -> {
                                currentState = State.PAUSE
                            }

                            State.PAUSE   -> {
                                currentState = State.STARTED
                            }
                        }

                    }
                ) {

                    Icon(
                        imageVector = if (currentState == State.PAUSE) ImageVector.vectorResource(id = R.drawable.play_pause)
                        else ImageVector.vectorResource(id = R.drawable.play_button),
                        contentDescription = "Icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = when (currentState) {
                            State.START, State.STARTED -> "Start"
                            State.PAUSE                -> "Pause"
                        }
                    )
                }
            }
        )
    }


}

@Composable
fun SegmentedLinearProgressBar(
    segment1Progress: Float,
    segment2Progress: Float,
    segment3Progress: Float,
    segment4Progress: Float,
    modifier: Modifier = Modifier,
    activeColor: Color,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceHeight,
    gapWidth: Dp = 2.dp,
) {

    val infiniteTransition = rememberInfiniteTransition(label = "shadow_animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = "scale_shadow"
    )

    val modifier = Modifier
        .fillMaxWidth()
        .dropShadow(
            shape = RoundedCornerShape(20.dp),
            shadow = Shadow(
                radius = 1.dp * scale,
                color = MaterialTheme.colorScheme.Error.copy(0.5f),
                spread = 3.dp * scale,
                alpha = 0.2f
            )
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(gapWidth)
    ) {

        LinearProgressIndicator(
            progress = { segment1Progress },
            modifier = if (segment4Progress > 0.0) modifier.weight(1f)
            else Modifier.weight(1f),
            color = activeColor,
            trackColor = inactiveColor,
            drawStopIndicator = {}
        )

        LinearProgressIndicator(
            progress = { segment2Progress },
            modifier = if (segment4Progress > 0.0) modifier.weight(1f)
            else Modifier.weight(1f),
            color = activeColor,
            trackColor = inactiveColor,
            drawStopIndicator = {}
        )

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            LinearProgressIndicator(
                progress = { segment3Progress },
                modifier = if(segment4Progress > 0.0 )modifier else Modifier,
                color = activeColor,
                trackColor = inactiveColor,
                drawStopIndicator = {}
            )

            AnimatedVisibility(
                visible = segment4Progress > 0.0,
            ) {
                Text(
                    color = MaterialTheme.colorScheme.surfaceHeight,
                    modifier = Modifier.fillMaxWidth(),
                    text = "100%",
                    textAlign = TextAlign.End,
                )
            }
        }

        AnimatedVisibility(
            visible = segment4Progress > 0.0,
            modifier = if (segment4Progress > 0.0) modifier.weight(1f)
            else Modifier.weight(1f),
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            LinearProgressIndicator(
                progress = { segment4Progress },
                modifier = Modifier,
                color = activeColor,
                trackColor = inactiveColor,
                drawStopIndicator = {}
            )
        }
    }


    /*  Row(
          modifier = modifier
              .fillMaxWidth()
              .padding(20.dp),
          horizontalArrangement = Arrangement.spacedBy(gapWidth)
      ) {
          repeat(segments) { index ->
              // Calculate the progress range for the current segment.
              // `segmentStartProgress` is the progress value where this segment begins.
              // `segmentEndProgress` is the progress value where this segment ends.
              val segmentStartProgress = index.toFloat() / segments.toFloat()
              val segmentEndProgress = (index + 1).toFloat() / segments.toFloat()
              // Determine if the current segment should be considered active based on the overall progress.
              val isSegmentActive = progress >= segmentStartProgress
              val isSegmentFullyActive = progress >= segmentEndProgress

              Box(
                  modifier = Modifier
                      .weight(1f)
                      .height(8.dp)
                      .clip(RoundedCornerShape(50))
                      .background(
                          if (segments == MAX_SEGMENTS) {
                              MaterialTheme.colorScheme.OverLoaded.copy(alpha = 0.3f)
                          } else {
                              inactiveColor
                          }
                      )
              ) {
                  if (isSegmentActive) {
                      // If the segment is active, calculate how much of this segment should be filled.
                      // `filledRatio` is a value between 0.0 and 1.0 representing the filled portion of this segment.
                      // `coerceIn(0f, 1f)` ensures the ratio stays within the valid range.
                      val filledRatio = if (isSegmentFullyActive) {
                          1f
                      } else {
                          ((progress - segmentStartProgress) / (segmentEndProgress - segmentStartProgress)).coerceIn(
                                  0f,
                                  1f
                              )
                      }
                      // An inner Box is used to draw the filled portion of the segment.
                      // `Modifier.fillMaxWidth(filledRatio)` sets the width of this inner Box based on the `filledRatio`.
                      Box(
                          modifier = Modifier
                              .fillMaxHeight()
                              .fillMaxWidth(filledRatio)
                              .background(
                                  if (segments == MAX_SEGMENTS) {
                                      MaterialTheme.colorScheme.OverLoaded
                                  } else activeColor)
                              .clip(RoundedCornerShape(50))
                      )
                  }
              }
          }
      }*/
}

@Preview()
@Composable
fun OrderPagePreview() {
    LearningCoroutineTheme {
        OrderPage()
    }
}