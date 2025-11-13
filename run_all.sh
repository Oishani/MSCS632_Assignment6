#!/bin/bash

# Master script to run both Java and Go implementations
# This demonstrates all features of both systems without user input

echo "=========================================="
echo "Data Processing System - Assignment 6"
echo "Running both Java and Go implementations"
echo "=========================================="
echo ""

# Get the script directory
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Check if Java is available and actually works
JAVA_AVAILABLE=false

# Check for Homebrew-installed OpenJDK (Apple Silicon)
if [ -d "/opt/homebrew/opt/openjdk@11/bin" ]; then
    if /opt/homebrew/opt/openjdk@11/bin/javac -version &> /dev/null; then
        JAVA_AVAILABLE=true
    fi
# Check for Homebrew-installed OpenJDK (Intel)
elif [ -d "/usr/local/opt/openjdk@11/bin" ]; then
    if /usr/local/opt/openjdk@11/bin/javac -version &> /dev/null; then
        JAVA_AVAILABLE=true
    fi
# Check for other Homebrew OpenJDK versions
elif [ -d "/opt/homebrew/opt/openjdk/bin" ]; then
    if /opt/homebrew/opt/openjdk/bin/javac -version &> /dev/null; then
        JAVA_AVAILABLE=true
    fi
elif [ -d "/usr/local/opt/openjdk/bin" ]; then
    if /usr/local/opt/openjdk/bin/javac -version &> /dev/null; then
        JAVA_AVAILABLE=true
    fi
# Check if system javac works
elif command -v javac &> /dev/null && command -v java &> /dev/null; then
    if javac -version &> /dev/null; then
        JAVA_AVAILABLE=true
    fi
fi

# Check if Go is available
GO_AVAILABLE=false
if command -v go &> /dev/null; then
    GO_AVAILABLE=true
fi

# Run Java implementation
if [ "$JAVA_AVAILABLE" = true ]; then
    echo ""
    echo "######################################"
    echo "# PART 1: Java Implementation        #"
    echo "######################################"
    echo ""
    bash "$SCRIPT_DIR/run_java.sh"
    
    # Add separator
    echo ""
    echo ""
    echo "=========================================="
    echo ""
    echo ""
else
    echo ""
    echo "######################################"
    echo "# PART 1: Java Implementation        #"
    echo "######################################"
    echo ""
    echo "⚠️  Java is not installed or not in PATH"
    echo "   Please install Java JDK 8 or higher to run the Java implementation"
    echo "   Visit: https://www.oracle.com/java/technologies/downloads/"
    echo ""
    echo "   The Java implementation is complete and ready to run at:"
    echo "   $SCRIPT_DIR/java/DataProcessingSystem.java"
    echo ""
    echo ""
    echo "=========================================="
    echo ""
    echo ""
fi

# Run Go implementation
if [ "$GO_AVAILABLE" = true ]; then
    echo "######################################"
    echo "# PART 2: Go Implementation          #"
    echo "######################################"
    echo ""
    bash "$SCRIPT_DIR/run_go.sh"
else
    echo "######################################"
    echo "# PART 2: Go Implementation          #"
    echo "######################################"
    echo ""
    echo "⚠️  Go is not installed or not in PATH"
    echo "   Please install Go 1.16 or higher to run the Go implementation"
    echo "   Visit: https://golang.org/dl/"
    echo ""
    echo "   The Go implementation is complete and ready to run at:"
    echo "   $SCRIPT_DIR/go/main.go"
    echo ""
fi

# Final summary
echo ""
echo "=========================================="
echo "Summary"
echo "=========================================="
echo ""

if [ "$JAVA_AVAILABLE" = true ] || [ "$GO_AVAILABLE" = true ]; then
    echo "✅ Completed implementations:"
    if [ "$JAVA_AVAILABLE" = true ]; then
        echo "   - Java (check java/results.txt and java/processing.log)"
    fi
    if [ "$GO_AVAILABLE" = true ]; then
        echo "   - Go (check go/results.txt and go/processing.log)"
    fi
    echo ""
    echo "📊 Understanding the Results:"
    echo "   Each task shows:"
    echo "   • Original: Initial data value (Task ID × 100)"
    echo "   • Processed: Computed result (Original × 2 + Task ID)"
    echo ""
    echo "   Example from results.txt:"
    echo "   Task 5 processed by Worker-3 | Original: 500 | Processed: 1005"
    echo "                                            ↑                 ↑"
    echo "                                      (5 × 100)      (500 × 2 + 5)"
    echo ""
fi

if [ "$JAVA_AVAILABLE" = false ] || [ "$GO_AVAILABLE" = false ]; then
    echo "⏭️  Skipped implementations (missing runtime):"
    if [ "$JAVA_AVAILABLE" = false ]; then
        echo "   - Java (source ready at java/DataProcessingSystem.java)"
    fi
    if [ "$GO_AVAILABLE" = false ]; then
        echo "   - Go (source ready at go/main.go)"
    fi
    echo ""
fi

echo "All source code is complete and ready to run!"
echo ""
