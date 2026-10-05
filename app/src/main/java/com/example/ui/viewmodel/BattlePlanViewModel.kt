package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiVisionService
import com.example.data.api.LiveVoiceService
import com.example.data.api.LiveVoiceState
import com.example.data.api.VoiceMessage
import com.example.data.local.AppDatabase
import com.example.data.local.BattlePlanEntity
import com.example.data.model.BattlePlanPayload
import com.example.data.model.TopicItem
import com.example.data.repository.BattlePlanRepository
import com.example.data.sample.SamplePresets
import com.example.data.sample.StudyPreset
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.InputStream

enum class AppScreen {
    HOME,
    SCAN_UPLOAD,
    PROCESSING,
    BATTLE_PLAN_DASHBOARD
}

enum class TopicSortFilter {
    BY_ROI,
    BY_WEIGHTAGE,
    BY_WEAKNESS,
    ACTIVE_ONLY
}

data class FocusTimerState(
    val isVisible: Boolean = false,
    val topicName: String = "",
    val totalSeconds: Int = 120 * 60, // 2 hours by default
    val remainingSeconds: Int = 120 * 60,
    val isRunning: Boolean = false,
    val currentStepIndex: Int = 0
) {
    val progress: Float
        get() = if (totalSeconds > 0) (totalSeconds - remainingSeconds).toFloat() / totalSeconds.toFloat() else 0f

    val formattedTime: String
        get() {
            val hours = remainingSeconds / 3600
            val minutes = (remainingSeconds % 3600) / 60
            val seconds = remainingSeconds % 60
            return if (hours > 0) {
                String.format(java.util.Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format(java.util.Locale.US, "%02d:%02d:%02d", 0, minutes, seconds)
            }
        }
}

class BattlePlanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BattlePlanRepository
    private val geminiService = GeminiVisionService()
    val liveVoiceService = LiveVoiceService(application)

    init {
        val db = AppDatabase.getInstance(application)
        repository = BattlePlanRepository(db.battlePlanDao())
    }

    val allSavedPlans: StateFlow<List<BattlePlanEntity>> = repository.allPlans
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _activePlan = MutableStateFlow<BattlePlanEntity?>(null)
    val activePlan: StateFlow<BattlePlanEntity?> = _activePlan.asStateFlow()

    // Upload & Scan state
    private val _syllabusBitmap = MutableStateFlow<Bitmap?>(null)
    val syllabusBitmap: StateFlow<Bitmap?> = _syllabusBitmap.asStateFlow()

    private val _scorecardBitmap = MutableStateFlow<Bitmap?>(null)
    val scorecardBitmap: StateFlow<Bitmap?> = _scorecardBitmap.asStateFlow()

    private val _selectedSubject = MutableStateFlow("Physics")
    val selectedSubject: StateFlow<String> = _selectedSubject.asStateFlow()

    // Processing animation progress
    private val _processingStep = MutableStateFlow("Initializing Academic Vision Engine...")
    val processingStep: StateFlow<String> = _processingStep.asStateFlow()

    private val _processingProgress = MutableStateFlow(0.1f)
    val processingProgress: StateFlow<Float> = _processingProgress.asStateFlow()

    // Desktop Mode (iQOO Office Kit simulation)
    private val _isDesktopMode = MutableStateFlow(false)
    val isDesktopMode: StateFlow<Boolean> = _isDesktopMode.asStateFlow()

    // Topic Filter & Sort
    private val _sortFilter = MutableStateFlow(TopicSortFilter.BY_ROI)
    val sortFilter: StateFlow<TopicSortFilter> = _sortFilter.asStateFlow()

    // Focus Study Timer
    private val _timerState = MutableStateFlow(FocusTimerState())
    val timerState: StateFlow<FocusTimerState> = _timerState.asStateFlow()

    // Live Voice Coach State
    private val _isLiveVoiceOpen = MutableStateFlow(false)
    val isLiveVoiceOpen: StateFlow<Boolean> = _isLiveVoiceOpen.asStateFlow()

    val voiceState: StateFlow<LiveVoiceState> = liveVoiceService.voiceState
    val voiceMessages: StateFlow<List<VoiceMessage>> = liveVoiceService.messages

    private var timerJob: Job? = null

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun toggleDesktopMode() {
        _isDesktopMode.value = !_isDesktopMode.value
    }

    fun setDesktopMode(enabled: Boolean) {
        _isDesktopMode.value = enabled
    }

    fun setSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun setSortFilter(filter: TopicSortFilter) {
        _sortFilter.value = filter
    }

    fun setSyllabusUri(uri: Uri) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>()
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                _syllabusBitmap.value = bitmap
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setScorecardUri(uri: Uri) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>()
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                _scorecardBitmap.value = bitmap
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadSimulatedSyllabus() {
        val lines = listOf(
            "1. Rotational Dynamics [Weightage: 9/10, Past Qs: 6]",
            "   - Pure rolling, Moment of Inertia, Conservation",
            "2. Thermodynamics & Cycles [Weightage: 8/10, Past Qs: 5]",
            "   - Carnot efficiency, PV diagrams, Entropy",
            "3. Wave Optics & Thin Film [Weightage: 7/10, Past Qs: 4]",
            "4. Electromagnetic Induction [Weightage: 8/10, Past Qs: 4]",
            "5. Fluid Mechanics & Bernoulli [Weightage: 7/10, Past Qs: 3]",
            "6. Electrostatics & Capacitors [Weightage: 8/10, Past Qs: 4]"
        )
        _syllabusBitmap.value = createSampleDocumentBitmap(
            title = "OFFICIAL EXAM SYLLABUS: ${_selectedSubject.value.uppercase()}",
            lines = lines,
            isScorecard = false
        )
    }

    fun loadSimulatedScorecard() {
        val lines = listOf(
            "CANDIDATE SCORECARD - MOCK TEST #4",
            "Total Score: 48 / 120 (Rank: 842 / 12,500)",
            "-------------------------------------------",
            "• Rotational Dynamics: 2/24 (Mistakes: -4 Neg)",
            "  * Reason: Lost grip on instantaneous center",
            "• 2nd Law Thermodynamics: 0/16 (Omitted)",
            "  * Reason: PV slope confusion, ran out of time",
            "• Wave Optics: 4/12 (Phase shift sign flip)",
            "• Electrostatics: 16/20 (Mastered)",
            "• Modern Physics: 18/20 (Mastered)"
        )
        _scorecardBitmap.value = createSampleDocumentBitmap(
            title = "MOCK TEST SCORECARD & ERROR AUDIT",
            lines = lines,
            isScorecard = true
        )
    }

    private fun createSampleDocumentBitmap(
        title: String,
        lines: List<String>,
        isScorecard: Boolean
    ): Bitmap {
        val width = 720
        val height = 960
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint().apply {
            color = if (isScorecard) 0xFF1C1924.toInt() else 0xFF101C24.toInt()
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        val borderPaint = Paint().apply {
            color = if (isScorecard) 0xFFFF5252.toInt() else 0xFF00E5FF.toInt()
            style = Paint.Style.STROKE
            strokeWidth = 6f
        }
        canvas.drawRect(16f, 16f, (width - 16).toFloat(), (height - 16).toFloat(), borderPaint)

        val titlePaint = Paint().apply {
            color = 0xFFFFFFFF.toInt()
            textSize = 28f
            isFakeBoldText = true
            isAntiAlias = true
        }
        canvas.drawText(title, 36f, 70f, titlePaint)

        val linePaint = Paint().apply {
            color = 0xFFD8D8E6.toInt()
            textSize = 22f
            isAntiAlias = true
        }

        var y = 130f
        for (line in lines) {
            canvas.drawText(line, 36f, y, linePaint)
            y += 42f
        }

        return bitmap
    }

    fun clearSyllabus() {
        _syllabusBitmap.value = null
    }

    fun clearScorecard() {
        _scorecardBitmap.value = null
    }

    fun loadPreset(preset: StudyPreset) {
        _selectedSubject.value = preset.subject
        saveAndActivatePayload(preset.payload)
    }

    fun generateBattlePlan() {
        _currentScreen.value = AppScreen.PROCESSING
        _processingProgress.value = 0.15f
        _processingStep.value = "Extracting chapters and weightages from syllabus image..."

        viewModelScope.launch {
            delay(700)
            _processingProgress.value = 0.45f
            _processingStep.value = "Auditing mock scorecard: identifying negative marks & error patterns..."

            delay(800)
            _processingProgress.value = 0.75f
            _processingStep.value = "Calculating Ruthless ROI: (Weightage × Weakness) / Estimated Hours..."

            val result = geminiService.analyzeSyllabusAndScorecard(
                syllabusBitmap = _syllabusBitmap.value,
                scorecardBitmap = _scorecardBitmap.value,
                subjectHint = _selectedSubject.value
            )

            delay(600)
            _processingProgress.value = 0.95f
            _processingStep.value = "Constructing 2-hour battle strike schedules..."

            delay(300)
            _processingProgress.value = 1.0f

            val payload = result.getOrNull() ?: SamplePresets.physicsPreset.payload
            saveAndActivatePayload(payload)
        }
    }

    private fun saveAndActivatePayload(payload: BattlePlanPayload) {
        viewModelScope.launch {
            val entity = BattlePlanEntity(
                title = payload.title,
                subject = payload.subject,
                summary = payload.summary,
                topics = payload.topics,
                heatmapTopics = payload.heatmapTopics
            )
            val newId = repository.savePlan(entity)
            val savedEntity = entity.copy(id = newId)
            _activePlan.value = savedEntity
            _currentScreen.value = AppScreen.BATTLE_PLAN_DASHBOARD
        }
    }

    fun selectPlan(plan: BattlePlanEntity) {
        _activePlan.value = plan
        _currentScreen.value = AppScreen.BATTLE_PLAN_DASHBOARD
    }

    fun deletePlan(planId: Long) {
        viewModelScope.launch {
            repository.deletePlan(planId)
            if (_activePlan.value?.id == planId) {
                _activePlan.value = null
                _currentScreen.value = AppScreen.HOME
            }
        }
    }

    fun toggleTopicCompleted(topicId: String) {
        val currentPlan = _activePlan.value ?: return
        val updatedTopics = currentPlan.topics.map { topic ->
            if (topic.id == topicId) {
                topic.copy(isCompleted = !topic.isCompleted)
            } else {
                topic
            }
        }
        val updatedPlan = currentPlan.copy(topics = updatedTopics)
        _activePlan.value = updatedPlan

        viewModelScope.launch {
            repository.updatePlan(updatedPlan)
        }
    }

    fun toggleStudyStepCompleted(topicId: String, stepIndex: Int) {
        val currentPlan = _activePlan.value ?: return
        val updatedTopics = currentPlan.topics.map { topic ->
            if (topic.id == topicId) {
                val updatedSteps = topic.studyPlan2Hours.mapIndexed { idx, step ->
                    if (idx == stepIndex) step.copy(isCompleted = !step.isCompleted) else step
                }
                topic.copy(studyPlan2Hours = updatedSteps)
            } else {
                topic
            }
        }
        val updatedPlan = currentPlan.copy(topics = updatedTopics)
        _activePlan.value = updatedPlan
        viewModelScope.launch {
            repository.updatePlan(updatedPlan)
        }
    }

    // --- Study Timer Actions ---
    fun openTimerForTopic(topic: TopicItem) {
        _timerState.value = FocusTimerState(
            isVisible = true,
            topicName = topic.topicName,
            totalSeconds = (topic.estimatedHours * 3600).toInt().coerceAtLeast(30 * 60),
            remainingSeconds = (topic.estimatedHours * 3600).toInt().coerceAtLeast(30 * 60),
            isRunning = true,
            currentStepIndex = 0
        )
        startTimerLoop()
    }

    fun toggleTimerPlayPause() {
        val isRunning = !_timerState.value.isRunning
        _timerState.value = _timerState.value.copy(isRunning = isRunning)
        if (isRunning) {
            startTimerLoop()
        } else {
            timerJob?.cancel()
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timerState.value = _timerState.value.copy(
            remainingSeconds = _timerState.value.totalSeconds,
            isRunning = false
        )
    }

    fun closeTimer() {
        timerJob?.cancel()
        _timerState.value = _timerState.value.copy(isVisible = false, isRunning = false)
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerState.value.isRunning && _timerState.value.remainingSeconds > 0) {
                delay(1000)
                val newSeconds = _timerState.value.remainingSeconds - 1
                _timerState.value = _timerState.value.copy(remainingSeconds = newSeconds)
            }
            if (_timerState.value.remainingSeconds <= 0) {
                _timerState.value = _timerState.value.copy(isRunning = false)
            }
        }
    }

    // --- Live Voice Coach Actions ---
    fun openLiveVoice() {
        _isLiveVoiceOpen.value = true
    }

    fun closeLiveVoice() {
        liveVoiceService.stopListening()
        liveVoiceService.stopSpeaking()
        _isLiveVoiceOpen.value = false
    }

    fun startListening() {
        liveVoiceService.startListening()
    }

    fun stopListening() {
        liveVoiceService.stopListening()
    }

    fun stopSpeaking() {
        liveVoiceService.stopSpeaking()
    }

    fun sendVoiceMessage(query: String) {
        liveVoiceService.sendMessage(query)
    }

    override fun onCleared() {
        super.onCleared()
        liveVoiceService.destroy()
    }
}
