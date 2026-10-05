package com.example.data.repository

import com.example.data.local.BattlePlanDao
import com.example.data.local.BattlePlanEntity
import kotlinx.coroutines.flow.Flow

class BattlePlanRepository(private val dao: BattlePlanDao) {
    val allPlans: Flow<List<BattlePlanEntity>> = dao.getAllBattlePlans()
    val latestPlan: Flow<BattlePlanEntity?> = dao.getLatestBattlePlan()

    suspend fun getPlanById(id: Long): BattlePlanEntity? = dao.getBattlePlanById(id)

    suspend fun savePlan(plan: BattlePlanEntity): Long = dao.insertBattlePlan(plan)

    suspend fun updatePlan(plan: BattlePlanEntity) = dao.updateBattlePlan(plan)

    suspend fun deletePlan(id: Long) = dao.deleteBattlePlanById(id)
}
