package com.example.data.model

data class StudyStep(
    val timeRange: String,
    val title: String,
    val description: String,
    val actionType: String = "PRACTICE", // "CONCEPT", "PRACTICE", "ANALYSIS"
    val isCompleted: Boolean = false
)

data class TopicItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val topicName: String,
    val subject: String = "General",
    val whyChosen: String,
    val examWeightage: Int, // 1 to 10
    val weaknessScore: Int, // 1 to 10
    val estimatedHours: Double, // e.g. 2.0
    val roiScore: Double, // (Weightage * Weakness) / Estimated Hours
    val studyPlan2Hours: List<StudyStep> = emptyList(),
    val isCompleted: Boolean = false
) {
    // Formatted ROI display
    val formattedRoi: String
        get() = String.format(java.util.Locale.US, "%.1f", roiScore)
}

enum class HeatmapStatus {
    CRITICAL_RED,
    MODERATE_YELLOW,
    MASTERED_GREEN
}

data class HeatmapTopic(
    val name: String,
    val subject: String,
    val weightage: Int,
    val weakness: Int,
    val status: HeatmapStatus,
    val mockAccuracy: Int, // e.g. 32%
    val roiScore: Double
)

data class BattlePlanPayload(
    val title: String,
    val subject: String,
    val summary: String,
    val topics: List<TopicItem>,
    val heatmapTopics: List<HeatmapTopic>
)
