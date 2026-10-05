package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.sample.SamplePresets
import com.example.data.sample.StudyPreset
import com.example.ui.components.TacticalTopAppBar
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricCyanSubtle
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenSubtle
import com.example.ui.theme.TacticalAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ScanUploadScreen(
    syllabusBitmap: Bitmap?,
    scorecardBitmap: Bitmap?,
    selectedSubject: String,
    onSubjectChange: (String) -> Unit,
    onSyllabusSelected: (android.net.Uri) -> Unit,
    onScorecardSelected: (android.net.Uri) -> Unit,
    onLoadSimulatedSyllabus: () -> Unit,
    onLoadSimulatedScorecard: () -> Unit,
    onClearSyllabus: () -> Unit,
    onClearScorecard: () -> Unit,
    onLoadPreset: (StudyPreset) -> Unit,
    onGeneratePlan: () -> Unit,
    onBackClick: () -> Unit,
    isDesktopMode: Boolean,
    onToggleDesktop: () -> Unit
) {
    BackHandler { onBackClick() }

    val syllabusPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onSyllabusSelected(uri)
    }

    val scorecardPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onScorecardSelected(uri)
    }

    val subjects = listOf("Physics", "Computer Science", "Chemistry", "Mathematics", "Biology")

    Scaffold(
        topBar = {
            TacticalTopAppBar(
                title = "SCAN MATERIALS",
                showBackButton = true,
                onBackClick = onBackClick,
                isDesktopMode = isDesktopMode,
                onToggleDesktop = onToggleDesktop
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header instruction
            Column {
                Text(
                    text = "Upload Study Intelligence",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gemini Vision analyzes syllabus chapters and extracts student mistake patterns from your mock scorecard to compute high-ROI focus areas.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                )
            }

            // Subject Selector Chips
            Column {
                Text(
                    text = "TARGET SUBJECT / DOMAIN",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    subjects.take(3).forEach { subject ->
                        val isSelected = selectedSubject.equals(subject, ignoreCase = true)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) NeonGreen else DarkSurfaceBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onSubjectChange(subject) }
                                .testTag("subject_chip_$subject"),
                            color = if (isSelected) NeonGreenSubtle else DarkSurfaceVariant
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = subject,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                        color = if (isSelected) NeonGreen else TextSecondary,
                                        fontSize = 12.sp
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Upload Area 1: Syllabus Image
            UploadCard(
                title = "1. SYLLABUS IMAGE",
                subtitle = "Official syllabus breakdown, topic weights, or chapter list",
                icon = Icons.Default.Description,
                bitmap = syllabusBitmap,
                badgeColor = ElectricCyan,
                badgeText = "SYLLABUS",
                onPickImage = {
                    syllabusPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onSimulateScan = onLoadSimulatedSyllabus,
                onClear = onClearSyllabus,
                testTagPrefix = "syllabus"
            )

            // Upload Area 2: Mock Scorecard Image
            UploadCard(
                title = "2. MOCK SCORECARD IMAGE",
                subtitle = "Recent test scorecard, marks report, or mistake audit sheet",
                icon = Icons.Default.Scoreboard,
                bitmap = scorecardBitmap,
                badgeColor = TacticalAmber,
                badgeText = "SCORECARD",
                onPickImage = {
                    scorecardPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onSimulateScan = onLoadSimulatedScorecard,
                onClear = onClearScorecard,
                testTagPrefix = "scorecard"
            )

            // Instant Preset Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp)),
                color = DarkSurfaceVariant
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TRY REALISTIC EXAM SAMPLES",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ElectricCyan,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                        Text(
                            text = "Instant test",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onLoadPreset(SamplePresets.physicsPreset) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_sample_physics"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "Load Physics JEE",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = ElectricCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        OutlinedButton(
                            onClick = { onLoadPreset(SamplePresets.csPreset) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_sample_cs"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "Load CS Midterm",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = NeonGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            // Big CTA: Generate Battle Plan
            Button(
                onClick = onGeneratePlan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .testTag("generate_battle_plan_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreen,
                    contentColor = DarkBackground
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = DarkBackground
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GENERATE BATTLE PLAN",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun UploadCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bitmap: Bitmap?,
    badgeColor: Color,
    badgeText: String,
    onPickImage: () -> Unit,
    onSimulateScan: () -> Unit,
    onClear: () -> Unit,
    testTagPrefix: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (bitmap != null) badgeColor.copy(alpha = 0.6f) else DarkSurfaceBorder,
                RoundedCornerShape(16.dp)
            )
            .testTag("${testTagPrefix}_upload_card"),
        color = DarkSurface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                }

                if (bitmap != null) {
                    IconButton(
                        onClick = onClear,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (bitmap != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Document preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = DarkBackground.copy(alpha = 0.85f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "High Resolution Scanned",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tap to choose or scan
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
                            .clickable { onSimulateScan() }
                            .testTag("${testTagPrefix}_pick_button"),
                        color = DarkSurfaceVariant
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(badgeColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DocumentScanner,
                                    contentDescription = null,
                                    tint = badgeColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Scan Realistic $badgeText Document",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Instant 1-tap academic sample scan",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = badgeColor.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "SCAN NOW",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = badgeColor,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }

                    // Secondary option: pick from device files
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPickImage() }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Or choose file from device gallery",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
