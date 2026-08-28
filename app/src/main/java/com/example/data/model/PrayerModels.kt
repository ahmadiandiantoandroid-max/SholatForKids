package com.example.data.model

enum class PrayerType(
    val idName: String,
    val displayName: String,
    val rakaat: Int,
    val defaultTime: String,
    val defaultPoints: Int,
    val isFardhu: Boolean,
    val description: String,
    val iconKey: String
) {
    SUBUH("SUBUH", "Subuh", 2, "04:45", 50, true, "2 Rakaat fardhu di waktu fajar", "subuh"),
    DZUHUR("DZUHUR", "Dzuhur", 4, "12:05", 40, true, "4 Rakaat fardhu di siang hari", "dzuhur"),
    ASHAR("ASHAR", "Ashar", 4, "15:20", 40, true, "4 Rakaat fardhu di sore hari", "ashar"),
    MAGHRIB("MAGHRIB", "Maghrib", 3, "18:05", 45, true, "3 Rakaat fardhu saat matahari terbenam", "maghrib"),
    ISYA("ISYA", "Isya", 4, "19:18", 40, true, "4 Rakaat fardhu di malam hari", "isya"),
    DHUHA("DHUHA", "Dhuha", 2, "07:30", 30, false, "Sunnah pembawa berkah di pagi hari", "dhuha");

    companion object {
        fun fromName(name: String): PrayerType {
            return entries.find { it.idName.equals(name, ignoreCase = true) } ?: DZUHUR
        }
    }
}

enum class MovementPose {
    BERDIRI_NIAT,
    TAKBIRATUL_IHRAM,
    SEDEKAP,
    RUKU,
    ITIDAL,
    SUJUD,
    DUDUK_ANTARA_DUA_SUJUD,
    TASYAHHUD_AWAL,
    TASYAHHUD_AKHIR,
    SALAM
}

data class PrayerStep(
    val stepNumber: Int,
    val title: String,
    val pose: MovementPose,
    val arabicText: String,
    val latinText: String,
    val translation: String,
    val kidTip: String,
    val audioKey: String = ""
)

data class WudhuStep(
    val stepNumber: Int,
    val title: String,
    val arabicText: String,
    val latinText: String,
    val translation: String,
    val description: String,
    val kidTip: String
)
