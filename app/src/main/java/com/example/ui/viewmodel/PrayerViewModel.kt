package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.LeaderboardEntity
import com.example.data.local.entity.PrayerRecordEntity
import com.example.data.local.entity.PrayerReminderEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.PrayerStep
import com.example.data.model.PrayerType
import com.example.data.model.WudhuStep
import com.example.data.provider.PrayerContentProvider
import com.example.data.repository.PrayerRepository
import com.example.util.PrayerNotificationHelper
import com.example.util.PrayerSpeechHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CelebrationUiState(
    val isVisible: Boolean = false,
    val prayerName: String = "",
    val pointsEarned: Int = 0,
    val isLevelUp: Boolean = false,
    val newLevel: Int = 1
)

enum class GuideMode {
    SHOLAT,
    WUDHU,
    KUIS
}

class PrayerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PrayerRepository
    private val speechHelper: PrayerSpeechHelper = PrayerSpeechHelper(application)

    val userProfile: StateFlow<UserProfileEntity?>
    val todayRecords: StateFlow<List<PrayerRecordEntity>>
    val achievements: StateFlow<List<AchievementEntity>>
    val leaderboard: StateFlow<List<LeaderboardEntity>>
    val reminders: StateFlow<List<PrayerReminderEntity>>

    // Guide State
    private val _selectedGuidePrayer = MutableStateFlow(PrayerType.SUBUH)
    val selectedGuidePrayer = _selectedGuidePrayer.asStateFlow()

    private val _currentGuideStepIndex = MutableStateFlow(0)
    val currentGuideStepIndex = _currentGuideStepIndex.asStateFlow()

    private val _guideMode = MutableStateFlow(GuideMode.SHOLAT)
    val guideMode = _guideMode.asStateFlow()

    // Celebration Dialog State
    private val _celebrationState = MutableStateFlow(CelebrationUiState())
    val celebrationState = _celebrationState.asStateFlow()

    // Quiz State
    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex = _currentQuizIndex.asStateFlow()

    private val _selectedQuizAnswer = MutableStateFlow<Int?>(null)
    val selectedQuizAnswer = _selectedQuizAnswer.asStateFlow()

    private val _quizFeedback = MutableStateFlow<String?>(null)
    val quizFeedback = _quizFeedback.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = PrayerRepository(database.prayerDao())

        userProfile = repository.getUserProfile()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        val today = repository.getTodayDateString()
        todayRecords = repository.getRecordsForDate(today)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        achievements = repository.getAllAchievements()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        leaderboard = repository.getLeaderboard()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        reminders = repository.getAllReminders()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun completePrayer(
        prayerType: PrayerType,
        isOntime: Boolean = true,
        isJamaah: Boolean = false,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val oldLevel = userProfile.value?.level ?: 1
            val earnedPoints = repository.logPrayer(prayerType, isOntime, isJamaah, notes)
            val newProfile = userProfile.value
            val isLevelUp = (newProfile?.level ?: 1) > oldLevel

            speechHelper.playCelebrationChime()
            speechHelper.speak("Alhamdulillah! Hebat sekali, sholat ${prayerType.displayName} selesai! Kamu dapat $earnedPoints poin bintang!")

            _celebrationState.value = CelebrationUiState(
                isVisible = true,
                prayerName = prayerType.displayName,
                pointsEarned = earnedPoints,
                isLevelUp = isLevelUp,
                newLevel = newProfile?.level ?: (oldLevel + 1)
            )
        }
    }

    fun dismissCelebration() {
        _celebrationState.value = CelebrationUiState(isVisible = false)
    }

    fun setGuideMode(mode: GuideMode) {
        _guideMode.value = mode
        _currentGuideStepIndex.value = 0
        speechHelper.stop()
    }

    fun selectPrayerGuide(prayerType: PrayerType) {
        _selectedGuidePrayer.value = prayerType
        _currentGuideStepIndex.value = 0
        _guideMode.value = GuideMode.SHOLAT
        speechHelper.stop()
    }

    fun nextGuideStep(totalSteps: Int) {
        if (_currentGuideStepIndex.value < totalSteps - 1) {
            _currentGuideStepIndex.value += 1
            speechHelper.playSuccessBeep()
        }
    }

    fun prevGuideStep() {
        if (_currentGuideStepIndex.value > 0) {
            _currentGuideStepIndex.value -= 1
            speechHelper.stop()
        }
    }

    fun setGuideStep(index: Int) {
        _currentGuideStepIndex.value = index
        speechHelper.stop()
    }

    fun playStepAudio(step: PrayerStep) {
        val speechText = "${step.title}. ${step.latinText}. Artinya: ${step.translation}"
        speechHelper.speak(speechText)
    }

    fun playWudhuAudio(step: WudhuStep) {
        val speechText = "${step.title}. ${step.latinText}. ${step.description}. ${step.kidTip}"
        speechHelper.speak(speechText)
    }

    fun stopAudio() {
        speechHelper.stop()
    }

    // Quiz functions
    fun answerQuiz(index: Int) {
        _selectedQuizAnswer.value = index
        val currentQ = PrayerContentProvider.dailyQuizzes.getOrNull(_currentQuizIndex.value)
        if (currentQ != null) {
            if (index == currentQ.correctIndex) {
                _quizFeedback.value = "Benar! 🎉 " + currentQ.explanation
                speechHelper.playCelebrationChime()
                speechHelper.speak("Jawabanmu benar! Hebat sekali!")
                viewModelScope.launch {
                    repository.addQuizXp(currentQ.xpBonus)
                }
            } else {
                _quizFeedback.value = "Hampir tepat! Jawaban yang benar adalah: " + currentQ.options[currentQ.correctIndex]
                speechHelper.speak("Ayo coba lagi, kamu pasti bisa!")
            }
        }
    }

    fun nextQuizQuestion() {
        _selectedQuizAnswer.value = null
        _quizFeedback.value = null
        val total = PrayerContentProvider.dailyQuizzes.size
        _currentQuizIndex.value = (_currentQuizIndex.value + 1) % total
    }

    fun updateProfile(name: String, avatarId: String, title: String) {
        viewModelScope.launch {
            repository.updateProfileAvatar(avatarId, name, title)
        }
    }

    fun toggleReminder(reminder: PrayerReminderEntity, enabled: Boolean) {
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(isEnabled = enabled))
        }
    }

    fun updateReminderTime(reminder: PrayerReminderEntity, newTime: String) {
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(timeString = newTime))
        }
    }

    fun testReminderNotification(prayerName: String) {
        PrayerNotificationHelper.showPrayerReminder(
            getApplication(),
            prayerName,
            "🕌 Waktunya Sholat $prayerName! Raih pahala dan kumpulkan bintang gamifikasi!"
        )
        speechHelper.playCelebrationChime()
    }

    fun addNewFriend(name: String, avatarId: String, title: String, xp: Int) {
        viewModelScope.launch {
            repository.addFriend(name, avatarId, title, xp)
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechHelper.release()
    }
}
