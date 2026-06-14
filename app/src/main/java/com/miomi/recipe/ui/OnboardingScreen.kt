package com.miomi.recipe.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private val Green = Color(0xFF3C6939)
private val DarkGreen = Color(0xFF245024)
private val LightGreen = Color(0xFF9ECF96)

private data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val description: String
)

private val pages = listOf(
    OnboardingPage(
        icon = Icons.Default.Restaurant,
        title = "Welcome to\nCooking Recipe",
        description = "Your personal cookbook. Browse, organize, and manage all your recipes in one place."
    ),
    OnboardingPage(
        icon = Icons.Default.Search,
        title = "Discover Online\nRecipes",
        description = "Search thousands of recipes by name, category, or ingredient and import any of them to your collection."
    ),
    OnboardingPage(
        icon = Icons.Default.Favorite,
        title = "Save Your\nFavorites",
        description = "Tap the heart on any recipe to add it to your Favorites list for quick access whenever you need it."
    )
)

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF9ECF96), Color(0xFFBCF0B4))
                )
            )
    ) {
        // Skip button — top right
        TextButton(
            onClick = onComplete,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 52.dp, end = 16.dp)
        ) {
            Text(
                text = "Skip",
                color = DarkGreen.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                OnboardingPageContent(page = pages[pageIndex])
            }

            // Dot indicators
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    val width by animateDpAsState(
                        targetValue = if (isSelected) 24.dp else 8.dp,
                        animationSpec = tween(300),
                        label = "dot_width"
                    )
                    val color by animateColorAsState(
                        targetValue = if (isSelected) DarkGreen else DarkGreen.copy(alpha = 0.3f),
                        animationSpec = tween(300),
                        label = "dot_color"
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            // Bottom card with action button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color(0xFFF7FBF1))
                    .padding(horizontal = 32.dp, vertical = 32.dp)
            ) {
                val isLastPage = pagerState.currentPage == pages.size - 1

                Button(
                    onClick = {
                        if (isLastPage) {
                            onComplete()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green
                    )
                ) {
                    Text(
                        text = if (isLastPage) "Get Started" else "Next",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Green
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = DarkGreen.copy(alpha = 0.75f),
            textAlign = TextAlign.Center,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
    }
}
