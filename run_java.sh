#!/bin/bash

# Script to run the Java Data Processing System
# This script compiles and runs the Java implementation with preloaded data

echo "======================================"
echo "Running Java Data Processing System"
echo "======================================"
echo ""

# Navigate to Java directory
cd "$(dirname "$0")/java" || exit 1

# Clean up previous output files
rm -f results.txt processing.log *.class

# Detect Java installation
JAVAC_CMD="javac"
JAVA_CMD="java"

# Check for Homebrew-installed OpenJDK (Apple Silicon)
if [ -d "/opt/homebrew/opt/openjdk@11/bin" ]; then
    JAVAC_CMD="/opt/homebrew/opt/openjdk@11/bin/javac"
    JAVA_CMD="/opt/homebrew/opt/openjdk@11/bin/java"
    echo "Using Homebrew OpenJDK 11 (Apple Silicon)"
# Check for Homebrew-installed OpenJDK (Intel)
elif [ -d "/usr/local/opt/openjdk@11/bin" ]; then
    JAVAC_CMD="/usr/local/opt/openjdk@11/bin/javac"
    JAVA_CMD="/usr/local/opt/openjdk@11/bin/java"
    echo "Using Homebrew OpenJDK 11 (Intel)"
# Check for other Homebrew OpenJDK versions
elif [ -d "/opt/homebrew/opt/openjdk/bin" ]; then
    JAVAC_CMD="/opt/homebrew/opt/openjdk/bin/javac"
    JAVA_CMD="/opt/homebrew/opt/openjdk/bin/java"
    echo "Using Homebrew OpenJDK (latest)"
elif [ -d "/usr/local/opt/openjdk/bin" ]; then
    JAVAC_CMD="/usr/local/opt/openjdk/bin/javac"
    JAVA_CMD="/usr/local/opt/openjdk/bin/java"
    echo "Using Homebrew OpenJDK (latest)"
# Check if javac works (system Java)
elif javac -version &> /dev/null; then
    echo "Using system Java"
else
    echo "ERROR: Java Development Kit (JDK) not found!"
    echo ""
    echo "Please install JDK using one of these methods:"
    echo "  1. Homebrew: brew install openjdk@11"
    echo "  2. Oracle: https://www.oracle.com/java/technologies/downloads/"
    echo ""
    exit 1
fi

# Compile the Java program
echo "Compiling Java source code..."
$JAVAC_CMD DataProcessingSystem.java

if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed!"
    exit 1
fi

echo "Compilation successful!"
echo ""

# Run the Java program
echo "Running the application..."
echo ""
$JAVA_CMD DataProcessingSystem

echo ""
echo "======================================"
echo "Java execution completed!"
echo "======================================"
echo ""
echo "Output files:"
echo "  - results.txt: Processed task results"
echo "  - processing.log: Detailed execution log"
echo ""
echo "Understanding the Results:"
echo "  • Original: Initial data value (Task ID × 100)"
echo "  • Processed: Computed result (Original × 2 + Task ID)"
echo "  • Example: Task 5 → Original: 500 → Processed: 1005"
echo ""
