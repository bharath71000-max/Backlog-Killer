package com.example.data.sample

import com.example.data.model.BattlePlanPayload
import com.example.data.model.HeatmapStatus
import com.example.data.model.HeatmapTopic
import com.example.data.model.StudyStep
import com.example.data.model.TopicItem

data class StudyPreset(
    val id: String,
    val title: String,
    val subject: String,
    val examName: String,
    val syllabusSummary: String,
    val scorecardSummary: String,
    val payload: BattlePlanPayload
)

object SamplePresets {

    val physicsPreset = StudyPreset(
        id = "preset_physics_jee",
        title = "Physics - Mechanics & Thermo Blitz",
        subject = "Physics",
        examName = "JEE Advanced / College Prep",
        syllabusSummary = "6 Core Modules: Rotational Mechanics, Thermodynamics, Ray Optics, Modern Physics, Fluid Mechanics, Electrostatics.",
        scorecardSummary = "Mock Test #4: Score 48/120. Rotational dynamics negative marking (-4), Thermo cycle zero.",
        payload = BattlePlanPayload(
            title = "Physics Tactical Kill Plan",
            subject = "Physics",
            summary = "Scorecard diagnosis revealed catastrophic error rates in Moment of Inertia conservation and Carnot efficiency. High exam weightage makes these two topics your fastest path to +32 net marks.",
            topics = listOf(
                TopicItem(
                    topicName = "Rotational Dynamics (Torque & Angular Momentum)",
                    subject = "Physics",
                    whyChosen = "Highest weightage in syllabus (9/10). Mock test scorecard shows 80% negative marking on rolling without slipping and angular momentum conservation.",
                    examWeightage = 9,
                    weaknessScore = 9,
                    estimatedHours = 2.0,
                    roiScore = 40.5,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 25 min",
                            title = "Core Theory Blitz",
                            description = "Review parallel/perpendicular axis theorems and pure rolling conditions (v = ωR, a = αR).",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "25 - 75 min",
                            title = "Ruthless Problem Drill",
                            description = "Solve 6 past-year questions specifically on instantaneous center of zero velocity and collision with hinged rods.",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "75 - 120 min",
                            title = "Scorecard Mistake Post-Mortem",
                            description = "Re-attempt questions #14, #15, #19 from your mock test without looking at solutions, document error root cause.",
                            actionType = "ANALYSIS"
                        )
                    )
                ),
                TopicItem(
                    topicName = "2nd Law of Thermodynamics & Carnot Cycles",
                    subject = "Physics",
                    whyChosen = "High yield (8/10 weightage). Complete omission on mock test due to PV/TS diagram confusion, yielding easy +12 marks recovery.",
                    examWeightage = 8,
                    weaknessScore = 8,
                    estimatedHours = 2.0,
                    roiScore = 32.0,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 30 min",
                            title = "Cycle Diagram Decryption",
                            description = "Map adiabatic vs isothermal slopes. Write work done integral equations for closed loops.",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "30 - 80 min",
                            title = "Efficiency & Entropy Calculations",
                            description = "Practice 5 multi-stage heat engine problems with mixed gases and polytropic processes.",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "80 - 120 min",
                            title = "Time-Trial Sprint",
                            description = "Solve 4 questions in 35 minutes under strict exam timer conditions.",
                            actionType = "ANALYSIS"
                        )
                    )
                ),
                TopicItem(
                    topicName = "Wave Optics & Thin Film Interference",
                    subject = "Physics",
                    whyChosen = "Exam weightage 7/10. Student lost 8 marks on path difference phase shift on reflection.",
                    examWeightage = 7,
                    weaknessScore = 8,
                    estimatedHours = 2.0,
                    roiScore = 28.0,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 30 min",
                            title = "Phase Shift & Optical Path Length",
                            description = "Master λ/2 shift rule upon reflection from denser medium.",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "30 - 85 min",
                            title = "YDSE & Thin Film Drill",
                            description = "Work through wedge-shaped films, fringe width shifts, and submerged apparatus problems.",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "85 - 120 min",
                            title = "Quick Check & Cheat Sheet",
                            description = "Draft 1-page condensed formula reference sheet for wave optics.",
                            actionType = "ANALYSIS"
                        )
                    )
                ),
                TopicItem(
                    topicName = "Electromagnetic Induction (Faraday & Lenz)",
                    subject = "Physics",
                    whyChosen = "Weightage 8/10. Moderate conceptual foundation, but calculation errors in motional EMF under time-varying magnetic fields.",
                    examWeightage = 8,
                    weaknessScore = 6,
                    estimatedHours = 2.0,
                    roiScore = 24.0,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 25 min",
                            title = "Lenz Law Direction Mastery",
                            description = "Right-hand rule drill on rotating loops and moving conductors.",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "25 - 80 min",
                            title = "Self & Mutual Inductance Problems",
                            description = "Solve RL circuit transient response and energy density integrals.",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "80 - 120 min",
                            title = "Mock Question Debugging",
                            description = "Analyze question #22 error in scorecard.",
                            actionType = "ANALYSIS"
                        )
                    )
                ),
                TopicItem(
                    topicName = "Fluid Dynamics (Bernoulli & Viscosity)",
                    subject = "Physics",
                    whyChosen = "Weightage 7/10. Poiseuille flow and terminal velocity mistakes accounted for 6 lost marks.",
                    examWeightage = 7,
                    weaknessScore = 5,
                    estimatedHours = 2.0,
                    roiScore = 17.5,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 30 min",
                            title = "Equation of Continuity & Energy Heads",
                            description = "Review Torricelli law, siphon problems, and Reynolds number thresholds.",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "30 - 85 min",
                            title = "Viscosity & Surface Tension Sprints",
                            description = "Calculate terminal velocity of falling droplets and excess pressure in bubbles.",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "85 - 120 min",
                            title = "Rapid Review Quiz",
                            description = "Complete 8 rapid-fire conceptual multiple choice checks.",
                            actionType = "ANALYSIS"
                        )
                    )
                )
            ),
            heatmapTopics = listOf(
                HeatmapTopic("Rotational Dynamics", "Physics", 9, 9, HeatmapStatus.CRITICAL_RED, 22, 40.5),
                HeatmapTopic("Thermodynamics", "Physics", 8, 8, HeatmapStatus.CRITICAL_RED, 28, 32.0),
                HeatmapTopic("Wave Optics", "Physics", 7, 8, HeatmapStatus.CRITICAL_RED, 35, 28.0),
                HeatmapTopic("Electromagnetic Induction", "Physics", 8, 6, HeatmapStatus.MODERATE_YELLOW, 52, 24.0),
                HeatmapTopic("Fluid Mechanics", "Physics", 7, 5, HeatmapStatus.MODERATE_YELLOW, 58, 17.5),
                HeatmapTopic("Modern Physics & Photoelectric", "Physics", 9, 3, HeatmapStatus.MASTERED_GREEN, 88, 13.5),
                HeatmapTopic("Electrostatics & Capacitors", "Physics", 8, 4, HeatmapStatus.MODERATE_YELLOW, 65, 16.0),
                HeatmapTopic("Current Electricity", "Physics", 7, 3, HeatmapStatus.MASTERED_GREEN, 84, 10.5),
                HeatmapTopic("Kinematics 1D & 2D", "Physics", 5, 2, HeatmapStatus.MASTERED_GREEN, 92, 5.0),
                HeatmapTopic("Gravitation & Satellites", "Physics", 6, 3, HeatmapStatus.MASTERED_GREEN, 85, 9.0),
                HeatmapTopic("Thermal Properties of Matter", "Physics", 5, 5, HeatmapStatus.MODERATE_YELLOW, 60, 12.5),
                HeatmapTopic("Ray Optics & Lenses", "Physics", 8, 4, HeatmapStatus.MODERATE_YELLOW, 70, 16.0)
            )
        )
    )

    val csPreset = StudyPreset(
        id = "preset_cs_dsa",
        title = "CS Algorithms - Midterm Backlog Killer",
        subject = "Computer Science",
        examName = "DSA Midterm & Technical Interview",
        syllabusSummary = "Dynamic Programming, Graph Shortest Paths, Trees & Balanced AVL, Sorting & Heaps, Bit Manipulation, Greedy Approximations.",
        scorecardSummary = "Midterm Exam Score: 52/100. Failed all DP memoization proofs and Dijkstra negative weight detection.",
        payload = BattlePlanPayload(
            title = "DSA Ruthless Score Maximizer",
            subject = "Computer Science",
            summary = "Scorecard analysis pinpoints severe deficiencies in state transition equations (DP) and edge-relaxation invariants. Mastering these two yields an immediate jump from C+ to solid A-grade.",
            topics = listOf(
                TopicItem(
                    topicName = "Dynamic Programming: 2D State Transitions",
                    subject = "Computer Science",
                    whyChosen = "Exam weightage 10/10. Student scored 3/25 on DP questions due to lack of base cases and state formulation.",
                    examWeightage = 10,
                    weaknessScore = 9,
                    estimatedHours = 2.0,
                    roiScore = 45.0,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 30 min",
                            title = "Subproblem Recurrence Mapping",
                            description = "Define dp[i][w] states for 0/1 Knapsack and Longest Common Subsequence.",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "30 - 80 min",
                            title = "Live Code Implementation",
                            description = "Implement bottom-up tabulation with space optimization from O(N*W) to O(W).",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "80 - 120 min",
                            title = "Scorecard Mistake Code Review",
                            description = "Rewrite Question 4 from midterm with rigorous time/space complexity proofs.",
                            actionType = "ANALYSIS"
                        )
                    )
                ),
                TopicItem(
                    topicName = "Graph Shortest Path (Dijkstra vs Bellman-Ford)",
                    subject = "Computer Science",
                    whyChosen = "Weightage 9/10. Student attempted Dijkstra on graph with negative edges, losing 15 marks outright.",
                    examWeightage = 9,
                    weaknessScore = 8,
                    estimatedHours = 2.0,
                    roiScore = 36.0,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 30 min",
                            title = "Invariant & Greedy Choice Rule",
                            description = "Clarify why Dijkstra fails on negative edge cycles. Review Bellman-Ford relaxation loop.",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "30 - 80 min",
                            title = "Priority Queue Optimization",
                            description = "Write adjacency list Dijkstra with min-heap priority queue in Kotlin/Java.",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "80 - 120 min",
                            title = "Negative Cycle Detection Drill",
                            description = "Run N-th iteration checks and dry-run 3 exam tracing diagrams.",
                            actionType = "ANALYSIS"
                        )
                    )
                ),
                TopicItem(
                    topicName = "AVL Tree Rotations & Balance Factors",
                    subject = "Computer Science",
                    whyChosen = "Weightage 8/10. Confused Left-Right (LR) double rotation with Right-Left (RL).",
                    examWeightage = 8,
                    weaknessScore = 7,
                    estimatedHours = 2.0,
                    roiScore = 28.0,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 30 min",
                            title = "4 Rotation Cases Anatomy",
                            description = "Draw LL, RR, LR, RL rebalancing diagrams step-by-step.",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "30 - 80 min",
                            title = "Dry-Run Insertion Traces",
                            description = "Simulate sequential insertions: 15, 20, 24, 10, 13, 7, 30, 36, 25.",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "80 - 120 min",
                            title = "Tree Property Verification",
                            description = "Verify height boundary h < 1.44 log2(N+2).",
                            actionType = "ANALYSIS"
                        )
                    )
                ),
                TopicItem(
                    topicName = "Bit Manipulation & Bitmasks",
                    subject = "Computer Science",
                    whyChosen = "Weightage 6/10. Fast study investment (1.5h) for reliable full points.",
                    examWeightage = 6,
                    weaknessScore = 7,
                    estimatedHours = 1.5,
                    roiScore = 28.0,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 25 min",
                            title = "Bitwise Operators & Tricks",
                            description = "Master n & (n-1) bit count, bit shift masks (1 << i), XOR properties.",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "25 - 65 min",
                            title = "Subset Generation via Bitmask",
                            description = "Implement subset iteration from 0 to (1 << N) - 1.",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "65 - 90 min",
                            title = "Exam Speed Quiz",
                            description = "Solve 5 quick bit tricks under 10 minutes total.",
                            actionType = "ANALYSIS"
                        )
                    )
                ),
                TopicItem(
                    topicName = "Greedy Scheduling & Interval Partitioning",
                    subject = "Computer Science",
                    whyChosen = "Weightage 7/10. Student sorted by start time instead of finish time on test.",
                    examWeightage = 7,
                    weaknessScore = 6,
                    estimatedHours = 2.0,
                    roiScore = 21.0,
                    studyPlan2Hours = listOf(
                        StudyStep(
                            timeRange = "00 - 30 min",
                            title = "Greedy Stays Ahead Proof",
                            description = "Understand exchange argument proof for earliest finish time.",
                            actionType = "CONCEPT"
                        ),
                        StudyStep(
                            timeRange = "30 - 80 min",
                            title = "Meeting Rooms & Activity Selection",
                            description = "Solve interval scheduling and minimum platforms needed.",
                            actionType = "PRACTICE"
                        ),
                        StudyStep(
                            timeRange = "80 - 120 min",
                            title = "Edge Case Audit",
                            description = "Test identical start times and overlapping endpoints.",
                            actionType = "ANALYSIS"
                        )
                    )
                )
            ),
            heatmapTopics = listOf(
                HeatmapTopic("Dynamic Programming", "CS", 10, 9, HeatmapStatus.CRITICAL_RED, 18, 45.0),
                HeatmapTopic("Graph Shortest Paths", "CS", 9, 8, HeatmapStatus.CRITICAL_RED, 25, 36.0),
                HeatmapTopic("AVL & Red-Black Trees", "CS", 8, 7, HeatmapStatus.CRITICAL_RED, 38, 28.0),
                HeatmapTopic("Bit Manipulation", "CS", 6, 7, HeatmapStatus.MODERATE_YELLOW, 45, 28.0),
                HeatmapTopic("Greedy Algorithms", "CS", 7, 6, HeatmapStatus.MODERATE_YELLOW, 55, 21.0),
                HeatmapTopic("Binary Search & Two Pointers", "CS", 8, 3, HeatmapStatus.MASTERED_GREEN, 88, 12.0),
                HeatmapTopic("Heap & Priority Queue", "CS", 7, 4, HeatmapStatus.MODERATE_YELLOW, 68, 14.0),
                HeatmapTopic("Recursion & Backtracking", "CS", 7, 5, HeatmapStatus.MODERATE_YELLOW, 62, 17.5),
                HeatmapTopic("Sorting Algorithms", "CS", 5, 2, HeatmapStatus.MASTERED_GREEN, 94, 5.0),
                HeatmapTopic("Hash Maps & Disjoint Sets", "CS", 8, 3, HeatmapStatus.MASTERED_GREEN, 85, 12.0),
                HeatmapTopic("Complexity Big-O Analysis", "CS", 6, 2, HeatmapStatus.MASTERED_GREEN, 90, 6.0),
                HeatmapTopic("String Matching (KMP)", "CS", 5, 6, HeatmapStatus.MODERATE_YELLOW, 50, 15.0)
            )
        )
    )

    val allPresets = listOf(physicsPreset, csPreset)
}
