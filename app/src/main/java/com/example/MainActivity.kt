package com.example

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.BattlePlanDashboardScreen
import com.example.ui.screens.FocusTimerSheet
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveVoiceCoachSheet
import com.example.ui.screens.ProcessingScreen
import com.example.ui.screens.ScanUploadScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BattlePlanViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BattlePlanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        BacklogKillerApp(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun BacklogKillerApp(viewModel: BattlePlanViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val savedPlans by viewModel.allSavedPlans.collectAsStateWithLifecycle()
    val activePlan by viewModel.activePlan.collectAsStateWithLifecycle()
    val syllabusBitmap by viewModel.syllabusBitmap.collectAsStateWithLifecycle()
    val scorecardBitmap by viewModel.scorecardBitmap.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
    val processingStep by viewModel.processingStep.collectAsStateWithLifecycle()
    val processingProgress by viewModel.processingProgress.collectAsStateWithLifecycle()
    val isDesktopMode by viewModel.isDesktopMode.collectAsStateWithLifecycle()
    val sortFilter by viewModel.sortFilter.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()

    val isLiveVoiceOpen by viewModel.isLiveVoiceOpen.collectAsStateWithLifecycle()
    val voiceState by viewModel.voiceState.collectAsStateWithLifecycle()
    val voiceMessages by viewModel.voiceMessages.collectAsStateWithLifecycle()

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.startListening()
        }
    }

    when (currentScreen) {
        AppScreen.HOME -> {
            HomeScreen(
                savedPlans = savedPlans,
                onScanClick = { viewModel.navigateTo(AppScreen.SCAN_UPLOAD) },
                onSelectPlan = { plan -> viewModel.selectPlan(plan) },
                onDeletePlan = { id -> viewModel.deletePlan(id) },
                onLoadPreset = { preset -> viewModel.loadPreset(preset) },
                isDesktopMode = isDesktopMode,
                onToggleDesktop = { viewModel.toggleDesktopMode() },
                onOpenVoiceCoach = { viewModel.openLiveVoice() }
            )
        }

        AppScreen.SCAN_UPLOAD -> {
            ScanUploadScreen(
                syllabusBitmap = syllabusBitmap,
                scorecardBitmap = scorecardBitmap,
                selectedSubject = selectedSubject,
                onSubjectChange = { viewModel.setSubject(it) },
                onSyllabusSelected = { uri -> viewModel.setSyllabusUri(uri) },
                onScorecardSelected = { uri -> viewModel.setScorecardUri(uri) },
                onLoadSimulatedSyllabus = { viewModel.loadSimulatedSyllabus() },
                onLoadSimulatedScorecard = { viewModel.loadSimulatedScorecard() },
                onClearSyllabus = { viewModel.clearSyllabus() },
                onClearScorecard = { viewModel.clearScorecard() },
                onLoadPreset = { preset -> viewModel.loadPreset(preset) },
                onGeneratePlan = { viewModel.generateBattlePlan() },
                onBackClick = { viewModel.navigateTo(AppScreen.HOME) },
                isDesktopMode = isDesktopMode,
                onToggleDesktop = { viewModel.toggleDesktopMode() }
            )
        }

        AppScreen.PROCESSING -> {
            ProcessingScreen(
                currentStep = processingStep,
                progress = processingProgress
            )
        }

        AppScreen.BATTLE_PLAN_DASHBOARD -> {
            BattlePlanDashboardScreen(
                plan = activePlan,
                onTopicToggle = { topicId -> viewModel.toggleTopicCompleted(topicId) },
                onStepToggle = { topicId, stepIdx -> viewModel.toggleStudyStepCompleted(topicId, stepIdx) },
                onStartTimer = { topic -> viewModel.openTimerForTopic(topic) },
                onBackClick = { viewModel.navigateTo(AppScreen.HOME) },
                sortFilter = sortFilter,
                onFilterChange = { filter -> viewModel.setSortFilter(filter) },
                isDesktopMode = isDesktopMode,
                onToggleDesktop = { viewModel.toggleDesktopMode() },
                onOpenVoiceCoach = { viewModel.openLiveVoice() }
            )
        }
    }

    // Modal Focus Timer Sheet
    if (timerState.isVisible) {
        FocusTimerSheet(
            state = timerState,
            onPlayPause = { viewModel.toggleTimerPlayPause() },
            onReset = { viewModel.resetTimer() },
            onClose = { viewModel.closeTimer() },
            onCompleteTopic = {
                activePlan?.topics?.find { it.topicName == timerState.topicName }?.let { topic ->
                    viewModel.toggleTopicCompleted(topic.id)
                }
            }
        )
    }

    // Modal Live Voice Coach Sheet (Gemini Live API)
    if (isLiveVoiceOpen) {
        LiveVoiceCoachSheet(
            isVisible = isLiveVoiceOpen,
            voiceState = voiceState,
            messages = voiceMessages,
            onStartListening = {
                micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            },
            onStopListening = { viewModel.stopListening() },
            onStopSpeaking = { viewModel.stopSpeaking() },
            onSendMessage = { query -> viewModel.sendVoiceMessage(query) },
            onClose = { viewModel.closeLiveVoice() }
        )
    }
}
