package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.HeatmapTopic
import com.example.data.model.TopicItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val topicListType = Types.newParameterizedType(List::class.java, TopicItem::class.java)
    private val topicAdapter = moshi.adapter<List<TopicItem>>(topicListType)

    private val heatmapListType = Types.newParameterizedType(List::class.java, HeatmapTopic::class.java)
    private val heatmapAdapter = moshi.adapter<List<HeatmapTopic>>(heatmapListType)

    @TypeConverter
    fun fromTopicList(value: List<TopicItem>?): String {
        return topicAdapter.toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toTopicList(value: String?): List<TopicItem> {
        if (value.isNullOrEmpty()) return emptyList()
        return try {
            topicAdapter.fromJson(value) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromHeatmapList(value: List<HeatmapTopic>?): String {
        return heatmapAdapter.toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toHeatmapList(value: String?): List<HeatmapTopic> {
        if (value.isNullOrEmpty()) return emptyList()
        return try {
            heatmapAdapter.fromJson(value) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
