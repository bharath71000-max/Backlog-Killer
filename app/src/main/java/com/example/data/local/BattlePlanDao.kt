package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BattlePlanDao {
    @Query("SELECT * FROM battle_plans ORDER BY createdAt DESC")
    fun getAllBattlePlans(): Flow<List<BattlePlanEntity>>

    @Query("SELECT * FROM battle_plans WHERE id = :id LIMIT 1")
    suspend fun getBattlePlanById(id: Long): BattlePlanEntity?

    @Query("SELECT * FROM battle_plans ORDER BY createdAt DESC LIMIT 1")
    fun getLatestBattlePlan(): Flow<BattlePlanEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBattlePlan(plan: BattlePlanEntity): Long

    @Update
    suspend fun updateBattlePlan(plan: BattlePlanEntity)

    @Query("DELETE FROM battle_plans WHERE id = :id")
    suspend fun deleteBattlePlanById(id: Long)
}
