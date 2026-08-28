package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.PrayerRecordEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.PrayerType
import com.example.ui.components.KidHeaderSection
import com.example.ui.theme.BrightGreen
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.DarkNavySurface
import com.example.ui.theme.GoldenAmberDark
import com.example.ui.theme.GoldenStar
import com.example.ui.theme.GoldenStarLight
import com.example.ui.theme.IslamicTeal
import com.example.ui.theme.IslamicTealContainer
import com.example.ui.theme.IslamicTealDark
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.SoftBgLight

@Composable
fun HomeScreen(
    userProfile: UserProfileEntity?,
    todayRecords: List<PrayerRecordEntity>,
    onCompletePrayer: (PrayerType, Boolean, Boolean, String) -> Unit,
    onOpenGuide: (PrayerType) -> Unit,
    onOpenProfile: () -> Unit,
    onTestNotification: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var prayerToLog by remember { mutableStateOf<PrayerType?>(null) }
    val completedTypes = remember(todayRecords) {
        todayRecords.map { it.prayerType }.toSet()
    }
    val completedCount = completedTypes.count { it != "DHUHA" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftBgLight)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Profile & Level Status Card
            KidHeaderSection(
                userProfile = userProfile,
                onProfileClick = onOpenProfile
            )
        }

        // Hero Next Prayer Banner
        item {
            HeroNextPrayerCard(
                completedCount = completedCount,
                onTestReminder = onTestNotification
            )
        }

        // Section Title: Sholat 5 Waktu
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Jadwal & Ceklis Hari Ini",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (completedCount >= 5) BrightGreen.copy(alpha = 0.2f) else IslamicTealContainer
                ) {
                    Text(
                        text = "$completedCount / 5 Fardhu",
                        color = if (completedCount >= 5) BrightGreen else IslamicTealDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // 5 Daily Prayers List
        items(PrayerType.entries) { prayer ->
            val isCompleted = completedTypes.contains(prayer.idName)
            val record = todayRecords.find { it.prayerType == prayer.idName }

            PrayerItemCard(
                prayer = prayer,
                isCompleted = isCompleted,
                record = record,
                onCheckClick = {
                    prayerToLog = prayer
                },
                onGuideClick = {
                    onOpenGuide(prayer)
                }
            )
        }

        // Daily Quest / Misi Harian Santri
        item {
            DailyMissionsCard(
                completedCount = completedCount,
                hasJamaah = todayRecords.any { it.isJamaah }
            )
            Spacer(modifier = Modifier.height(18.dp))
        }
    }

    // Interactive Dialog to log prayer with bonuses
    prayerToLog?.let { prayer ->
        LogPrayerDialog(
            prayer = prayer,
            onDismiss = { prayerToLog = null },
            onConfirm = { isOntime, isJamaah, notes ->
                onCompletePrayer(prayer, isOntime, isJamaah, notes)
                prayerToLog = null
            }
        )
    }
}

@Composable
fun HeroNextPrayerCard(
    completedCount: Int,
    onTestReminder: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = IslamicTeal),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(IslamicTealDark, IslamicTeal)
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GoldenStar.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🕌", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Semangat Ibadah Hari Ini",
                                color = GoldenStarLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (completedCount == 5) "Alhamdulillah 5 Waktu Selesai!" else "Yuk Jaga Sholat Tepat Waktu!",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Quick test reminder alarm
                    IconButton(
                        onClick = { onTestReminder("Dzuhur") },
                        modifier = Modifier.testTag("test_adzan_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Test Pengingat Adzan",
                            tint = GoldenStar
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "💡 Sabda Nabi: 'Sholat adalah tiang agama.' Setiap sujud dan doa mendekatkan kita ke surga!",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun PrayerItemCard(
    prayer: PrayerType,
    isCompleted: Boolean,
    record: PrayerRecordEntity?,
    onCheckClick: () -> Unit,
    onGuideClick: () -> Unit
) {
    val cardBg = if (isCompleted) Color(0xFFF1F8F5) else Color.White
    val borderColor = if (isCompleted) BrightGreen else Color.LightGray.copy(alpha = 0.5f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag("prayer_card_${prayer.idName}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(if (isCompleted) 1.5.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCompleted) 1.dp else 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Prayer Info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isCompleted) BrightGreen.copy(alpha = 0.15f)
                                else IslamicTealContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val iconEmoji = when (prayer) {
                            PrayerType.SUBUH -> "🌅"
                            PrayerType.DZUHUR -> "☀️"
                            PrayerType.ASHAR -> "🌤️"
                            PrayerType.MAGHRIB -> "🌇"
                            PrayerType.ISYA -> "🌙"
                            PrayerType.DHUHA -> "✨"
                        }
                        Text(iconEmoji, fontSize = 22.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = prayer.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DarkNavy
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = IslamicTeal.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "${prayer.rakaat} Rakaat",
                                    fontSize = 10.sp,
                                    color = IslamicTeal,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Waktu",
                                tint = Color.Gray,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Waktu: ${prayer.defaultTime}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "•  +${prayer.defaultPoints} XP",
                                fontSize = 12.sp,
                                color = GoldenAmberDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Check Button or Completed Badge
                if (isCompleted) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BrightGreen.copy(alpha = 0.15f),
                        modifier = Modifier.clickable { onCheckClick() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selesai",
                                tint = BrightGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Selesai",
                                color = BrightGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = onCheckClick,
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicTeal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_complete_${prayer.idName}")
                    ) {
                        Text(
                            text = "Sholat!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Bottom Actions: Detail Guide Button
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (record != null && (record.isJamaah || record.isOntime)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (record.isJamaah) {
                            Text(
                                text = "👥 Berjamaah (+30 XP)",
                                fontSize = 11.sp,
                                color = SkyBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        if (record.isOntime) {
                            Text(
                                text = "⏰ Tepat Waktu (+20 XP)",
                                fontSize = 11.sp,
                                color = BrightGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    Text(
                        text = prayer.description,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                OutlinedButton(
                    onClick = onGuideClick,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, IslamicTeal.copy(alpha = 0.5f)),
                    modifier = Modifier.height(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "Panduan",
                        tint = IslamicTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Panduan",
                        color = IslamicTeal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun LogPrayerDialog(
    prayer: PrayerType,
    onDismiss: () -> Unit,
    onConfirm: (Boolean, Boolean, String) -> Unit
) {
    var isOntime by remember { mutableStateOf(true) }
    var isJamaah by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("log_prayer_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🌟 Ceklis Sholat ${prayer.displayName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Kumpulkan poin bintang ekstra dengan mengisi pilihan di bawah!",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Options
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftBgLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isOntime = !isOntime }
                        ) {
                            Checkbox(
                                checked = isOntime,
                                onCheckedChange = { isOntime = it },
                                colors = CheckboxDefaults.colors(checkedColor = IslamicTeal)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Sholat Tepat Waktu",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = DarkNavy
                                )
                                Text(
                                    text = "Bonus +20 XP Bintang",
                                    fontSize = 11.sp,
                                    color = BrightGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isJamaah = !isJamaah }
                        ) {
                            Checkbox(
                                checked = isJamaah,
                                onCheckedChange = { isJamaah = it },
                                colors = CheckboxDefaults.colors(checkedColor = IslamicTeal)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Sholat Berjamaah",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = DarkNavy
                                )
                                Text(
                                    text = "Bonus +30 XP Bintang (27x Pahala)",
                                    fontSize = 11.sp,
                                    color = SkyBlue
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

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
                        onClick = { onConfirm(isOntime, isJamaah, "") },
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicTeal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("confirm_log_prayer_button")
                    ) {
                        Text("Simpan & Raih XP", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DailyMissionsCard(
    completedCount: Int,
    hasJamaah: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Misi",
                    tint = GoldenAmberDark,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Misi Harian Santri Cilik",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            MissionItem(
                title = "Selesaikan Minimal 3 Sholat Fardhu",
                progressText = "$completedCount / 3",
                isCompleted = completedCount >= 3,
                rewardXp = 50
            )

            Spacer(modifier = Modifier.height(8.dp))

            MissionItem(
                title = "Lakukan 1 Sholat Berjamaah",
                progressText = if (hasJamaah) "1 / 1" else "0 / 1",
                isCompleted = hasJamaah,
                rewardXp = 40
            )

            Spacer(modifier = Modifier.height(8.dp))

            MissionItem(
                title = "Pelajari 1 Langkah Gerakan & Bacaan Sholat",
                progressText = "Siap Belajar",
                isCompleted = false,
                rewardXp = 30
            )
        }
    }
}

@Composable
fun MissionItem(
    title: String,
    progressText: String,
    isCompleted: Boolean,
    rewardXp: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isCompleted) BrightGreen.copy(alpha = 0.1f) else SoftBgLight)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isCompleted) BrightGreen else Color.Gray,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = if (isCompleted) DarkNavy else Color.DarkGray,
                fontWeight = if (isCompleted) FontWeight.SemiBold else FontWeight.Normal
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isCompleted) BrightGreen.copy(alpha = 0.2f) else GoldenStarLight
        ) {
            Text(
                text = if (isCompleted) "Selesai ✨" else "+$rewardXp XP",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted) BrightGreen else GoldenAmberDark,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}
