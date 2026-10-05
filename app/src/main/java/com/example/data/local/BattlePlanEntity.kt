package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.HeatmapTopic
import com.example.data.model.TopicItem

@Entity(tableName = "battle_plans")
data class BattlePlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subject: String,
    val summary: String,
    val createdAt: Long = System.currentTimeMillis(),
    val syllabusUri: String? = null,
    val scorecardUri: String? = null,
    val topics: List<TopicItem>,
    val heatmapTopics: List<HeatmapTopic>
) {
    val totalTopicsCount: Int get() = topics.size
    val completedCount: Int get() = topics.count { it.isCompleted }
    val progressPercent: Float
        get() = if (topics.isNotEmpty()) (completedCount.toFloat() / topics.size.toFloat()) else 0f
    val topTopic: TopicItem? get() = topics.maxByOrNull { it.roiScore }
}
