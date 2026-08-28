package com.example.data.repository

import com.example.data.local.dao.PrayerDao
import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.LeaderboardEntity
import com.example.data.local.entity.PrayerRecordEntity
import com.example.data.local.entity.PrayerReminderEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.PrayerType
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrayerRepository(private val prayerDao: PrayerDao) {

    fun getRecordsForDate(date: String): Flow<List<PrayerRecordEntity>> =
        prayerDao.getRecordsByDate(date)

    fun getUserProfile(): Flow<UserProfileEntity?> =
        prayerDao.getUserProfile()

    fun getAllAchievements(): Flow<List<AchievementEntity>> =
        prayerDao.getAllAchievements()

    fun getLeaderboard(): Flow<List<LeaderboardEntity>> =
        prayerDao.getLeaderboard()

    fun getAllReminders(): Flow<List<PrayerReminderEntity>> =
        prayerDao.getAllReminders()

    suspend fun logPrayer(
        prayerType: PrayerType,
        isOntime: Boolean = true,
        isJamaah: Boolean = false,
        notes: String = ""
    ): Int {
        val todayStr = getTodayDateString()
        var points = prayerType.defaultPoints
        if (isOntime) points += 20 // Bonus tepat waktu
        if (isJamaah) points += 30 // Bonus jamaah

        val record = PrayerRecordEntity(
            prayerType = prayerType.idName,
            dateString = todayStr,
            timestamp = System.currentTimeMillis(),
            pointsEarned = points,
            isOntime = isOntime,
            isJamaah = isJamaah,
            notes = notes
        )
        prayerDao.insertPrayerRecord(record)

        // Update User Profile
        val currentProfile = prayerDao.getUserProfileOnce() ?: UserProfileEntity()
        val newXp = currentProfile.xp + points
        val newLevel = (newXp / 200) + 1
        val newStars = currentProfile.starsCount + (points / 10)
        val newTotalPrayers = currentProfile.totalPrayersCompleted + 1
        val newJamaahCount = if (isJamaah) currentProfile.totalJamaahCount + 1 else currentProfile.totalJamaahCount

        // Check Streak
        var newStreak = currentProfile.currentStreak
        if (currentProfile.lastCompletedDate != todayStr) {
            newStreak += 1
        }
        val longestStreak = maxOf(newStreak, currentProfile.longestStreak)

        val updatedProfile = currentProfile.copy(
            xp = newXp,
            level = newLevel,
            currentStreak = newStreak,
            longestStreak = longestStreak,
            lastCompletedDate = todayStr,
            totalPrayersCompleted = newTotalPrayers,
            totalJamaahCount = newJamaahCount,
            starsCount = newStars
        )
        prayerDao.insertOrUpdateProfile(updatedProfile)

        // Update Leaderboard current user entry
        updateCurrentUserLeaderboard(updatedProfile)

        // Check Achievements
        checkAndUnlockAchievements(updatedProfile, prayerType, isJamaah)

        return points
    }

    suspend fun addQuizXp(bonusXp: Int) {
        val currentProfile = prayerDao.getUserProfileOnce() ?: UserProfileEntity()
        val newXp = currentProfile.xp + bonusXp
        val newLevel = (newXp / 200) + 1
        val updated = currentProfile.copy(
            xp = newXp,
            level = newLevel,
            starsCount = currentProfile.starsCount + (bonusXp / 5)
        )
        prayerDao.insertOrUpdateProfile(updated)
        updateCurrentUserLeaderboard(updated)
    }

    suspend fun updateProfileAvatar(avatarId: String, name: String, title: String) {
        val currentProfile = prayerDao.getUserProfileOnce() ?: UserProfileEntity()
        val updated = currentProfile.copy(
            avatarId = avatarId,
            name = name,
            selectedTitle = title
        )
        prayerDao.insertOrUpdateProfile(updated)
        updateCurrentUserLeaderboard(updated)
    }

    suspend fun updateReminder(reminder: PrayerReminderEntity) {
        prayerDao.updateReminder(reminder)
    }

    suspend fun addFriend(name: String, avatarId: String, title: String, xp: Int) {
        val friend = LeaderboardEntity(
            id = "friend_${System.currentTimeMillis()}",
            name = name,
            avatarId = avatarId,
            xp = xp,
            rank = 99,
            title = title,
            streakDays = 3,
            isCurrentUser = false,
            isFriend = true
        )
        prayerDao.insertLeaderboardEntry(friend)
    }

    private suspend fun updateCurrentUserLeaderboard(profile: UserProfileEntity) {
        val userEntry = LeaderboardEntity(
            id = "user_me",
            name = "${profile.name} (Kamu)",
            avatarId = profile.avatarId,
            xp = profile.xp,
            rank = 1,
            title = profile.selectedTitle,
            streakDays = profile.currentStreak,
            isCurrentUser = true,
            isFriend = false
        )
        prayerDao.insertLeaderboardEntry(userEntry)
    }

    private suspend fun checkAndUnlockAchievements(
        profile: UserProfileEntity,
        prayerType: PrayerType,
        isJamaah: Boolean
    ) {
        val todayStr = getTodayDateString()

        // Pejuang subuh check
        if (prayerType == PrayerType.SUBUH) {
            val subuhBadge = prayerDao.getAchievementById("badge_subuh_first")
            if (subuhBadge != null && !subuhBadge.isUnlocked) {
                prayerDao.updateAchievement(
                    subuhBadge.copy(
                        isUnlocked = true,
                        unlockedDate = todayStr,
                        currentProgress = 1
                    )
                )
            }
        }

        // Streak check
        val streakBadge = prayerDao.getAchievementById("badge_streak_7")
        if (streakBadge != null) {
            val updatedProgress = minOf(profile.currentStreak, 7)
            val isUnlocked = updatedProgress >= 7
            prayerDao.updateAchievement(
                streakBadge.copy(
                    currentProgress = updatedProgress,
                    isUnlocked = isUnlocked,
                    unlockedDate = if (isUnlocked && streakBadge.unlockedDate == null) todayStr else streakBadge.unlockedDate
                )
            )
        }

        // Jamaah check
        val jamaahBadge = prayerDao.getAchievementById("badge_jamaah_star")
        if (jamaahBadge != null) {
            val updatedProgress = profile.totalJamaahCount
            val isUnlocked = updatedProgress >= 5
            prayerDao.updateAchievement(
                jamaahBadge.copy(
                    currentProgress = updatedProgress,
                    isUnlocked = isUnlocked,
                    unlockedDate = if (isUnlocked && jamaahBadge.unlockedDate == null) todayStr else jamaahBadge.unlockedDate
                )
            )
        }
    }

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
