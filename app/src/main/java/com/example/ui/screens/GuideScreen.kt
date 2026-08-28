package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PrayerStep
import com.example.data.model.PrayerType
import com.example.data.model.WudhuStep
import com.example.data.provider.PrayerContentProvider
import com.example.ui.components.PrayerPoseIllustration
import com.example.ui.theme.BrightGreen
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.GoldenAmberDark
import com.example.ui.theme.GoldenStar
import com.example.ui.theme.GoldenStarLight
import com.example.ui.theme.IslamicTeal
import com.example.ui.theme.IslamicTealContainer
import com.example.ui.theme.IslamicTealDark
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SoftBgLight
import com.example.ui.viewmodel.GuideMode

@Composable
fun GuideScreen(
    guideMode: GuideMode,
    selectedPrayer: PrayerType,
    currentStepIndex: Int,
    currentQuizIndex: Int,
    selectedQuizAnswer: Int?,
    quizFeedback: String?,
    onSelectMode: (GuideMode) -> Unit,
    onSelectPrayer: (PrayerType) -> Unit,
    onStepSelected: (Int) -> Unit,
    onNextStep: (Int) -> Unit,
    onPrevStep: () -> Unit,
    onPlayStepAudio: (PrayerStep) -> Unit,
    onPlayWudhuAudio: (WudhuStep) -> Unit,
    onAnswerQuiz: (Int) -> Unit,
    onNextQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prayerSteps = remember(selectedPrayer) {
        PrayerContentProvider.getStepsForPrayer(selectedPrayer)
    }
    val wudhuSteps = remember {
        PrayerContentProvider.getWudhuSteps()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftBgLight)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Mode Selector Tabs (Sholat, Wudhu, Kuis Ceria)
            GuideModeSelector(
                currentMode = guideMode,
                onModeSelected = onSelectMode
            )
        }

        when (guideMode) {
            GuideMode.SHOLAT -> {
                item {
                    // Prayer Selector Chips
                    PrayerSelectionRow(
                        selectedPrayer = selectedPrayer,
                        onPrayerSelected = onSelectPrayer
                    )
                }

                item {
                    // Step Progress Indicator
                    StepProgressIndicator(
                        totalSteps = prayerSteps.size,
                        currentStep = currentStepIndex,
                        onStepClick = onStepSelected
                    )
                }

                item {
                    // Current Prayer Step Card
                    val step = prayerSteps.getOrNull(currentStepIndex) ?: prayerSteps.first()
                    PrayerStepDetailCard(
                        step = step,
                        totalSteps = prayerSteps.size,
                        onNext = { onNextStep(prayerSteps.size) },
                        onPrev = onPrevStep,
                        onPlayAudio = { onPlayStepAudio(step) }
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            GuideMode.WUDHU -> {
                item {
                    // Wudhu Step Progress Indicator
                    StepProgressIndicator(
                        totalSteps = wudhuSteps.size,
                        currentStep = currentStepIndex,
                        onStepClick = onStepSelected
                    )
                }

                item {
                    val wudhuStep = wudhuSteps.getOrNull(currentStepIndex) ?: wudhuSteps.first()
                    WudhuStepDetailCard(
                        step = wudhuStep,
                        totalSteps = wudhuSteps.size,
                        onNext = { onNextStep(wudhuSteps.size) },
                        onPrev = onPrevStep,
                        onPlayAudio = { onPlayWudhuAudio(wudhuStep) }
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            GuideMode.KUIS -> {
                item {
                    QuizCard(
                        currentQuizIndex = currentQuizIndex,
                        selectedAnswer = selectedQuizAnswer,
                        feedback = quizFeedback,
                        onAnswer = onAnswerQuiz,
                        onNext = onNextQuiz
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
    }
}

@Composable
fun GuideModeSelector(
    currentMode: GuideMode,
    onModeSelected: (GuideMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val modes = listOf(
            GuideMode.SHOLAT to "📖 Panduan Sholat",
            GuideMode.WUDHU to "💧 Wudhu",
            GuideMode.KUIS to "🎯 Kuis Poin"
        )

        modes.forEach { (mode, label) ->
            val isSelected = currentMode == mode
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) IslamicTeal else Color.Transparent)
                    .clickable { onModeSelected(mode) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else DarkNavy
                )
            }
        }
    }
}

@Composable
fun PrayerSelectionRow(
    selectedPrayer: PrayerType,
    onPrayerSelected: (PrayerType) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(PrayerType.entries) { prayer ->
            val isSelected = prayer == selectedPrayer
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) IslamicTealDark else Color.White,
                border = BorderStroke(1.dp, if (isSelected) GoldenStar else Color.LightGray.copy(alpha = 0.5f)),
                modifier = Modifier.clickable { onPrayerSelected(prayer) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (prayer) {
                            PrayerType.SUBUH -> "🌅"
                            PrayerType.DZUHUR -> "☀️"
                            PrayerType.ASHAR -> "🌤️"
                            PrayerType.MAGHRIB -> "🌇"
                            PrayerType.ISYA -> "🌙"
                            PrayerType.DHUHA -> "✨"
                        },
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = prayer.displayName,
                        color = if (isSelected) Color.White else DarkNavy,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StepProgressIndicator(
    totalSteps: Int,
    currentStep: Int,
    onStepClick: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Langkah ${currentStep + 1} dari $totalSteps",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GoldenStarLight
                ) {
                    Text(
                        text = "⭐ +10 XP tiap langkah",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldenAmberDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(totalSteps) { idx ->
                    val isDone = idx < currentStep
                    val isCurrent = idx == currentStep

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCurrent -> GoldenStar
                                    isDone -> BrightGreen
                                    else -> Color.LightGray.copy(alpha = 0.4f)
                                }
                            )
                            .clickable { onStepClick(idx) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${idx + 1}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent || isDone) Color.White else Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerStepDetailCard(
    step: PrayerStep,
    totalSteps: Int,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onPlayAudio: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("prayer_step_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // Step Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = IslamicTealContainer
                ) {
                    Text(
                        text = "Langkah #${step.stepNumber}",
                        color = IslamicTealDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Audio Recitation Speaker Button
                Button(
                    onClick = onPlayAudio,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldenStar),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("play_audio_step_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Dengarkan Bacaan",
                        tint = DarkNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Dengar Bacaan",
                        color = DarkNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = step.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkNavy
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Visual Movement Pose Canvas
            PrayerPoseIllustration(pose = step.pose)

            Spacer(modifier = Modifier.height(16.dp))

            // Arabic Reading Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAF8)),
                border = BorderStroke(1.dp, IslamicTealLight.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = step.arabicText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicTealDark,
                        textAlign = TextAlign.End,
                        lineHeight = 36.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = step.latinText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkNavy,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Artinya: \"${step.translation}\"",
                        fontSize = 12.sp,
                        color = Color(0xFF455A64),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Kid-Friendly Tip Box
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = GoldenStarLight,
                border = BorderStroke(1.dp, GoldenStar.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Tips Anak",
                        tint = GoldenAmberDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Tips Santri Cilik:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = GoldenAmberDark
                        )
                        Text(
                            text = step.kidTip,
                            fontSize = 12.sp,
                            color = DarkNavy,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onPrev,
                    enabled = step.stepNumber > 1,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Kembali", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("next_step_button")
                ) {
                    Text(
                        text = if (step.stepNumber >= totalSteps) "Selesai Panduan ✨" else "Lanjut Langkah",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun WudhuStepDetailCard(
    step: WudhuStep,
    totalSteps: Int,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onPlayAudio: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SkyBlue.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "Rukun Wudhu #${step.stepNumber}",
                        color = SkyBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Button(
                    onClick = onPlayAudio,
                    colors = ButtonDefaults.buttonColors(containerColor = SkyBlue),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Dengarkan",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Dengar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = step.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkNavy
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Wudhu Illustration Visual Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(SkyBlue.copy(alpha = 0.2f), IslamicTealContainer)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (step.stepNumber) {
                            1 -> "🧼 🤲🏻"
                            2 -> "💧 👄"
                            3 -> "👃🏻 💧"
                            4 -> "🧔🏻 💧"
                            5 -> "💪🏻 💧"
                            6 -> "🧑🏻 💧"
                            7 -> "👂🏻 💧"
                            8 -> "🦶🏻 💧"
                            else -> "🤲🏻 ✨"
                        },
                        fontSize = 38.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = step.description,
                        fontSize = 12.sp,
                        color = DarkNavy,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (step.arabicText.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAF8)),
                    border = BorderStroke(1.dp, SkyBlue.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = step.arabicText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicTealDark,
                            textAlign = TextAlign.End,
                            lineHeight = 28.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = step.latinText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Artinya: \"${step.translation}\"",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GoldenStarLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💡", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = step.kidTip,
                        fontSize = 12.sp,
                        color = DarkNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onPrev,
                    enabled = step.stepNumber > 1,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Sebelumnya", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = SkyBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.3f)
                ) {
                    Text(
                        text = if (step.stepNumber >= totalSteps) "Wudhu Selesai ✨" else "Lanjut Wudhu",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun QuizCard(
    currentQuizIndex: Int,
    selectedAnswer: Int?,
    feedback: String?,
    onAnswer: (Int) -> Unit,
    onNext: () -> Unit
) {
    val quiz = PrayerContentProvider.dailyQuizzes.getOrNull(currentQuizIndex)
        ?: PrayerContentProvider.dailyQuizzes.first()

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("quiz_card")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = CoralOrange.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "Pertanyaan #${currentQuizIndex + 1} / ${PrayerContentProvider.dailyQuizzes.size}",
                        color = CoralOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GoldenStarLight
                ) {
                    Text(
                        text = "+${quiz.xpBonus} XP Bintang",
                        color = GoldenAmberDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = quiz.question,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkNavy,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Options
            quiz.options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == index
                val isCorrect = index == quiz.correctIndex
                val optBg = when {
                    selectedAnswer == null -> Color(0xFFF8FAF9)
                    isSelected && isCorrect -> BrightGreen.copy(alpha = 0.15f)
                    isSelected && !isCorrect -> CoralOrange.copy(alpha = 0.15f)
                    selectedAnswer != null && isCorrect -> BrightGreen.copy(alpha = 0.15f)
                    else -> Color(0xFFF8FAF9)
                }
                val optBorder = when {
                    selectedAnswer == null -> BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
                    isSelected && isCorrect -> BorderStroke(1.5.dp, BrightGreen)
                    isSelected && !isCorrect -> BorderStroke(1.5.dp, CoralOrange)
                    selectedAnswer != null && isCorrect -> BorderStroke(1.5.dp, BrightGreen)
                    else -> BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
                }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = optBg),
                    border = optBorder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = selectedAnswer == null) {
                            onAnswer(index)
                        }
                        .testTag("quiz_option_$index")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) IslamicTeal else Color.LightGray.copy(alpha = 0.3f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = listOf("A", "B", "C", "D").getOrElse(index) { "?" },
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else DarkNavy,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = option,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkNavy
                        )
                    }
                }
            }

            // Feedback Message
            feedback?.let { msg ->
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedAnswer == quiz.correctIndex) BrightGreen.copy(alpha = 0.15f) else CoralOrange.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = msg,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selectedAnswer == quiz.correctIndex) BrightGreen else CoralOrange,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onNext,
                enabled = selectedAnswer != null,
                colors = ButtonDefaults.buttonColors(containerColor = IslamicTeal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("next_quiz_button")
            ) {
                Text("Pertanyaan Berikutnya ➡️", fontWeight = FontWeight.Bold)
            }
        }
    }
}
