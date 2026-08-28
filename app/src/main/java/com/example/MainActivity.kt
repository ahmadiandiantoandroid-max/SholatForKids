package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.PrayerType
import com.example.ui.components.CelebrationDialog
import com.example.ui.screens.GuideScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.ProfileAndRemindersScreen
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.GoldenStar
import com.example.ui.theme.IslamicTeal
import com.example.ui.theme.IslamicTealContainer
import com.example.ui.theme.IslamicTealDark
import com.example.ui.theme.JagoSholatTheme
import com.example.ui.viewmodel.GuideMode
import com.example.ui.viewmodel.PrayerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JagoSholatTheme {
                JagoSholatApp()
            }
        }
    }
}

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    BERANDA("Beranda", Icons.Filled.Home, Icons.Outlined.Home, "tab_beranda"),
    PANDUAN("Panduan", Icons.Filled.Book, Icons.Outlined.Book, "tab_panduan"),
    PERINGKAT("Peringkat", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents, "tab_peringkat"),
    PROFIL("Piala & Profil", Icons.Filled.Person, Icons.Outlined.Person, "tab_profil")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagoSholatApp(viewModel: PrayerViewModel = viewModel()) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val todayRecords by viewModel.todayRecords.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    val leaderboard by viewModel.leaderboard.collectAsStateWithLifecycle()
    val reminders by viewModel.reminders.collectAsStateWithLifecycle()

    val guideMode by viewModel.guideMode.collectAsStateWithLifecycle()
    val selectedGuidePrayer by viewModel.selectedGuidePrayer.collectAsStateWithLifecycle()
    val currentStepIndex by viewModel.currentGuideStepIndex.collectAsStateWithLifecycle()
    val currentQuizIndex by viewModel.currentQuizIndex.collectAsStateWithLifecycle()
    val selectedQuizAnswer by viewModel.selectedQuizAnswer.collectAsStateWithLifecycle()
    val quizFeedback by viewModel.quizFeedback.collectAsStateWithLifecycle()

    val celebrationState by viewModel.celebrationState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(GoldenStar),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🕌", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Jago Sholat",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 19.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            shape = CircleShape,
                            color = GoldenStar.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "⭐ ANAK",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldenStar,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = IslamicTealDark
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_navigation_bar")
            ) {
                MainTab.entries.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = IslamicTealDark,
                            selectedTextColor = IslamicTealDark,
                            indicatorColor = IslamicTealContainer,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> {
                    HomeScreen(
                        userProfile = userProfile,
                        todayRecords = todayRecords,
                        onCompletePrayer = { prayer, isOntime, isJamaah, notes ->
                            viewModel.completePrayer(prayer, isOntime, isJamaah, notes)
                        },
                        onOpenGuide = { prayer ->
                            viewModel.selectPrayerGuide(prayer)
                            selectedTab = 1 // Switch to guide tab
                        },
                        onOpenProfile = {
                            selectedTab = 3 // Switch to profile tab
                        },
                        onTestNotification = { prayerName ->
                            viewModel.testReminderNotification(prayerName)
                        }
                    )
                }

                1 -> {
                    GuideScreen(
                        guideMode = guideMode,
                        selectedPrayer = selectedGuidePrayer,
                        currentStepIndex = currentStepIndex,
                        currentQuizIndex = currentQuizIndex,
                        selectedQuizAnswer = selectedQuizAnswer,
                        quizFeedback = quizFeedback,
                        onSelectMode = { viewModel.setGuideMode(it) },
                        onSelectPrayer = { viewModel.selectPrayerGuide(it) },
                        onStepSelected = { viewModel.setGuideStep(it) },
                        onNextStep = { total -> viewModel.nextGuideStep(total) },
                        onPrevStep = { viewModel.prevGuideStep() },
                        onPlayStepAudio = { viewModel.playStepAudio(it) },
                        onPlayWudhuAudio = { viewModel.playWudhuAudio(it) },
                        onAnswerQuiz = { viewModel.answerQuiz(it) },
                        onNextQuiz = { viewModel.nextQuizQuestion() }
                    )
                }

                2 -> {
                    LeaderboardScreen(
                        leaderboard = leaderboard,
                        onAddFriend = { name, avatar, title, xp ->
                            viewModel.addNewFriend(name, avatar, title, xp)
                        }
                    )
                }

                3 -> {
                    ProfileAndRemindersScreen(
                        userProfile = userProfile,
                        achievements = achievements,
                        reminders = reminders,
                        onUpdateProfile = { name, avatar, title ->
                            viewModel.updateProfile(name, avatar, title)
                        },
                        onToggleReminder = { reminder, enabled ->
                            viewModel.toggleReminder(reminder, enabled)
                        },
                        onTestReminder = { prayerName ->
                            viewModel.testReminderNotification(prayerName)
                        }
                    )
                }
            }

            // Confetti / Celebration popup modal
            if (celebrationState.isVisible) {
                CelebrationDialog(
                    prayerName = celebrationState.prayerName,
                    pointsEarned = celebrationState.pointsEarned,
                    isLevelUp = celebrationState.isLevelUp,
                    newLevel = celebrationState.newLevel,
                    onDismiss = { viewModel.dismissCelebration() }
                )
            }
        }
    }
}
