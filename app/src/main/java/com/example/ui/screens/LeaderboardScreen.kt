package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.LeaderboardEntity
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
fun LeaderboardScreen(
    leaderboard: List<LeaderboardEntity>,
    onAddFriend: (String, String, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddFriendDialog by remember { mutableStateOf(false) }

    val sortedList = remember(leaderboard) {
        leaderboard.sortedByDescending { it.xp }
    }

    val top1 = sortedList.getOrNull(0)
    val top2 = sortedList.getOrNull(1)
    val top3 = sortedList.getOrNull(2)

    val currentUserEntry = sortedList.find { it.isCurrentUser }
    val currentUserRank = if (currentUserEntry != null) sortedList.indexOf(currentUserEntry) + 1 else 3

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SoftBgLight)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Title and Banner
                LeaderboardHeader(currentUserRank = currentUserRank)
            }

            // Top 3 Podium
            item {
                PodiumSection(top1 = top1, top2 = top2, top3 = top3)
            }

            // Leaderboard List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Peringkat Sahabat Sholat",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavy
                    )
                    Text(
                        text = "${sortedList.size} Peserta",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Ranks list
            itemsIndexed(sortedList) { index, entry ->
                LeaderboardRowItem(
                    rank = index + 1,
                    entry = entry
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Floating Action Button to Add / Challenge Friends
        FloatingActionButton(
            onClick = { showAddFriendDialog = true },
            containerColor = IslamicTeal,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_friend_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Tambah Teman")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tambah Teman", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }

    if (showAddFriendDialog) {
        AddFriendDialog(
            onDismiss = { showAddFriendDialog = false },
            onAdd = { name, avatarId, title, xp ->
                onAddFriend(name, avatarId, title, xp)
                showAddFriendDialog = false
            }
        )
    }
}

@Composable
fun LeaderboardHeader(currentUserRank: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = IslamicTealDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(IslamicTealDark, DarkNavy)
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🏆 Liga Santri Bintang",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = GoldenStar
                    )
                    Text(
                        text = "Berlomba-lomba dalam kebaikan (Fastabiqul Khoirot)",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GoldenStar
                ) {
                    Text(
                        text = "Kamu di #$currentUserRank",
                        color = DarkNavy,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PodiumSection(
    top1: LeaderboardEntity?,
    top2: LeaderboardEntity?,
    top3: LeaderboardEntity?
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✨ Tiga Besar Pekan Ini ✨",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DarkNavy
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // 2nd Place (Silver)
                top2?.let {
                    PodiumPillar(
                        entry = it,
                        rank = 2,
                        crownEmoji = "🥈",
                        pillarHeight = 90,
                        pillarColor = Color(0xFFCFD8DC),
                        badgeColor = Color(0xFF90A4AE)
                    )
                }

                // 1st Place (Gold)
                top1?.let {
                    PodiumPillar(
                        entry = it,
                        rank = 1,
                        crownEmoji = "👑 🥇",
                        pillarHeight = 120,
                        pillarColor = GoldenStarLight,
                        badgeColor = GoldenStar
                    )
                }

                // 3rd Place (Bronze)
                top3?.let {
                    PodiumPillar(
                        entry = it,
                        rank = 3,
                        crownEmoji = "🥉",
                        pillarHeight = 75,
                        pillarColor = Color(0xFFFFE0B2),
                        badgeColor = CoralOrange
                    )
                }
            }
        }
    }
}

@Composable
fun PodiumPillar(
    entry: LeaderboardEntity,
    rank: Int,
    crownEmoji: String,
    pillarHeight: Int,
    pillarColor: Color,
    badgeColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(95.dp)
    ) {
        Text(text = crownEmoji, fontSize = 16.sp)

        AvatarDisplay(
            avatarId = entry.avatarId,
            size = if (rank == 1) 50 else 42,
            modifier = Modifier.border(2.dp, badgeColor, CircleShape)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = entry.name.split(" ").firstOrNull() ?: entry.name,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = DarkNavy,
            maxLines = 1
        )

        Text(
            text = "${entry.xp} XP",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = GoldenAmberDark
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Pillar block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pillarHeight.dp)
                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                .background(pillarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = badgeColor
            )
        }
    }
}

@Composable
fun LeaderboardRowItem(
    rank: Int,
    entry: LeaderboardEntity
) {
    val isMe = entry.isCurrentUser
    val itemBg = if (isMe) GoldenStarLight else Color.White
    val borderColor = if (isMe) GoldenStar else Color.LightGray.copy(alpha = 0.3f)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = itemBg),
        border = BorderStroke(if (isMe) 1.5.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isMe) 3.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_${entry.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Rank Number + Avatar + Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            when (rank) {
                                1 -> GoldenStar
                                2 -> Color(0xFF90A4AE)
                                3 -> CoralOrange
                                else -> Color.LightGray.copy(alpha = 0.3f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$rank",
                        fontWeight = FontWeight.Bold,
                        color = if (rank <= 3) Color.White else DarkNavy,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                AvatarDisplay(avatarId = entry.avatarId, size = 40)

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = entry.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = DarkNavy
                        )
                        if (isMe) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = IslamicTeal.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "Kamu",
                                    fontSize = 10.sp,
                                    color = IslamicTeal,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = entry.title,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = CoralOrange,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${entry.streakDays}d",
                            fontSize = 11.sp,
                            color = CoralOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Total Points Chip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isMe) GoldenStar else IslamicTealContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (isMe) DarkNavy else IslamicTealDark,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${entry.xp} XP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isMe) DarkNavy else IslamicTealDark
                    )
                }
            }
        }
    }
}

@Composable
fun AddFriendDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Int) -> Unit
) {
    var friendName by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf("avatar_boy_2") }
    var friendTitle by remember { mutableStateOf("Sahabat Sholat") }

    val avatars = listOf("avatar_boy_1", "avatar_boy_2", "avatar_boy_3", "avatar_boy_4", "avatar_girl_1", "avatar_girl_2")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("add_friend_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👥 Tambah Sahabat Mengaji",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = friendName,
                    onValueChange = { friendName = it },
                    label = { Text("Nama Teman / Sahabat") },
                    placeholder = { Text("Contoh: Rayyan Al-Farizi") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Pilih Karakter Teman:",
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
                            if (friendName.isNotBlank()) {
                                onAdd(friendName.trim(), selectedAvatar, friendTitle, 350)
                            }
                        },
                        enabled = friendName.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicTeal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.4f)
                            .testTag("confirm_add_friend_button")
                    ) {
                        Text("Tambah", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
