# MSCS-632 Assignment 6: Concurrent Data Processing System

This project implements a comprehensive **Multi-threaded Data Processing System** in both **Java** and **Go**, demonstrating advanced concurrency patterns, synchronization techniques, and error handling in production-ready applications.

## 🎯 Concurrency Principles Demonstrated

- **🔒 Thread Synchronization**: Proper use of locks, mutexes, and channels to prevent race conditions
- **⚡ Parallel Processing**: Multiple worker threads/goroutines processing tasks concurrently
- **🛡️ Deadlock Prevention**: Safe resource acquisition patterns and proper cleanup
- **🔧 Error Handling**: Comprehensive exception handling and graceful error recovery
- **📊 Resource Management**: Safe termination and proper cleanup of all threads

## 🚀 Project Overview

This system simulates a real-world data processing pipeline where multiple worker threads compete for tasks from a shared queue, process them with simulated computational work, and write results to a shared output file. The implementation showcases proper concurrency management in two very different programming paradigms.

### Key Components:
- **Shared Task Queue**: Thread-safe queue with proper synchronization mechanisms
- **Worker Threads**: 5 concurrent workers processing tasks in parallel
- **Concurrent File Writing**: Safe shared resource access with locks/mutexes
- **Comprehensive Logging**: Timestamped event tracking across all threads
- **Automated Demonstration**: Zero-input CLI apps with preloaded data

## 💻 System Requirements

### Java Implementation
- **JDK**: Java Development Kit 8 or higher
- **Operating System**: Linux, macOS, or Windows
- **Memory**: Minimum 512MB RAM
- **Disk Space**: ~50MB for compiled classes

### Go Implementation
- **Go**: Go 1.16 or higher
- **Operating System**: Linux, macOS, or Windows  
- **Memory**: Minimum 256MB RAM
- **Disk Space**: ~50MB for binary

## 📦 Installation & Setup

### Prerequisites

Before running this project, you need to install either Java or Go (or both) depending on which implementation you want to run.

### Quick Start (macOS/Linux)
```bash
# Clone or navigate to project directory
cd MSCS632_Assignment6

# Make scripts executable
chmod +x run_all.sh
chmod +x run_java.sh
chmod +x run_go.sh

# Run complete demonstration (Java + Go)
./run_all.sh
```

> **Note**: The script will automatically detect which languages are installed and run the available implementations.

---

### 🔧 Java Installation (Required for Java Implementation)

#### Verify if Java is Already Installed
```bash
java -version
javac -version
```

If both commands show version information (e.g., "Java version 1.8.0" or higher), Java JDK is installed and you can skip to the "Running" section.

#### macOS Installation

**Option 1: Homebrew (Recommended)**
```bash
# Install Homebrew OpenJDK 11
brew install openjdk@11

# The script will automatically detect Homebrew installations at:
# - /opt/homebrew/opt/openjdk@11/bin/ (Apple Silicon)
# - /usr/local/opt/openjdk@11/bin/ (Intel)
# - /opt/homebrew/opt/openjdk/bin/ (latest version)

# Verify installation
/opt/homebrew/opt/openjdk@11/bin/java -version
```

**Option 2: Download from Oracle**
1. Visit: https://www.oracle.com/java/technologies/downloads/
2. Download "macOS" installer for Java SE 11 or higher (JDK, not JRE)
3. Run the `.dmg` file and follow installation prompts
4. Verify installation: `java -version && javac -version`


#### Ubuntu/Debian Installation
```bash
# Update package list
sudo apt update

# Install default JDK (OpenJDK 11)
sudo apt install default-jdk

# Or install specific version
sudo apt install openjdk-11-jdk

# Verify installation (both java and javac should work)
java -version
javac -version
```

# Verify installation
java -version
javac -version
```

#### Windows Installation
1. Visit: https://www.oracle.com/java/technologies/downloads/
2. Download "Windows" installer (x64) for **JDK** (not JRE)
3. Run the `.exe` installer
4. Add Java to PATH:
   - Search "Environment Variables" in Windows
   - Edit "Path" variable
   - Add: `C:\Program Files\Java\jdk-11\bin`
5. Verify in Command Prompt: `java -version && javac -version`

---

### ⚡ Go Installation (Required for Go Implementation)

#### Verify if Go is Already Installed
```bash
go version
```

If the command shows version information (e.g., "go version go1.16" or higher), Go is installed and you can skip to the "Running" section.

#### macOS Installation

**Download from Official Site**
1. Visit: https://golang.org/dl/
2. Download "macOS" installer (.pkg file)
3. Run the package and follow prompts
4. Verify: `go version`

#### Ubuntu/Debian Installation

**Option 1: Using apt (May not be latest version)**
```bash
sudo apt update
sudo apt install golang-go

# Verify
go version
```

**Option 2: Download Latest Version**
```bash
# Download latest Go (check https://golang.org/dl/ for current version)
wget https://go.dev/dl/go1.21.5.linux-amd64.tar.gz

# Remove old version and extract
sudo rm -rf /usr/local/go
sudo tar -C /usr/local -xzf go1.21.5.linux-amd64.tar.gz

# Add to PATH (add to ~/.bashrc or ~/.profile)
echo 'export PATH=$PATH:/usr/local/go/bin' >> ~/.bashrc
source ~/.bashrc

# Verify
go version
```

#### Windows Installation
1. Visit: https://golang.org/dl/
2. Download "Windows" installer (.msi file)
3. Run the installer (installs to `C:\Go` by default)
4. Installer automatically adds Go to PATH
5. Verify in Command Prompt: `go version`

---

### ✅ Verification

After installing Java and/or Go, verify your installation:

```bash
# Check Java
java -version
# Check Go
go version

# Test the project
cd MSCS632_Assignment6
./run_all.sh     # Will run both implementations if available
```

### 🎯 What You Need

| To Run | Requirements |
|--------|-------------|
| **Java Implementation** | JDK 8+ (JDK 11+ recommended) |
| **Go Implementation** | Go 1.16+ (Go 1.21+ recommended) |
| **Both Implementations** | Both of the above |
| **Minimum (Demo)** | At least one (Java or Go) |

---

### 🎯 Post-Installation Setup

#### For Java Users
Once Java is installed, you can compile and run the Java implementation:

```bash
# Navigate to project
cd MSCS632_Assignment6

# Run Java implementation
./run_java.sh
```

#### For Go Users
Once Go is installed, you can run the Go implementation:

```bash
# Navigate to project
cd MSCS632_Assignment6

# Run Go implementation
./run_go.sh
```

---

## 📁 Project Structure

```
MSCS632_Assignment6/
├── README.md                    # This comprehensive guide
├── run_all.sh                   # Master demo script
├── run_java.sh                  # Java-specific runner
├── run_go.sh                    # Go-specific runner
│
├── java/                        # Java Implementation
│   ├── DataProcessingSystem.java # Complete implementation
│   ├── results.txt              # Generated after execution
│   └── processing.log           # Generated after execution
│
└── go/                          # Go Implementation
    ├── main.go                  # Complete implementation
    ├── go.mod                   # Go module file
    ├── results.txt              # Generated after execution
    └── processing.log           # Generated after execution
```

## 🛠 Features & Implementation

#### 1. **Shared Resource Queue** ✓
**Requirement:** Thread-safe queue with `addTask()` and `getTask()` methods

**Java Implementation:**
- `TaskQueue` class using `ReentrantLock` for explicit thread-safe access
- `LinkedList` as underlying data structure
- Lock-based synchronization in `finally` blocks ensures cleanup

**Go Implementation:**
- Channel-based queue (`chan Task`) - inherently thread-safe
- Buffered channel with capacity for all tasks
- Idiomatic Go concurrency using CSP model

#### 2. **Worker Threads** ✓
**Requirement:** Multiple workers retrieving and processing tasks with simulated delay

**Java Implementation:**
- 5 `Worker` instances implementing `Runnable` interface
- `ExecutorService` with fixed thread pool for efficient management
- Simulated computational delay: 50-150ms per task
- Results written to shared `ResultWriter`

**Go Implementation:**
- 5 goroutines (lightweight threads)
- Each goroutine runs `Worker.Run()` method  
- Simulated computational delay: 50-150ms per task
- Results written to shared `ResultWriter` with mutex protection

#### 3. **Concurrency Management** ✓
**Requirement:** No deadlocks, safe termination, proper synchronization

**Java Techniques:**
- `ReentrantLock` for queue and file access
- `CountDownLatch` for worker coordination
- `ExecutorService.shutdown()` for graceful termination
- Lock acquisition order prevents deadlocks

**Go Techniques:**
- Channels for queue operations (deadlock-free by design)
- `sync.Mutex` for file writer protection
- `sync.WaitGroup` for goroutine coordination
- `defer` statements ensure resources always released

**Safety Guarantees:**
- ✓ No race conditions detected
- ✓ All 20 tasks processed exactly once
- ✓ No deadlocks observed
- ✓ Clean shutdown of all workers
- ✓ Proper resource cleanup

#### 4. **Exception Handling** ✓
**Requirement:** Handle empty queue, file I/O errors, thread interruptions

**Java Error Handling:**
```java
try {
    task = taskQueue.getTask();
    resultWriter.writeResult(result);
} catch (IOException e) {
    logger.log("ERROR: " + e.getMessage());
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
} finally {
    // Guaranteed cleanup
}
```

**Go Error Handling:**
```go
result, err := w.processTask(task)
if err != nil {
    w.Logger.Log(fmt.Sprintf("ERROR: %v", err))
    continue
}

defer func() {
    // Guaranteed cleanup
    resultWriter.Close()
}()
```

#### 5. **Logging** ✓
**Requirement:** Log thread lifecycle, task processing, and errors

**Both Implementations Feature:**
- Thread-safe loggers with mutex/lock protection
- Timestamps on all entries (format: `[YYYY-MM-DD HH:MM:SS.mmm]`)
- Worker start/completion events
- Task processing events
- Error conditions with context
- System initialization and shutdown

### Enhanced Features 🚀

#### 6. **Production-Ready Architecture**
- **Factory Pattern**: Task creation with unique IDs
- **Resource Management**: Proper cleanup with finally/defer
- **Scalable Design**: Easy to adjust worker count and task volume
- **Separation of Concerns**: Clear class/struct responsibilities

#### 7. **Automated Demonstration**
- **Zero User Input**: All data preloaded in code
- **Comprehensive Scripts**: One command runs entire demo
- **Clear Output**: Real-time console feedback
- **Verification Files**: Results and logs for inspection

#### 8. **Performance Monitoring**
- **Task Distribution**: Logs show which worker processes which task
- **Completion Tracking**: Per-worker task counts
- **Throughput Metrics**: Total tasks processed per second
- **Resource Utilization**: Concurrent file writes without conflicts

## 🏃‍♂️ Running the Applications

### 🎬 Complete Demonstration (Both Languages)
```bash
# Run the master demo script (recommended)
./run_all.sh
```
This script will:
1. Check for Java and Go installations
2. Run Java implementation (if JDK available)
3. Run Go implementation (if Go available)
4. Display summary of results

### 🔧 Java Implementation

#### Automated Demo Script
```bash
cd java
../run_java.sh
```

#### Java Demo Features:
- **ExecutorService Thread Pool**: Efficient thread management
- **ReentrantLock Synchronization**: Explicit lock control
- **CountDownLatch Coordination**: Wait for all workers
- **Try-Catch-Finally**: Robust error handling
- **BufferedWriter**: Efficient file I/O

### ⚡ Go Implementation

#### Automated Demo Script
```bash
cd go
../run_go.sh
```

#### Go Demo Features:
- **Goroutines**: Lightweight concurrent execution
- **Channels**: CSP-style communication
- **WaitGroup**: Goroutine synchronization
- **Defer Statements**: Guaranteed cleanup
- **Error Values**: Explicit error handling

## 🔍 Implementation Details

### Java Architecture

**Class Hierarchy:**
```
DataProcessingSystem (Main)
├── TaskQueue (Thread-safe queue)
├── Task (Data model)
├── Worker (Runnable) 
├── ResultWriter (Thread-safe file I/O)
└── Logger (Thread-safe logging)
```

**Key Design Patterns:**
- **Factory Pattern**: Task creation with unique IDs
- **Thread Pool Pattern**: ExecutorService manages workers
- **Locking Pattern**: ReentrantLock with try-finally
- **Resource Acquisition Is Initialization (RAII)**: Proper cleanup

**Synchronization Details:**
- Queue operations: `ReentrantLock` 
- File writes: `ReentrantLock` with `BufferedWriter`
- Worker coordination: `CountDownLatch`
- Thread lifecycle: `ExecutorService.shutdown()`

### Go Architecture

**Component Structure:**
```
main()
├── TaskQueue (Channel-based)
├── Task (Struct)
├── Worker (Method on struct)
├── ResultWriter (Mutex-protected)
└── Logger (Mutex-protected)
```

**Key Design Patterns:**
- **CSP Pattern**: Channel-based communication
- **Worker Pool Pattern**: Fixed number of goroutines
- **Defer Pattern**: Guaranteed cleanup
- **Error Handling Pattern**: Explicit error values

**Synchronization Details:**
- Queue operations: Buffered channel (`chan Task`)
- File writes: `sync.Mutex` with `defer`
- Worker coordination: `sync.WaitGroup`
- Resource cleanup: `defer` statements

## 📝 Design Decisions

### Why 5 Workers?
- Demonstrates clear task distribution
- Shows concurrent execution without overwhelming output
- Typical for demonstrating parallelism concepts
- Easy to verify all workers participate

### Why 20 Tasks?
- Multiple tasks per worker shows competition
- Enough volume to demonstrate queue management
- Not too many for easy verification
- Allows clear output observation

### Why Random Delays (50-150ms)?
- Simulates real computational work
- Creates realistic task interleaving
- Shows non-deterministic execution order
- Makes concurrency issues visible if present

### Synchronization Strategy Comparison

| Aspect | Java | Go | Reasoning |
|--------|------|-----|-----------|
| **Queue** | ReentrantLock + LinkedList | Buffered Channel | Go channels are idiomatic and thread-safe by design |
| **File I/O** | ReentrantLock + BufferedWriter | Mutex + os.File | Both need explicit synchronization for shared files |
| **Coordination** | CountDownLatch | WaitGroup | Similar concepts, language-specific implementations |
| **Cleanup** | Finally blocks | Defer statements | Both guarantee execution, defer is more concise |
| **Error Handling** | Exceptions (try-catch) | Error values | Java: exceptional conditions; Go: explicit errors |