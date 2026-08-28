package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.LeaderboardEntity
import com.example.data.local.entity.PrayerRecordEntity
import com.example.data.local.entity.PrayerReminderEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerDao {

    // --- Prayer Records ---
    @Query("SELECT * FROM prayer_records WHERE dateString = :date ORDER BY timestamp ASC")
    fun getRecordsByDate(date: String): Flow<List<PrayerRecordEntity>>

    @Query("SELECT * FROM prayer_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<PrayerRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerRecord(record: PrayerRecordEntity): Long

    @Query("DELETE FROM prayer_records WHERE id = :id")
    suspend fun deletePrayerRecord(id: Long)

    @Query("SELECT COUNT(*) FROM prayer_records WHERE dateString = :date AND prayerType = :prayerType")
    suspend fun countRecordForPrayer(date: String, prayerType: String): Int

    // --- User Profile ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // --- Achievements ---
    @Query("SELECT * FROM achievements ORDER BY isUnlocked DESC, xpReward ASC")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAchievements(list: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    @Query("SELECT * FROM achievements WHERE id = :id LIMIT 1")
    suspend fun getAchievementById(id: String): AchievementEntity?

    // --- Leaderboard ---
    @Query("SELECT * FROM leaderboard_entries ORDER BY xp DESC")
    fun getLeaderboard(): Flow<List<LeaderboardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntries(list: List<LeaderboardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntry(entry: LeaderboardEntity)

    // --- Reminders ---
    @Query("SELECT * FROM prayer_reminders ORDER BY timeString ASC")
    fun getAllReminders(): Flow<List<PrayerReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(list: List<PrayerReminderEntity>)

    @Update
    suspend fun updateReminder(reminder: PrayerReminderEntity)
}
