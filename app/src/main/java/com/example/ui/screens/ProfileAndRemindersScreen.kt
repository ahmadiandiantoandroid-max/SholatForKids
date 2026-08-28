package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.PrayerReminderEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.ui.components.AvatarDisplay
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

@Composable
fun ProfileAndRemindersScreen(
    userProfile: UserProfileEntity?,
    achievements: List<AchievementEntity>,
    reminders: List<PrayerReminderEntity>,
    onUpdateProfile: (String, String, String) -> Unit,
    onToggleReminder: (PrayerReminderEntity, Boolean) -> Unit,
    onTestReminder: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftBgLight)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Profile Big Card
            ProfileSummaryCard(
                userProfile = userProfile,
                onEditClick = { showEditProfileDialog = true }
            )
        }

        // Achievements & Badges Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = GoldenAmberDark,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Koleksi Piala & Lencana",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavy
                    )
                }

                val unlockedCount = achievements.count { it.isUnlocked }
                Text(
                    text = "$unlockedCount / ${achievements.size} Terbuka",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Achievements list
        items(achievements) { badge ->
            AchievementItemCard(badge = badge)
        }

        // Section: Pengaturan Pengingat Sholat
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = IslamicTeal,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Pengingat & Alarm Sholat",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavy
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = IslamicTealContainer
                ) {
                    Text(
                        text = "Notifikasi Aktif",
                        color = IslamicTealDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Reminders list
        items(reminders) { reminder ->
            ReminderItemCard(
                reminder = reminder,
                onToggle = { enabled -> onToggleReminder(reminder, enabled) },
                onTest = { onTestReminder(reminder.prayerName) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            currentProfile = userProfile,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, avatarId, title ->
                onUpdateProfile(name, avatarId, title)
                showEditProfileDialog = false
            }
        )
    }
}

@Composable
fun ProfileSummaryCard(
    userProfile: UserProfileEntity?,
    onEditClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = IslamicTealDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_summary_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(IslamicTealDark, DarkNavy)
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarDisplay(
                        avatarId = userProfile?.avatarId ?: "avatar_boy_1",
                        size = 64,
                        modifier = Modifier.border(3.dp, GoldenStar, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = userProfile?.name ?: "Bintang Cilik",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Level ${userProfile?.level ?: 1} • ${userProfile?.selectedTitle ?: "Santri Cilik"}",
                            color = GoldenStar,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.testTag("edit_profile_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profil",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStatPill(
                    icon = "⭐",
                    label = "Total XP",
                    value = "${userProfile?.xp ?: 0}"
                )
                ProfileStatPill(
                    icon = "🔥",
                    label = "Streak",
                    value = "${userProfile?.currentStreak ?: 0} Hari"
                )
                ProfileStatPill(
                    icon = "🕌",
                    label = "Total Sholat",
                    value = "${userProfile?.totalPrayersCompleted ?: 0}x"
                )
                ProfileStatPill(
                    icon = "👥",
                    label = "Jamaah",
                    value = "${userProfile?.totalJamaahCount ?: 0}x"
                )
            }
        }
    }
}

@Composable
fun ProfileStatPill(icon: String, label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.12f),
        modifier = Modifier.width(72.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun AchievementItemCard(badge: AchievementEntity) {
    val isUnlocked = badge.isUnlocked
    val cardBg = if (isUnlocked) Color.White else Color(0xFFF5F5F5)
    val borderColor = if (isUnlocked) GoldenStar.copy(alpha = 0.8f) else Color.LightGray.copy(alpha = 0.3f)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(if (isUnlocked) 1.5.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 2.dp else 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("badge_item_${badge.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Badge Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) GoldenStarLight
                        else Color.LightGray.copy(alpha = 0.3f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    val emoji = when (badge.badgeIcon) {
                        "ic_subuh_hero" -> "🌅"
                        "ic_fire_streak" -> "🔥"
                        "ic_jamaah_group" -> "👥"
                        "ic_five_stars" -> "⭐"
                        "ic_water_drop" -> "💧"
                        "ic_trophy_gold" -> "🏆"
                        "ic_quran_book" -> "📖"
                        else -> "🎖️"
                    }
                    Text(emoji, fontSize = 24.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Terkunci",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = badge.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isUnlocked) DarkNavy else Color.Gray
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isUnlocked) GoldenStar else Color.LightGray.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = if (isUnlocked) "Terbuka ✨" else "+${badge.xpReward} XP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) DarkNavy else Color.DarkGray,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = badge.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )

                if (!isUnlocked) {
                    Spacer(modifier = Modifier.height(6.dp))
                    val prog = badge.currentProgress.toFloat() / badge.targetProgress.coerceAtLeast(1)
                    LinearProgressIndicator(
                        progress = { prog.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GoldenAmberDark,
                        trackColor = Color.LightGray.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

@Composable
fun ReminderItemCard(
    reminder: PrayerReminderEntity,
    onToggle: (Boolean) -> Unit,
    onTest: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("reminder_card_${reminder.prayerName}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(IslamicTealContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = null,
                        tint = IslamicTealDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Sholat ${reminder.prayerName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DarkNavy
                    )
                    Text(
                        text = "Jam ${reminder.timeString} • ${reminder.soundOption}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Test button
                IconButton(onClick = onTest) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Tes Adzan",
                        tint = GoldenAmberDark
                    )
                }

                Switch(
                    checked = reminder.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = IslamicTeal
                    ),
                    modifier = Modifier.testTag("switch_${reminder.prayerName}")
                )
            }
        }
    }
}

@Composable
fun EditProfileDialog(
    currentProfile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentProfile?.name ?: "Bintang Cilik") }
    var selectedAvatar by remember { mutableStateOf(currentProfile?.avatarId ?: "avatar_boy_1") }
    var selectedTitle by remember { mutableStateOf(currentProfile?.selectedTitle ?: "Santri Cilik") }

    val avatars = listOf("avatar_boy_1", "avatar_boy_2", "avatar_boy_3", "avatar_boy_4", "avatar_girl_1", "avatar_girl_2")
    val titles = listOf("Santri Cilik", "Bintang Sholat", "Pejuang Subuh", "Hafidz Cilik", "Ksatria Masjid")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("edit_profile_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎨 Edit Karakter Santri",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Kamu") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Pilih Avatar Karakter:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkNavy,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    avatars.forEach { avatar ->
                        val isSelected = avatar == selectedAvatar
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .border(if (isSelected) 2.5.dp else 0.dp, GoldenStar, CircleShape)
                                .clickable { selectedAvatar = avatar }
                        ) {
                            AvatarDisplay(avatarId = avatar, size = 42)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Pilih Gelar / Julukan:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkNavy,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    titles.forEach { titleOption ->
                        val isSelected = titleOption == selectedTitle
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) IslamicTealContainer else Color(0xFFF5F5F5),
                            border = BorderStroke(1.dp, if (isSelected) IslamicTeal else Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTitle = titleOption }
                        ) {
                            Text(
                                text = "🎖️ $titleOption",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) IslamicTealDark else DarkNavy,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal", color = Color.Gray)
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(name.trim(), selectedAvatar, selectedTitle)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicTeal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("save_profile_button")
                    ) {
                        Text("Simpan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
