package com.miomi.recipe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miomi.recipe.viewmodel.AuthViewModel

private val Green = Color(0xFF3C6939)
private val DarkGreen = Color(0xFF245024)

@Composable
fun LoginScreen(authViewModel: AuthViewModel) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF9ECF96), Color(0xFFBCF0B4))
                )
            )
    ) {
        // Background circles for decoration
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = 110.dp, y = (-90).dp)
                .clip(CircleShape)
                .background(Green.copy(alpha = 0.10f))
                .align(Alignment.TopEnd)
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(x = (-50).dp, y = 80.dp)
                .clip(CircleShape)
                .background(Green.copy(alpha = 0.07f))
                .align(Alignment.TopStart)
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // Top content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 72.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(1.dp)
                            .background(DarkGreen.copy(alpha = 0.35f))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "MIOMI",
                        style = MaterialTheme.typography.labelLarge,
                        color = DarkGreen.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 5.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(1.dp)
                            .background(DarkGreen.copy(alpha = 0.35f))
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // App logo
                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(54.dp),
                        tint = Green
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // App name and tagline
                Text(
                    text = "Cooking Recipe",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = DarkGreen
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Tagline
                Text(
                    text = "Discover, save and cook\nyour favorite recipes",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DarkGreen.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Features row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FeatureItem(icon = Icons.Default.Search, label = "Discover")
                    FeatureItem(icon = Icons.Default.Favorite, label = "Save")
                    FeatureItem(icon = Icons.Default.LocalDining, label = "Cook")
                }
            }

            // Bottom card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color(0xFFF7FBF1))
                    .padding(horizontal = 32.dp, vertical = 40.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Welcome text
                    Text(
                        text = "Get started",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Subtext
                    Text(
                        text = "Sign in to access your recipes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Google Sign-In button
                    Button(
                        onClick = { authViewModel.signIn(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sign in with Google",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Terms of Service text
                    Text(
                        text = "By continuing, you agree to our\nTerms of Service & Privacy Policy",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.55f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = Green
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        // Feature label
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = DarkGreen,
            fontWeight = FontWeight.Medium
        )
    }
}
