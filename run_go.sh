#!/bin/bash

# Script to run the Go Data Processing System
# This script builds and runs the Go implementation with preloaded data

echo "======================================"
echo "Running Go Data Processing System"
echo "======================================"
echo ""

# Navigate to Go directory
cd "$(dirname "$0")/go" || exit 1

# Clean up previous output files
rm -f results.txt processing.log

# Build the Go program
echo "Building Go program..."
go build -o data_processing_system main.go

if [ $? -ne 0 ]; then
    echo "ERROR: Build failed!"
    exit 1
fi

echo "Build successful!"
echo ""

# Run the Go program
echo "Running the application..."
echo ""
./data_processing_system

echo ""
echo "======================================"
echo "Go execution completed!"
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

# Clean up binary
rm -f data_processing_system
