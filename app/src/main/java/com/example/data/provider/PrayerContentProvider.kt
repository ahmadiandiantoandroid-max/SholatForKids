package com.example.data.provider

import com.example.data.model.MovementPose
import com.example.data.model.PrayerStep
import com.example.data.model.PrayerType
import com.example.data.model.WudhuStep

object PrayerContentProvider {

    fun getStepsForPrayer(prayerType: PrayerType): List<PrayerStep> {
        return listOf(
            PrayerStep(
                stepNumber = 1,
                title = "Niat & Takbiratul Ihram",
                pose = MovementPose.TAKBIRATUL_IHRAM,
                arabicText = when (prayerType) {
                    PrayerType.SUBUH -> "أُصَلِّي فَرْضَ الصُّبْحِ رَكْعَتَيْنِ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلَّهِ تَعَالَى"
                    PrayerType.DZUHUR -> "أُصَلِّي فَرْضَ الظُّهْرِ أَرْبَعَ رَكَعَاتٍ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلَّهِ تَعَالَى"
                    PrayerType.ASHAR -> "أُصَلِّي فَرْضَ الْعَصْرِ أَرْبَعَ رَكَعَاتٍ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلَّهِ تَعَالَى"
                    PrayerType.MAGHRIB -> "أُصَلِّي فَرْضَ الْمَغْرِبِ ثَلَاثَ رَكَعَاتٍ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلَّهِ تَعَالَى"
                    PrayerType.ISYA -> "أُصَلِّي فَرْضَ الْعِشَاءِ أَرْبَعَ رَكَعَاتٍ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلَّهِ تَعَالَى"
                    PrayerType.DHUHA -> "أُصَلِّي سُنَّةَ الضُّحَى رَكْعَتَيْنِ مُسْتَقْبِلَ الْقِبْلَةِ أَدَاءً لِلَّهِ تَعَالَى"
                },
                latinText = when (prayerType) {
                    PrayerType.SUBUH -> "Usholli fardhas-subhi rok'ataini mustaqbilal qiblati adaa-an lillaahi ta'aala. Allaahu Akbar."
                    PrayerType.DZUHUR -> "Usholli fardhaz-zhuhri arba'a roka'aatin mustaqbilal qiblati adaa-an lillaahi ta'aala. Allaahu Akbar."
                    PrayerType.ASHAR -> "Usholli fardhal 'ashri arba'a roka'aatin mustaqbilal qiblati adaa-an lillaahi ta'aala. Allaahu Akbar."
                    PrayerType.MAGHRIB -> "Usholli fardhal maghribi tsalaatsa roka'aatin mustaqbilal qiblati adaa-an lillaahi ta'aala. Allaahu Akbar."
                    PrayerType.ISYA -> "Usholli fardhal 'isyaa-i arba'a roka'aatin mustaqbilal qiblati adaa-an lillaahi ta'aala. Allaahu Akbar."
                    PrayerType.DHUHA -> "Usholli sunnatadh-dhuhaa rok'ataini mustaqbilal qiblati adaa-an lillaahi ta'aala. Allaahu Akbar."
                },
                translation = "Aku berniat sholat fardhu menghadap kiblat karena Allah Ta'ala. Allah Maha Besar.",
                kidTip = "Angkat kedua telapak tangan sejajar telinga (atau bahu), jari terbuka rileks, sambil mengucapkan takbir 'Allahu Akbar' di hati dan lisan!",
                audioKey = "takbir"
            ),
            PrayerStep(
                stepNumber = 2,
                title = "Sedekap & Doa Iftitah",
                pose = MovementPose.SEDEKAP,
                arabicText = "اللَّهُ أَكْبَرُ كَبِيرًا وَالْحَمْدُ لِلَّهِ كَثِيرًا وَسُبْحَانَ اللَّهِ بُكْرَةً وَأَصِيلاً. إِنِّي وَجَّهْتُ وَجْهِيَ لِلَّذِي فَطَرَ السَّمَاوَاتِ وَالأَرْضَ حَنِيفًا مُسْلِمًا وَمَا أَنَا مِنَ الْمُشْرِكِينَ",
                latinText = "Allaahu akbar kabiiraa wal hamdu lillaahi katsiiraa, wa subhaanallaahi bukrataw-wa ashiilaa. Inni wajjahtu wajhiya lilladzii fatharas-samaawaati wal ardha haniifam-muslimaw-wamaa ana minal musyrikiin.",
                translation = "Allah Maha Besar sebesar-besarnya. Segala puji bagi Allah sebanyak-banyaknya. Maha Suci Allah di waktu pagi dan petang. Sesungguhnya aku hadapkan wajahku kepada Dzat yang menciptakan langit dan bumi...",
                kidTip = "Letakkan tangan kanan di atas punggung tangan kiri di atas dada atau bawah dada. Pandangan mata lurus ke arah tempat sujud ya!",
                audioKey = "iftitah"
            ),
            PrayerStep(
                stepNumber = 3,
                title = "Membaca Surat Al-Fatihah",
                pose = MovementPose.SEDEKAP,
                arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ﴿١﴾ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ﴿٢﴾ الرَّحْمَٰنِ الرَّحِيمِ ﴿٣﴾ مَالِكِ يَوْمِ الدِّينِ ﴿٤﴾ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ﴿٥﴾ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ﴿٦﴾ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ ﴿٧﴾",
                latinText = "Bismillaahir-rahmaanir-rahiim. Alhamdulillaahi rabbil 'aalamiin. Ar-rahmaanir-rahiim. Maaliki yawmid-diin. Iyyaaka na'budu wa iyyaaka nasta'iin. Ihdinash-shiraathal mustaqiim. Shiraathal ladziina an'amta 'alaihim ghairil maghdhuubi 'alaihim waladh-dhaalliin. Aamiin.",
                translation = "Dengan nama Allah Yang Maha Pengasih lagi Maha Penyayang. Segala puji bagi Allah, Tuhan semesta alam...",
                kidTip = "Membaca Al-Fatihah adalah rukun sholat yang wajib dibaca di setiap rakaat. Baca dengan tenang dan perlahan (tartil) ya adik-adik!",
                audioKey = "fatihah"
            ),
            PrayerStep(
                stepNumber = 4,
                title = "Ruku' dengan Tuma'ninah",
                pose = MovementPose.RUKU,
                arabicText = "سُبْحَانَ رَبِّيَ الْعَظِيمِ وَبِحَمْدِهِ",
                latinText = "Subhaana robbiyal 'azhiimi wa bihamdih (Dibaca 3 kali)",
                translation = "Maha Suci Tuhanku Yang Maha Agung dan dengan memuji-Nya.",
                kidTip = "Bungkukkan badan membentuk sudut 90 derajat, punggung lurus seperti papan datar, dan kedua telapak tangan memegang lutut!",
                audioKey = "ruku"
            ),
            PrayerStep(
                stepNumber = 5,
                title = "I'tidal (Bangkit dari Ruku')",
                pose = MovementPose.ITIDAL,
                arabicText = "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ\nرَبَّنَا لَكَ الْحَمْدُ مِلْءَ السَّمَاوَاتِ وَمِلْءَ الأَرْضِ وَمِلْءَ مَا شِئْتَ مِنْ شَيْءٍ بَعْدُ",
                latinText = "Sami'allaahu liman hamidah. Robbanaa lakal hamdu mil'us-samaawaati wa mil'ul ardhi wa mil'u maa syi'ta min syai-in ba'du.",
                translation = "Allah mendengar orang yang memuji-Nya. Ya Tuhan kami, bagi-Mu lah segala puji sepenuh langit dan sepenuh bumi...",
                kidTip = "Berdiri tegak kembali dengan tenang (tuma'ninah). Tangan kembali lurus ke bawah di samping badan atau bersedekap.",
                audioKey = "itidal"
            ),
            PrayerStep(
                stepNumber = 6,
                title = "Sujud Pertama & Kedua",
                pose = MovementPose.SUJU,
                arabicText = "سُبْحَانَ رَبِّيَ الأَعْلَى وَبِحَمْدِهِ",
                latinText = "Subhaana robbiyal a'laa wa bihamdih (Dibaca 3 kali)",
                translation = "Maha Suci Tuhanku Yang Maha Tinggi dan dengan memuji-Nya.",
                kidTip = "Pastikan 7 anggota badan menempel: dahi & hidung, kedua telapak tangan, kedua lutut, dan ujung jari jemari kedua kaki!",
                audioKey = "sujud"
            ),
            PrayerStep(
                stepNumber = 7,
                title = "Duduk Antara Dua Sujud",
                pose = MovementPose.DUDUK_ANTARA_DUA_SUJUD,
                arabicText = "رَبِّ اغْفِرْ لِي وَارْحَمْنِي وَاجْبُرْنِي وَارْفَعْنِي وَارْزُقْنِي وَاهْدِنِي وَعَافِنِي وَاعْفُ عَنِّي",
                latinText = "Robbighfirlii warhamnii wajburnii warfa'nii warzuqnii wahdinii wa'aafinii wa'fu 'annii.",
                translation = "Ya Tuhanku, ampunilah aku, sayangilah aku, cukupkanlah kekuranganku, tinggikanlah derajatku, berilah aku rezeki, petunjuk, kesehatan, dan ampunan.",
                kidTip = "Duduk di atas kaki kiri dan tegakkan telapak kaki kanan (duduk iftirasy). Letakkan tangan di atas paha dekat lutut.",
                audioKey = "duduk"
            ),
            PrayerStep(
                stepNumber = 8,
                title = if (prayerType.rakaat > 2) "Tasyahhud Akhir & Sholawat" else "Tasyahhud & Doa Akhir",
                pose = MovementPose.TASYAHHUD_AKHIR,
                arabicText = "التَّحِيَّاتُ الْمُبَارَكَاتُ الصَّلَوَاتُ الطَّيِّبَاتُ لِلَّهِ. السَّلاَمُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ. السَّلاَمُ عَلَيْنَا وَعَلَى عِبَادِ اللَّهِ الصَّالِحِينَ. أَشْهَدُ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا رَسُولُ اللَّهِ. اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ...",
                latinText = "Attahiyyaatul mubaarakaatush-shalawaatuth-thayyibaatu lillaah. Assalaamu 'alaika ayyuhan-nabiyyu wa rahmatullaahi wa barakaatuh. Assalaamu 'alainaa wa 'alaa 'ibaadillaahish-shaalihiin. Asyhadu allaa ilaaha illallaah, wa asyhadu anna Muhammadar Rasuulullaah. Allaahumma shalli 'alaa Muhammad wa 'alaa aali Muhammad...",
                translation = "Segala kehormatan, keberkahan, kebahagiaan dan kebaikan bagi Allah. Salam, rahmat dan berkah-Nya semoga tercurah kepadamu wahai Nabi...",
                kidTip = "Saat mengucapkan syahadat 'Asyhadu allaa ilaaha illallaah', angkat jari telunjuk tangan kanan lurus mengarah ke kiblat!",
                audioKey = "tasyahhud"
            ),
            PrayerStep(
                stepNumber = 9,
                title = "Salam ke Kanan & ke Kiri",
                pose = MovementPose.SALAM,
                arabicText = "السَّلاَمُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
                latinText = "Assalaamu 'alaikum warahmatullaah",
                translation = "Semoga keselamatan dan rahmat Allah terlimpah kepadamu.",
                kidTip = "Palingkan wajah ke kanan sampai pipi terlihat dari belakang, lalu palingkan ke kiri sambil mengucapkan salam. Selesai dan alhamdulillah!",
                audioKey = "salam"
            )
        )
    }

    fun getWudhuSteps(): List<WudhuStep> {
        return listOf(
            WudhuStep(
                stepNumber = 1,
                title = "Membaca Bismillah & Cuci Tangan",
                arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                latinText = "Bismillaahir-rahmaanir-rahiim",
                translation = "Dengan nama Allah Yang Maha Pengasih lagi Maha Penyayang",
                description = "Membasuh kedua telapak tangan hingga pergelangan sebanyak 3 kali sambil membersihkan sela-sela jari.",
                kidTip = "Gosok sela-sela jari tangan dengan bersih ya!"
            ),
            WudhuStep(
                stepNumber = 2,
                title = "Berkumur-kumur",
                arabicText = "اللَّهُمَّ أَعِنِّي عَلَى تِلاَوَةِ كِتَابِكَ وَكَثْرَةِ الذِّكْرِ لَكَ",
                latinText = "Allahumma a'inni 'ala tilawati kitabik",
                translation = "Ya Allah, tolonglah aku untuk rajin membaca kitab-Mu dan berdzikir kepada-Mu",
                description = "Memasukkan air ke dalam mulut lalu berkumur dan mengeluarkannya sebanyak 3 kali.",
                kidTip = "Kumur secara merata agar sisa makanan di mulut bersih!"
            ),
            WudhuStep(
                stepNumber = 3,
                title = "Membersihkan Lubang Hidung",
                arabicText = "اللَّهُمَّ أَرِحْنِي رَائِحَةَ الْجَنَّةِ",
                latinText = "Istinsyaq wa Istintsar",
                translation = "Menghirup air ke hidung lalu mengeluarkannya",
                description = "Menghirup sedikit air ke dalam rongga hidung lalu menghembuskannya keluar sebanyak 3 kali.",
                kidTip = "Hembuskan air dari hidung dengan tangan kiri!"
            ),
            WudhuStep(
                stepNumber = 4,
                title = "Niat & Membasuh Wajah",
                arabicText = "نَوَيْتُ الْوُضُوءَ لِرَفْعِ الْحَدَثِ الأَصْغَرِ فَرْضًا لِلَّهِ تَعَالَى",
                latinText = "Nawaitul wudhuu-a lirof'il hadatsil ashghari fardhal lillaahi ta'aalaa",
                translation = "Aku berniat wudhu untuk menghilangkan hadats kecil, fardhu karena Allah Ta'ala.",
                description = "Membasuh seluruh permukaan wajah dari tumbuhnya rambut di dahi hingga bawah dagu dan dari telinga ke telinga sebanyak 3 kali.",
                kidTip = "Pastikan seluruh area wajah dari kening sampai dagu terkena air!"
            ),
            WudhuStep(
                stepNumber = 5,
                title = "Membasuh Tangan Hingga Siku",
                arabicText = "اللَّهُمَّ أَعْطِنِي كِتَابِي بِيَمِينِي",
                latinText = "Membasuh tangan kanan lalu tangan kiri hingga siku",
                translation = "Membasuh kedua lengan hingga siku secara sempurna",
                description = "Membasuh tangan kanan dari ujung jari hingga melebihi siku 3 kali, dilanjutkan tangan kiri 3 kali.",
                kidTip = "Dahulukan tangan kanan, lalu gosok hingga siku terbasahi sempurna!"
            ),
            WudhuStep(
                stepNumber = 6,
                title = "Mengusap Sebagian Kepala / Rambut",
                arabicText = "اللَّهُمَّ حَرِّمْ شَعْرِي وَبَشَرِي عَلَى النَّارِ",
                latinText = "Mengusap ubun-ubun atau kepala dengan air",
                translation = "Membasahi tangan lalu mengusapkannya ke rambut/kepala",
                description = "Membasahi kedua tangan lalu mengusapkan ke bagian depan kepala hingga ubun-ubun sebanyak 3 kali.",
                kidTip = "Cukup usapkan tangan yang basah ke rambut atau kepala!"
            ),
            WudhuStep(
                stepNumber = 7,
                title = "Membasuh Kedua Telinga",
                arabicText = "اللَّهُمَّ اجْعَلْنِي مِنَ الَّذِينَ يَسْتَمِعُونَ الْقَوْلَ فَيَتَّبِعُونَ أَحْسَنَهُ",
                latinText = "Mengusap daun dan lubang telinga",
                translation = "Membersihkan bagian luar dan dalam kedua telinga",
                description = "Memasukkan jari telunjuk ke rongga telinga dan ibu jari mengusap bagian belakang daun telinga sebanyak 3 kali.",
                kidTip = "Telunjuk masuk ke dalam telinga, jempol memutar di belakang daun telinga!"
            ),
            WudhuStep(
                stepNumber = 8,
                title = "Membasuh Kaki Hingga Mata Kaki",
                arabicText = "اللَّهُمَّ ثَبِّتْ قَدَمَيَّ عَلَى الصِّرَاطِ يَوْمَ تَزِلُّ فِيهِ الأَقْدَامُ",
                latinText = "Membasuh kaki kanan lalu kaki kiri hingga mata kaki",
                translation = "Membersihkan kedua kaki beserta sela-sela jari kaki",
                description = "Membasuh kaki kanan hingga di atas mata kaki sebanyak 3 kali, bersihkan sela jari dengan jari kelingking, lalu ulangi pada kaki kiri.",
                kidTip = "Jangan lupa bersihkan tumit dan sela jari kaki ya!"
            ),
            WudhuStep(
                stepNumber = 9,
                title = "Doa Setelah Wudhu",
                arabicText = "أَشْهَدُ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ. اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ وَاجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ",
                latinText = "Asyhadu allaa ilaaha illallaahu wahdahu laa syariikalah, wa asyhadu anna Muhammadan 'abduhuu wa rasuuluh. Allaahummaj'alnii minat-tawwaabiina waj'alnii minal mutathahhiriin.",
                translation = "Aku bersaksi tiada tuhan selain Allah yang Maha Esa tiada sekutu bagi-Nya, dan Muhammad hamba serta utusan-Nya. Ya Allah jadikanlah aku termasuk golongan orang yang bertaubat dan menyucikan diri.",
                kidTip = "Menghadap kiblat dan menengadahkan kedua tangan saat berdoa. Pintu surga terbuka untuk yang membacanya!",
            )
        )
    }

    data class QuizQuestion(
        val question: String,
        val options: List<String>,
        val correctIndex: Int,
        val explanation: String,
        val xpBonus: Int = 20
    )

    val dailyQuizzes = listOf(
        QuizQuestion(
            question = "Berapa jumlah rakaat sholat Subuh?",
            options = ["2 Rakaat", "3 Rakaat", "4 Rakaat", "1 Rakaat"],
            correctIndex = 0,
            explanation = "Hebat! Sholat Subuh berjumlah 2 rakaat yang dikerjakan sebelum terbit matahari."
        ),
        QuizQuestion(
            question = "Apa bacaan saat kita berada dalam posisi Ruku'?",
            options = [
                "Subhaana robbiyal a'laa wa bihamdih",
                "Subhaana robbiyal 'azhiimi wa bihamdih",
                "Rabbighfirlii warhamnii",
                "Samiallahu liman hamidah"
            ],
            correctIndex = 1,
            explanation = "Pintar sekali! Bacaan ruku adalah Subhaana robbiyal 'azhiimi wa bihamdih 3x."
        ),
        QuizQuestion(
            question = "Berapa jumlah rakaat sholat Maghrib?",
            options = ["2 Rakaat", "3 Rakaat", "4 Rakaat", "5 Rakaat"],
            correctIndex = 1,
            explanation = "Tepat! Sholat Maghrib memiliki 3 rakaat saat matahari terbenam."
        ),
        QuizQuestion(
            question = "Gerakan menempelkan dahi, hidung, telapak tangan, lutut, dan jari kaki dinamakan apa?",
            options = ["Ruku'", "I'tidal", "Sujud", "Duduk Iftirasy"],
            correctIndex = 2,
            explanation = "MasyaAllah benar! Itu adalah gerakan Sujud, posisi paling dekat seorang hamba dengan Allah."
        ),
        QuizQuestion(
            question = "Apa rukun sholat yang wajib dibaca pada setiap rakaat?",
            options = ["Surat An-Nas", "Surat Al-Fatihah", "Doa Iftitah", "Surat Al-Ikhlas"],
            correctIndex = 1,
            explanation = "Benar! Surat Al-Fatihah wajib dibaca di setiap rakaat sholat."
        )
    )
}
