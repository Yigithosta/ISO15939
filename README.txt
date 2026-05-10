================================================================================
  ISO 15939 Measurement Process Simulator
  Java Swing Desktop Application
================================================================================

Student Name  : [YOUR NAME HERE]
Student ID    : [YOUR ID HERE]
Course        : Software Project II
Assignment    : Individual Project – ISO/IEC 15939 Simulator

--------------------------------------------------------------------------------
PROJECT OVERVIEW
--------------------------------------------------------------------------------
A 5-step wizard application that simulates the ISO/IEC 15939 software
measurement process standard:

  Step 1 – Profile   : Enter username, school, and session name
  Step 2 – Define    : Select quality type, mode, and scenario
  Step 3 – Plan      : View dimensions and metrics (read-only table)
  Step 4 – Collect   : View raw data values and auto-calculated scores (1–5)
  Step 5 – Analyse   : Weighted averages, radar chart, and gap analysis

--------------------------------------------------------------------------------
TECHNOLOGIES USED
--------------------------------------------------------------------------------
  - Java SE 17 (standard library only – no external dependencies)
  - Java Swing: JFrame, JPanel, CardLayout, JRadioButton, JTable,
                JProgressBar, Graphics2D
  - MVC pattern: model/ contains data/logic, view/ contains all UI classes
  - OOP: Encapsulation (private fields + getters), Inheritance (panel hierarchy),
         Polymorphism (renderer overrides)
  - Collections: ArrayList, HashMap (ScenarioRepository)

--------------------------------------------------------------------------------
FILE STRUCTURE
--------------------------------------------------------------------------------
  ISO15939/
  ├── Main.java                    Application entry point
  ├── model/
  │   ├── Metric.java              Single metric with score formula
  │   ├── Dimension.java           Groups metrics, computes weighted avg
  │   ├── Scenario.java            Contains a list of dimensions
  │   ├── UserProfile.java         Step 1 data
  │   ├── MeasurementSession.java  Wizard state passed between steps
  │   └── ScenarioRepository.java  All hard-coded scenario datasets
  └── view/
      ├── MainFrame.java           JFrame + CardLayout wizard
      ├── StepIndicatorPanel.java  Top progress indicator
      ├── ProfilePanel.java        Step 1 UI
      ├── DefinePanel.java         Step 2 UI (radio buttons)
      ├── PlanPanel.java           Step 3 UI (read-only table)
      ├── CollectPanel.java        Step 4 UI (scored table)
      ├── AnalysePanel.java        Step 5 UI (bars + chart + gap)
      └── RadarChartPanel.java     Bonus – spider chart via Graphics2D

--------------------------------------------------------------------------------
COMPILATION & RUN INSTRUCTIONS
--------------------------------------------------------------------------------
  Requirements: JDK 17 or higher installed and on PATH.

  1. Open a terminal and navigate to the project root:
       cd /path/to/ISO15939

  2. Compile all source files:
       mkdir -p out
       javac -encoding UTF-8 -d out \
         model/Metric.java \
         model/Dimension.java \
         model/Scenario.java \
         model/UserProfile.java \
         model/MeasurementSession.java \
         model/ScenarioRepository.java \
         view/RadarChartPanel.java \
         view/StepIndicatorPanel.java \
         view/ProfilePanel.java \
         view/DefinePanel.java \
         view/PlanPanel.java \
         view/CollectPanel.java \
         view/AnalysePanel.java \
         view/MainFrame.java \
         Main.java

  3. Run the application:
       java -cp out Main

  Alternatively, use the provided script (macOS / Linux):
       chmod +x compile.sh
       ./compile.sh

--------------------------------------------------------------------------------
SCENARIOS INCLUDED
--------------------------------------------------------------------------------
  Health Mode:
    Scenario A – Clinic Alpha   (Performance, Security, Availability, Usability)
    Scenario B – Hospital Beta  (Performance, Security, Availability, Usability)

  Education Mode:
    Scenario C – Team Alpha     (Usability, Perf.Efficiency, Accessibility,
                                  Reliability, Func.Suitability)
    Scenario D – Team Beta      (same dimensions, different values)

--------------------------------------------------------------------------------
SCORE CALCULATION
--------------------------------------------------------------------------------
  Higher is better:  score = 1 + (value - min) / (max - min) × 4
  Lower is better:   score = 5 - (value - min) / (max - min) × 4
  Result clamped to [1.0, 5.0] and rounded to nearest 0.5.

  Dimension score (weighted average):
    dimensionScore = Σ(metricScore × metricCoeff) / Σ(metricCoeff)

--------------------------------------------------------------------------------
BONUS FEATURES
--------------------------------------------------------------------------------
  - Radar (spider) chart on the Analyse screen (Step 5b)
    Implemented using Java 2D Graphics (Graphics2D, Polygon drawing).

================================================================================
