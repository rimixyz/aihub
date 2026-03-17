package com.foss.aihub.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.foss.aihub.R

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isRefreshing: Boolean = false,
    serviceName: String,
    accentColor: Color,
    progress: Int = 0,
    enableShimmer: Boolean = true
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.9f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val animatedProgress by animateFloatAsState(
        targetValue = progress / 100f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val shimmerOffset = if (enableShimmer) {
        val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
        infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Restart
            ), label = "shimmerOffset"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    AnimatedVisibility(
        visible = isVisible, enter = fadeIn(animationSpec = tween(300)) + scaleIn(
            initialScale = 0.9f, animationSpec = tween(300, easing = FastOutSlowInEasing)
        ), exit = fadeOut(animationSpec = tween(300)) + scaleOut(
            targetScale = 0.9f, animationSpec = tween(300, easing = FastOutSlowInEasing)
        ), modifier = modifier
    ) {
        Surface(
            color = Color.Transparent, modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f), Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    modifier = Modifier
                        .scale(scale)
                        .width(280.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
                            CircularProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier.fillMaxSize(),
                                color = accentColor,
                                strokeWidth = 4.dp,
                                trackColor = accentColor.copy(alpha = 0.2f),
                                strokeCap = StrokeCap.Round,
                            )
                            if (progress > 0) {
                                Text(
                                    text = "$progress%",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = serviceName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = if (isRefreshing) stringResource(R.string.label_refreshing) else stringResource(
                                    R.string.label_loading,
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (progress >= 0) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                LinearProgressIndicator(
                                    progress = { 1f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(accentColor.copy(alpha = 0.2f)),
                                    color = accentColor.copy(alpha = 0.2f),
                                    trackColor = Color.Transparent,
                                    strokeCap = StrokeCap.Round,
                                )

                                val progressWidth = animatedProgress
                                val gradientBrush = Brush.horizontalGradient(
                                    colors = listOf(accentColor, accentColor.copy(alpha = 0.7f))
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progressWidth)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(gradientBrush)
                                )

                                if (enableShimmer) {
                                    val shimmerBrush = Brush.linearGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.3f),
                                            Color.White.copy(alpha = 0.1f),
                                            Color.White.copy(alpha = 0.3f)
                                        ),
                                        start = Offset(x = shimmerOffset.value, y = 0f),
                                        end = Offset(x = shimmerOffset.value + 200f, y = 0f)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progressWidth)
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(shimmerBrush)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}