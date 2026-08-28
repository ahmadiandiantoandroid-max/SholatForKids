package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.PrayerDao
import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.LeaderboardEntity
import com.example.data.local.entity.PrayerRecordEntity
import com.example.data.local.entity.PrayerReminderEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PrayerRecordEntity::class,
        UserProfileEntity::class,
        AchievementEntity::class,
        LeaderboardEntity::class,
        PrayerReminderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jago_sholat_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateInitialData(database.prayerDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: PrayerDao) {
            // Initial Profile
            dao.insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Bintang Cilik",
                    avatarId = "avatar_boy_1",
                    xp = 240,
                    level = 2,
                    currentStreak = 4,
                    longestStreak = 7,
                    lastCompletedDate = "",
                    totalPrayersCompleted = 12,
                    totalJamaahCount = 6,
                    selectedTitle = "Bintang Sholat",
                    starsCount = 65
                )
            )

            // Initial Reminders
            dao.insertReminders(
                listOf(
                    PrayerReminderEntity("SUBUH", "04:45", true, 5, "Adzan Anak Merdu"),
                    PrayerReminderEntity("DZUHUR", "12:05", true, 5, "Adzan Anak Merdu"),
                    PrayerReminderEntity("ASHAR", "15:20", true, 5, "Adzan Anak Merdu"),
                    PrayerReminderEntity("MAGHRIB", "18:05", true, 5, "Adzan Merdu Syahdu"),
                    PrayerReminderEntity("ISYA", "19:18", true, 5, "Adzan Anak Merdu"),
                    PrayerReminderEntity("DHUHA", "07:30", true, 0, "Bel Ceria Pagi")
                )
            )

            // Initial Badges / Achievements
            dao.insertAchievements(
                listOf(
                    AchievementEntity(
                        id = "badge_subuh_first",
                        title = "Pejuang Subuh",
                        description = "Selesaikan sholat Subuh tepat waktu saat fajar",
                        badgeIcon = "ic_subuh_hero",
                        xpReward = 100,
                        isUnlocked = true,
                        unlockedDate = "2026-08-27",
                        currentProgress = 1,
                        targetProgress = 1
                    ),
                    AchievementEntity(
                        id = "badge_streak_3",
                        title = "Api Istiqomah 3 Hari",
                        description = "Sholat 3 hari berturut-turut tanpa terputus",
                        badgeIcon = "ic_fire_streak",
                        xpReward = 150,
                        isUnlocked = true,
                        unlockedDate = "2026-08-26",
                        currentProgress = 3,
                        targetProgress = 3
                    ),
                    AchievementEntity(
                        id = "badge_jamaah_star",
                        title = "Bintang Jamaah",
                        description = "Lakukan sholat berjamaah bersama keluarga/teman 5 kali",
                        badgeIcon = "ic_jamaah_group",
                        xpReward = 200,
                        isUnlocked = true,
                        unlockedDate = "2026-08-28",
                        currentProgress = 6,
                        targetProgress = 5
                    ),
                    AchievementEntity(
                        id = "badge_5_waktu_day",
                        title = "Ksatria 5 Waktu",
                        description = "Selesaikan seluruh 5 waktu sholat fardhu dalam 1 hari",
                        badgeIcon = "ic_five_stars",
                        xpReward = 250,
                        isUnlocked = false,
                        unlockedDate = null,
                        currentProgress = 3,
                        targetProgress = 5
                    ),
                    AchievementEntity(
                        id = "badge_wudhu_master",
                        title = "Master Wudhu Bersih",
                        description = "Pelajari dan kuasai semua rukun wudhu secara sempurna",
                        badgeIcon = "ic_water_drop",
                        xpReward = 120,
                        isUnlocked = true,
                        unlockedDate = "2026-08-25",
                        currentProgress = 7,
                        targetProgress = 7
                    ),
                    AchievementEntity(
                        id = "badge_streak_7",
                        title = "Juara 7 Hari Istiqomah",
                        description = "Pertahankan streak ibadah selama 7 hari penuh",
                        badgeIcon = "ic_trophy_gold",
                        xpReward = 500,
                        isUnlocked = false,
                        unlockedDate = null,
                        currentProgress = 4,
                        targetProgress = 7
                    ),
                    AchievementEntity(
                        id = "badge_hafal_iftitah",
                        title = "Hafidz Bacaan Sholat",
                        description = "Selesaikan semua modul latihan bacaan dan arti sholat",
                        badgeIcon = "ic_quran_book",
                        xpReward = 300,
                        isUnlocked = false,
                        unlockedDate = null,
                        currentProgress = 5,
                        targetProgress = 8
                    )
                )
            )

            // Initial Friendly Kids Leaderboard
            dao.insertLeaderboardEntries(
                listOf(
                    LeaderboardEntity(
                        id = "friend_1",
                        name = "Salman Al-Farisi",
                        avatarId = "avatar_boy_2",
                        xp = 580,
                        rank = 1,
                        title = "Hafidz Cilik",
                        streakDays = 12,
                        isCurrentUser = false,
                        isFriend = true
                    ),
                    LeaderboardEntity(
                        id = "friend_2",
                        name = "Aisyah Zahira",
                        avatarId = "avatar_girl_1",
                        xp = 460,
                        rank = 2,
                        title = "Bintang Istiqomah",
                        streakDays = 8,
                        isCurrentUser = false,
                        isFriend = true
                    ),
                    LeaderboardEntity(
                        id = "user_me",
                        name = "Bintang Cilik (Kamu)",
                        avatarId = "avatar_boy_1",
                        xp = 380,
                        rank = 3,
                        title = "Pejuang Subuh",
                        streakDays = 4,
                        isCurrentUser = true,
                        isFriend = false
                    ),
                    LeaderboardEntity(
                        id = "friend_3",
                        name = "Fatimah Azzahra",
                        avatarId = "avatar_girl_2",
                        xp = 340,
                        rank = 4,
                        title = "Santri Hebat",
                        streakDays = 5,
                        isCurrentUser = false,
                        isFriend = true
                    ),
                    LeaderboardEntity(
                        id = "friend_4",
                        name = "Umar Ibnu Khattab",
                        avatarId = "avatar_boy_3",
                        xp = 290,
                        rank = 5,
                        title = "Sahabat Masjid",
                        streakDays = 3,
                        isCurrentUser = false,
                        isFriend = true
                    ),
                    LeaderboardEntity(
                        id = "friend_5",
                        name = "Rayyan Al-Fatih",
                        avatarId = "avatar_boy_4",
                        xp = 210,
                        rank = 6,
                        title = "Pemula Semangat",
                        streakDays = 2,
                        isCurrentUser = false,
                        isFriend = true
                    )
                )
            )
        }
    }
}
