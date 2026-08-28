package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_records")
data class PrayerRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val prayerType: String,
    val dateString: String, // Format: YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val pointsEarned: Int,
    val isOntime: Boolean = true,
    val isJamaah: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Santri Hebat",
    val avatarId: String = "avatar_boy_1",
    val xp: Int = 120,
    val level: Int = 1,
    val currentStreak: Int = 3,
    val longestStreak: Int = 7,
    val lastCompletedDate: String = "",
    val totalPrayersCompleted: Int = 8,
    val totalJamaahCount: Int = 4,
    val selectedTitle: String = "Santri Cilik",
    val starsCount: Int = 45
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val badgeIcon: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false,
    val unlockedDate: String? = null,
    val currentProgress: Int = 0,
    val targetProgress: Int = 1
)

@Entity(tableName = "leaderboard_entries")
data class LeaderboardEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val avatarId: String,
    val xp: Int,
    val rank: Int,
    val title: String,
    val streakDays: Int = 1,
    val isCurrentUser: Boolean = false,
    val isFriend: Boolean = true
)

@Entity(tableName = "prayer_reminders")
data class PrayerReminderEntity(
    @PrimaryKey
    val prayerName: String,
    val timeString: String,
    val isEnabled: Boolean = true,
    val reminderMinutesBefore: Int = 0,
    val soundOption: String = "Adzan Anak Merdu"
)
