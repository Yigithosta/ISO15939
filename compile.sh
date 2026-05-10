#!/bin/bash
# Compile and optionally run the ISO 15939 Measurement Process Simulator.
# Usage: ./compile.sh          – compile only
#        ./compile.sh run      – compile and run

set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT/src"
OUT="$ROOT/out"

mkdir -p "$OUT"

echo "Compiling..."
javac -encoding UTF-8 -d "$OUT" \
    "$SRC/model/Metric.java" \
    "$SRC/model/Dimension.java" \
    "$SRC/model/Scenario.java" \
    "$SRC/model/UserProfile.java" \
    "$SRC/model/MeasurementSession.java" \
    "$SRC/model/ScenarioRepository.java" \
    "$SRC/view/RadarChartPanel.java" \
    "$SRC/view/StepIndicatorPanel.java" \
    "$SRC/view/ProfilePanel.java" \
    "$SRC/view/DefinePanel.java" \
    "$SRC/view/PlanPanel.java" \
    "$SRC/view/CollectPanel.java" \
    "$SRC/view/AnalysePanel.java" \
    "$SRC/view/MainFrame.java" \
    "$SRC/Main.java"

echo "Compilation successful."

if [ "$1" = "run" ]; then
    echo "Starting application..."
    java -cp "$OUT" Main
fi
