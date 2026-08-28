package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.MovementPose
import com.example.ui.theme.BrightGreen
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.GoldenAmberDark
import com.example.ui.theme.GoldenStar
import com.example.ui.theme.GoldenStarLight
import com.example.ui.theme.IslamicTeal
import com.example.ui.theme.IslamicTealDark
import com.example.ui.theme.IslamicTealLight
import com.example.ui.theme.SkyBlue
import kotlin.random.Random

@Composable
fun KidHeaderSection(
    userProfile: UserProfileEntity?,
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val xp = userProfile?.xp ?: 0
    val level = userProfile?.level ?: 1
    val streak = userProfile?.currentStreak ?: 0
    val xpInLevel = xp % 200
    val progress = xpInLevel / 200f
    val title = userProfile?.selectedTitle ?: "Santri Cilik"
    val avatarId = userProfile?.avatarId ?: "avatar_boy_1"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onProfileClick() }
            .testTag("kid_header_card"),
        colors = CardDefaults.cardColors(
            containerColor = IslamicTealDark
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Avatar + Name & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarDisplay(
                        avatarId = avatarId,
                        size = 52,
                        modifier = Modifier.border(2.dp, GoldenStar, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = userProfile?.name ?: "Bintang Cilik",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = GoldenStar.copy(alpha = 0.2f),
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = "🎖️ $title",
                                    color = GoldenStar,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Streak Flame Badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CoralOrange.copy(alpha = 0.25f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoralOrange)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak Istiqomah",
                            tint = CoralOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$streak Hari",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // XP and Level Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Level",
                        tint = GoldenStar,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Level $level",
                        color = GoldenStar,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Text(
                    text = "$xpInLevel / 200 XP (Total: $xp XP)",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = GoldenStar,
                trackColor = Color.White.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun AvatarDisplay(
    avatarId: String,
    size: Int = 48,
    modifier: Modifier = Modifier
) {
    val bgColor = when (avatarId) {
        "avatar_boy_1" -> IslamicTeal
        "avatar_boy_2" -> SkyBlue
        "avatar_boy_3" -> CoralOrange
        "avatar_boy_4" -> GoldenAmberDark
        "avatar_girl_1" -> Color(0xFFE91E63)
        "avatar_girl_2" -> Color(0xFF9C27B0)
        else -> IslamicTeal
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        val emoji = when (avatarId) {
            "avatar_boy_1" -> "👦🏻"
            "avatar_boy_2" -> "👳🏻"
            "avatar_boy_3" -> "🧒🏽"
            "avatar_boy_4" -> "🧑🏻"
            "avatar_girl_1" -> "🧕🏻"
            "avatar_girl_2" -> "👧🏽"
            else -> "👦🏻"
        }
        Text(
            text = emoji,
            fontSize = (size * 0.52).sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PrayerPoseIllustration(
    pose: MovementPose,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        IslamicTealDark.copy(alpha = 0.95f),
                        DarkNavy
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Glowing Canvas with Kid Movement Visual
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .scale(pulseScale)
                .padding(16.dp)
        ) {
            val canvasW = size.width
            val canvasH = size.height
            val centerX = canvasW / 2f
            val groundY = canvasH * 0.82f

            // Draw Prayer Rug (Sajadah)
            val rugWidth = canvasW * 0.65f
            val rugHeight = 24.dp.toPx()
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(GoldenAmberDark, GoldenStar, GoldenAmberDark)),
                topLeft = Offset(centerX - rugWidth / 2f, groundY),
                size = Size(rugWidth, rugHeight),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )

            // Draw Crescent & Stars in background
            drawCircle(
                color = GoldenStar.copy(alpha = 0.25f),
                radius = 45.dp.toPx(),
                center = Offset(centerX, canvasH * 0.4f)
            )

            // Draw specific pose cartoon stick/geometric figure
            drawPoseFigure(pose, centerX, groundY, canvasH)
        }
    }
}

private fun DrawScope.drawPoseFigure(
    pose: MovementPose,
    centerX: Float,
    groundY: Float,
    canvasH: Float
) {
    val bodyColor = Color(0xFFE0F2F1)
    val headColor = Color(0xFFFFCC80)
    val peciColor = Color.White
    val pantsColor = Color(0xFF004D40)

    val strokeBody = Stroke(width = 12.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)

    when (pose) {
        MovementPose.BERDIRI_NIAT, MovementPose.TAKBIRATUL_IHRAM, MovementPose.SEDEKAP -> {
            val headY = groundY - 80.dp.toPx()
            // Head
            drawCircle(color = headColor, radius = 16.dp.toPx(), center = Offset(centerX, headY))
            // Peci
            drawRoundRect(
                color = peciColor,
                topLeft = Offset(centerX - 14.dp.toPx(), headY - 20.dp.toPx()),
                size = Size(28.dp.toPx(), 12.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Torso
            drawLine(
                color = bodyColor,
                start = Offset(centerX, headY + 16.dp.toPx()),
                end = Offset(centerX, groundY - 30.dp.toPx()),
                strokeWidth = 14.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            // Legs
            drawLine(
                color = pantsColor,
                start = Offset(centerX - 8.dp.toPx(), groundY - 30.dp.toPx()),
                end = Offset(centerX - 8.dp.toPx(), groundY),
                strokeWidth = 10.dp.toPx()
            )
            drawLine(
                color = pantsColor,
                start = Offset(centerX + 8.dp.toPx(), groundY - 30.dp.toPx()),
                end = Offset(centerX + 8.dp.toPx(), groundY),
                strokeWidth = 10.dp.toPx()
            )

            // Arms based on pose
            if (pose == MovementPose.TAKBIRATUL_IHRAM) {
                // Raised arms
                drawLine(
                    color = bodyColor,
                    start = Offset(centerX, headY + 22.dp.toPx()),
                    end = Offset(centerX - 28.dp.toPx(), headY),
                    strokeWidth = 9.dp.toPx(),
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                drawLine(
                    color = bodyColor,
                    start = Offset(centerX, headY + 22.dp.toPx()),
                    end = Offset(centerX + 28.dp.toPx(), headY),
                    strokeWidth = 9.dp.toPx(),
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            } else {
                // Sedekap on chest
                drawLine(
                    color = bodyColor,
                    start = Offset(centerX - 18.dp.toPx(), headY + 36.dp.toPx()),
                    end = Offset(centerX + 18.dp.toPx(), headY + 36.dp.toPx()),
                    strokeWidth = 10.dp.toPx(),
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }
        }

        MovementPose.RUKU -> {
            val hipX = centerX + 20.dp.toPx()
            val hipY = groundY - 45.dp.toPx()
            val shoulderX = centerX - 35.dp.toPx()
            val shoulderY = hipY
            val headX = shoulderX - 18.dp.toPx()

            // Head & Peci
            drawCircle(color = headColor, radius = 14.dp.toPx(), center = Offset(headX, shoulderY))
            // Flat back (torso)
            drawLine(
                color = bodyColor,
                start = Offset(shoulderX, shoulderY),
                end = Offset(hipX, hipY),
                strokeWidth = 14.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            // Arms to knees
            drawLine(
                color = bodyColor,
                start = Offset(shoulderX, shoulderY),
                end = Offset(centerX - 5.dp.toPx(), groundY - 20.dp.toPx()),
                strokeWidth = 9.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            // Legs
            drawLine(
                color = pantsColor,
                start = Offset(hipX, hipY),
                end = Offset(hipX - 10.dp.toPx(), groundY),
                strokeWidth = 10.dp.toPx()
            )
        }

        MovementPose.ITIDAL -> {
            val headY = groundY - 80.dp.toPx()
            drawCircle(color = headColor, radius = 16.dp.toPx(), center = Offset(centerX, headY))
            drawLine(
                color = bodyColor,
                start = Offset(centerX, headY + 16.dp.toPx()),
                end = Offset(centerX, groundY - 30.dp.toPx()),
                strokeWidth = 14.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            // Arms hanging straight down
            drawLine(
                color = bodyColor,
                start = Offset(centerX - 14.dp.toPx(), headY + 22.dp.toPx()),
                end = Offset(centerX - 16.dp.toPx(), groundY - 35.dp.toPx()),
                strokeWidth = 8.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            drawLine(
                color = bodyColor,
                start = Offset(centerX + 14.dp.toPx(), headY + 22.dp.toPx()),
                end = Offset(centerX + 16.dp.toPx(), groundY - 35.dp.toPx()),
                strokeWidth = 8.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            // Legs
            drawLine(
                color = pantsColor,
                start = Offset(centerX, groundY - 30.dp.toPx()),
                end = Offset(centerX, groundY),
                strokeWidth = 12.dp.toPx()
            )
        }

        MovementPose.SUJUD -> {
            val headX = centerX - 45.dp.toPx()
            val headY = groundY - 8.dp.toPx()
            val hipX = centerX + 15.dp.toPx()
            val hipY = groundY - 35.dp.toPx()

            // Head on ground
            drawCircle(color = headColor, radius = 14.dp.toPx(), center = Offset(headX, headY))
            // Curved body up to hips
            val path = Path().apply {
                moveTo(headX + 12.dp.toPx(), headY)
                quadraticTo(centerX - 10.dp.toPx(), hipY - 10.dp.toPx(), hipX, hipY)
            }
            drawPath(path, color = bodyColor, style = strokeBody)

            // Hands on mat
            drawCircle(color = headColor, radius = 6.dp.toPx(), center = Offset(headX + 18.dp.toPx(), groundY - 4.dp.toPx()))
            // Folded Legs
            drawLine(
                color = pantsColor,
                start = Offset(hipX, hipY),
                end = Offset(centerX + 40.dp.toPx(), groundY),
                strokeWidth = 10.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }

        MovementPose.DUDUK_ANTARA_DUA_SUJUD, MovementPose.TASYAHHUD_AWAL, MovementPose.TASYAHHUD_AKHIR -> {
            val headY = groundY - 50.dp.toPx()
            // Head
            drawCircle(color = headColor, radius = 14.dp.toPx(), center = Offset(centerX, headY))
            // Torso
            drawLine(
                color = bodyColor,
                start = Offset(centerX, headY + 14.dp.toPx()),
                end = Offset(centerX, groundY - 15.dp.toPx()),
                strokeWidth = 12.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            // Sitting base
            drawLine(
                color = pantsColor,
                start = Offset(centerX - 25.dp.toPx(), groundY),
                end = Offset(centerX + 25.dp.toPx(), groundY),
                strokeWidth = 12.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            // Hands on knees
            drawLine(
                color = bodyColor,
                start = Offset(centerX, headY + 20.dp.toPx()),
                end = Offset(centerX - 14.dp.toPx(), groundY - 10.dp.toPx()),
                strokeWidth = 7.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            drawLine(
                color = bodyColor,
                start = Offset(centerX, headY + 20.dp.toPx()),
                end = Offset(centerX + 14.dp.toPx(), groundY - 10.dp.toPx()),
                strokeWidth = 7.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }

        MovementPose.SALAM -> {
            val headY = groundY - 50.dp.toPx()
            drawCircle(color = headColor, radius = 14.dp.toPx(), center = Offset(centerX + 8.dp.toPx(), headY))
            drawLine(
                color = bodyColor,
                start = Offset(centerX, headY + 14.dp.toPx()),
                end = Offset(centerX, groundY - 15.dp.toPx()),
                strokeWidth = 12.dp.toPx()
            )
            drawLine(
                color = pantsColor,
                start = Offset(centerX - 25.dp.toPx(), groundY),
                end = Offset(centerX + 25.dp.toPx(), groundY),
                strokeWidth = 12.dp.toPx()
            )
        }
    }
}

@Composable
fun CelebrationDialog(
    prayerName: String,
    pointsEarned: Int,
    isLevelUp: Boolean,
    newLevel: Int,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("celebration_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterVertically
            ) {
                // Glowing badge animation icon
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(GoldenStar, GoldenAmberDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = "Celebration",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MasyaAllah! Hebat! 🎉",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = IslamicTealDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Kamu telah menyelesaikan Sholat $prayerName!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkNavy,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Points Chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GoldenStarLight,
                    border = androidx.compose.foundation.BorderStroke(2.dp, GoldenStar)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Points",
                            tint = GoldenAmberDark,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+$pointsEarned XP Bintang!",
                            color = GoldenAmberDark,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    }
                }

                if (isLevelUp) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BrightGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "🚀 LEVEL UP! Kamu sekarang Level $newLevel!",
                            color = BrightGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicTeal),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("celebration_ok_button")
                ) {
                    Text(
                        text = "Alhamdulillah, Lanjut!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
