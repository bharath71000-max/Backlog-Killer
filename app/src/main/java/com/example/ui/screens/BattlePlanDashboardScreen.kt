package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BattlePlanEntity
import com.example.data.model.StudyStep
import com.example.data.model.TopicItem
import com.example.ui.components.HapticUtil
import com.example.ui.components.RoiBadge
import com.example.ui.components.RoiGauge
import com.example.ui.components.TacticalTopAppBar
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CrimsonRedSubtle
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricCyanSubtle
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenSubtle
import com.example.ui.theme.TacticalAmber
import com.example.ui.theme.TacticalAmberSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.TopicSortFilter

@Composable
fun BattlePlanDashboardScreen(
    plan: BattlePlanEntity?,
    onTopicToggle: (String) -> Unit,
    onStepToggle: (String, Int) -> Unit,
    onStartTimer: (TopicItem) -> Unit,
    onBackClick: () -> Unit,
    sortFilter: TopicSortFilter,
    onFilterChange: (TopicSortFilter) -> Unit,
    isDesktopMode: Boolean,
    onToggleDesktop: () -> Unit,
    onOpenVoiceCoach: () -> Unit
) {
    BackHandler { onBackClick() }

    if (plan == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground),
            contentAlignment = Alignment.Center
        ) {
            Text("No active battle plan found", color = TextPrimary)
        }
        return
    }

    // If Desktop Mode is active, show the DesktopOfficeKitView
    if (isDesktopMode) {
        DesktopOfficeKitView(
            plan = plan,
            onTopicToggle = onTopicToggle,
            onStartTimer = onStartTimer,
            onCloseDesktop = onToggleDesktop
        )
        return
    }

    val context = LocalContext.current

    // Apply sorting & filtering
    val displayTopics = remember(plan.topics, sortFilter) {
        val list = when (sortFilter) {
            TopicSortFilter.BY_ROI -> plan.topics.sortedByDescending { it.roiScore }
            TopicSortFilter.BY_WEIGHTAGE -> plan.topics.sortedByDescending { it.examWeightage }
            TopicSortFilter.BY_WEAKNESS -> plan.topics.sortedByDescending { it.weaknessScore }
            TopicSortFilter.ACTIVE_ONLY -> plan.topics.filter { !it.isCompleted }
        }
        list
    }

    val numberOneTopic = displayTopics.firstOrNull()
    val otherTopics = if (displayTopics.size > 1) displayTopics.drop(1) else emptyList()

    Scaffold(
        topBar = {
            TacticalTopAppBar(
                title = plan.subject.uppercase(),
                showBackButton = true,
                onBackClick = onBackClick,
                isDesktopMode = isDesktopMode,
                onToggleDesktop = onToggleDesktop,
                onOpenVoiceCoach = onOpenVoiceCoach
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("battle_plan_dashboard"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Diagnosis Summary Banner
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp)),
                    color = DarkSurface
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = plan.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NeonGreenSubtle
                            ) {
                                Text(
                                    text = "${plan.completedCount}/${plan.totalTopicsCount} KILLED",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = NeonGreen
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = plan.summary,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { plan.progressPercent },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NeonGreen,
                            trackColor = DarkSurfaceVariant
                        )
                    }
                }
            }

            // Filter & Sort Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SORT:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    )

                    FilterChip(
                        label = "Highest ROI",
                        isSelected = sortFilter == TopicSortFilter.BY_ROI,
                        onClick = { onFilterChange(TopicSortFilter.BY_ROI) },
                        testTag = "filter_roi"
                    )
                    FilterChip(
                        label = "Weightage",
                        isSelected = sortFilter == TopicSortFilter.BY_WEIGHTAGE,
                        onClick = { onFilterChange(TopicSortFilter.BY_WEIGHTAGE) },
                        testTag = "filter_weightage"
                    )
                    FilterChip(
                        label = "Uncrushed Only",
                        isSelected = sortFilter == TopicSortFilter.ACTIVE_ONLY,
                        onClick = { onFilterChange(TopicSortFilter.ACTIVE_ONLY) },
                        testTag = "filter_active"
                    )
                }
            }

            // #1 Priority Topic Highlight (Hero Card)
            if (numberOneTopic != null) {
                item {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TOP KILL TARGET (#1 PRIORITY)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = NeonGreen,
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        PriorityOneHeroCard(
                            topic = numberOneTopic,
                            onTopicToggle = { onTopicToggle(numberOneTopic.id) },
                            onStepToggle = { stepIdx -> onStepToggle(numberOneTopic.id, stepIdx) },
                            onStartTimer = { onStartTimer(numberOneTopic) }
                        )
                    }
                }
            }

            // Priority Queue Header
            if (otherTopics.isNotEmpty()) {
                item {
                    Text(
                        text = "REMAINING PRIORITY QUEUE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 1.sp
                        )
                    )
                }

                itemsIndexed(otherTopics, key = { _, t -> t.id }) { index, topic ->
                    PriorityQueueItemCard(
                        rank = index + 2,
                        topic = topic,
                        onTopicToggle = { onTopicToggle(topic.id) },
                        onStepToggle = { stepIdx -> onStepToggle(topic.id, stepIdx) },
                        onStartTimer = { onStartTimer(topic) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PriorityOneHeroCard(
    topic: TopicItem,
    onTopicToggle: () -> Unit,
    onStepToggle: (Int) -> Unit,
    onStartTimer: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, NeonGreen.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .testTag("priority_one_card"),
        color = DarkSurface
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = NeonGreenSubtle
                    ) {
                        Text(
                            text = "MAXIMUM SCORE IMPACT",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = NeonGreen,
                                fontSize = 10.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = topic.topicName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            textDecoration = if (topic.isCompleted) TextDecoration.LineThrough else null
                        ),
                        color = if (topic.isCompleted) TextMuted else TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                RoiGauge(score = topic.roiScore, size = 68.dp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = topic.whyChosen,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Weightage & Weakness Metric Meters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceVariant)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MetricItem(label = "EXAM WEIGHT", value = "${topic.examWeightage}/10", color = ElectricCyan)
                MetricItem(label = "WEAKNESS", value = "${topic.weaknessScore}/10", color = CrimsonRed)
                MetricItem(label = "EST HOURS", value = "${topic.estimatedHours}h", color = TacticalAmber)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Expandable 2-Hour Study Plan
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "2-HOUR TACTICAL STRIKE PLAN",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = ElectricCyan,
                        letterSpacing = 0.5.sp
                    )
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = ElectricCyan
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    topic.studyPlan2Hours.forEachIndexed { idx, step ->
                        StepItemRow(
                            step = step,
                            onToggle = { onStepToggle(idx) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onStartTimer,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("priority_one_timer_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Start 2h Timer",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                    )
                }

                Button(
                    onClick = {
                        HapticUtil.trigger(context)
                        onTopicToggle()
                    },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(48.dp)
                        .testTag("priority_one_checkoff_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (topic.isCompleted) DarkSurfaceVariant else NeonGreen,
                        contentColor = if (topic.isCompleted) TextMuted else DarkBackground
                    )
                ) {
                    Icon(
                        imageVector = if (topic.isCompleted) Icons.Default.CheckCircle else Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (topic.isCompleted) "Crushed (Undo)" else "Check Off Topic",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PriorityQueueItemCard(
    rank: Int,
    topic: TopicItem,
    onTopicToggle: () -> Unit,
    onStepToggle: (Int) -> Unit,
    onStartTimer: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp))
            .testTag("priority_card_$rank"),
        color = DarkSurface
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = DarkSurfaceVariant
                    ) {
                        Text(
                            text = "#$rank",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = topic.topicName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textDecoration = if (topic.isCompleted) TextDecoration.LineThrough else null
                        ),
                        color = if (topic.isCompleted) TextMuted else TextPrimary,
                        maxLines = 1
                    )
                }
                RoiBadge(score = topic.roiScore)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = topic.whyChosen,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Expand toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Hide 2h Plan" else "View 2h Study Plan (${topic.studyPlan2Hours.size} steps)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = ElectricCyan,
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    topic.studyPlan2Hours.forEachIndexed { idx, step ->
                        StepItemRow(
                            step = step,
                            onToggle = { onStepToggle(idx) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onStartTimer,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Timer", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        HapticUtil.trigger(context)
                        onTopicToggle()
                    },
                    modifier = Modifier
                        .weight(1.4f)
                        .height(38.dp)
                        .testTag("checkoff_button_$rank"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (topic.isCompleted) DarkSurfaceVariant else NeonGreen,
                        contentColor = if (topic.isCompleted) TextMuted else DarkBackground
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (topic.isCompleted) "Done" else "Check Off",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun StepItemRow(
    step: StudyStep,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onToggle() },
        color = DarkSurfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = step.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = NeonGreen,
                    checkmarkColor = DarkBackground,
                    uncheckedColor = DarkSurfaceBorder
                ),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = step.timeRange,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textDecoration = if (step.isCompleted) TextDecoration.LineThrough else null
                        ),
                        color = if (step.isCompleted) TextMuted else TextPrimary,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                fontSize = 9.sp
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                color = color
            )
        )
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                1.dp,
                if (isSelected) NeonGreen else DarkSurfaceBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .testTag(testTag),
        color = if (isSelected) NeonGreenSubtle else DarkSurfaceVariant
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                color = if (isSelected) NeonGreen else TextSecondary,
                fontSize = 11.sp
            )
        )
    }
}
