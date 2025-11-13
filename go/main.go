package main

import (
	"fmt"
	"io"
	"log"
	"math/rand"
	"os"
	"sync"
	"time"
)

const (
	OUTPUT_FILE = "results.txt"
	LOG_FILE    = "processing.log"
	NUM_WORKERS = 5
	NUM_TASKS   = 20
)

// Task represents a unit of work to be processed
type Task struct {
	ID          int
	Description string
	DataValue   int
}

func (t Task) String() string {
	return fmt.Sprintf("Task[id=%d, description='%s', data=%d]", t.ID, t.Description, t.DataValue)
}

// TaskQueue manages tasks using channels (Go's concurrency-safe queue)
type TaskQueue struct {
	tasks chan Task
}

// NewTaskQueue creates a new task queue
func NewTaskQueue(capacity int) *TaskQueue {
	return &TaskQueue{
		tasks: make(chan Task, capacity),
	}
}

// AddTask adds a task to the queue
func (tq *TaskQueue) AddTask(task Task) {
	tq.tasks <- task
}

// GetTask retrieves a task from the queue
// Returns task and boolean indicating if task was retrieved
func (tq *TaskQueue) GetTask() (Task, bool) {
	task, ok := <-tq.tasks
	return task, ok
}

// Close closes the task queue
func (tq *TaskQueue) Close() {
	close(tq.tasks)
}

// ResultWriter handles thread-safe writing to the results file
type ResultWriter struct {
	file  *os.File
	mutex sync.Mutex
}

// NewResultWriter creates a new result writer
func NewResultWriter(filename string) (*ResultWriter, error) {
	file, err := os.Create(filename)
	if err != nil {
		return nil, fmt.Errorf("failed to create results file: %w", err)
	}
	
	return &ResultWriter{
		file: file,
	}, nil
}

// WriteResult writes a result to the file (thread-safe)
func (rw *ResultWriter) WriteResult(result string) error {
	rw.mutex.Lock()
	defer rw.mutex.Unlock()
	
	_, err := fmt.Fprintln(rw.file, result)
	if err != nil {
		return fmt.Errorf("failed to write result: %w", err)
	}
	
	return nil
}

// Close closes the result writer
func (rw *ResultWriter) Close() error {
	rw.mutex.Lock()
	defer rw.mutex.Unlock()
	
	if rw.file != nil {
		return rw.file.Close()
	}
	return nil
}

// Logger handles thread-safe logging
type Logger struct {
	file  *os.File
	mutex sync.Mutex
}

// NewLogger creates a new logger
func NewLogger(filename string) (*Logger, error) {
	file, err := os.Create(filename)
	if err != nil {
		return nil, fmt.Errorf("failed to create log file: %w", err)
	}
	
	return &Logger{
		file: file,
	}, nil
}

// Log writes a log entry (thread-safe)
func (l *Logger) Log(message string) {
	l.mutex.Lock()
	defer l.mutex.Unlock()
	
	timestamp := time.Now().Format("2006-01-02 15:04:05.000")
	logEntry := fmt.Sprintf("[%s] %s\n", timestamp, message)
	
	_, err := l.file.WriteString(logEntry)
	if err != nil {
		log.Printf("ERROR: Failed to write log: %v\n", err)
	}
}

// Close closes the logger
func (l *Logger) Close() error {
	l.mutex.Lock()
	defer l.mutex.Unlock()
	
	if l.file != nil {
		return l.file.Close()
	}
	return nil
}

// Worker processes tasks from the queue
type Worker struct {
	ID           int
	TaskQueue    *TaskQueue
	ResultWriter *ResultWriter
	Logger       *Logger
	WaitGroup    *sync.WaitGroup
}

// Run starts the worker processing loop
func (w *Worker) Run() {
	defer w.WaitGroup.Done()
	
	workerName := fmt.Sprintf("Worker-%d", w.ID)
	w.Logger.Log(fmt.Sprintf("%s started", workerName))
	fmt.Printf("[%s] Started\n", workerName)
	
	tasksProcessed := 0
	
	// Process tasks until queue is closed and empty
	for {
		task, ok := w.TaskQueue.GetTask()
		if !ok {
			// Channel closed, no more tasks
			w.Logger.Log(fmt.Sprintf("%s found empty queue, finishing", workerName))
			break
		}
		
		// Process the task
		w.Logger.Log(fmt.Sprintf("%s processing %s", workerName, task.String()))
		fmt.Printf("[%s] Processing Task %d: %s\n", workerName, task.ID, task.Description)
		
		result, err := w.processTask(task)
		if err != nil {
			w.Logger.Log(fmt.Sprintf("ERROR: %s failed to process Task %d: %v", workerName, task.ID, err))
			fmt.Printf("[%s] ERROR: Failed to process task: %v\n", workerName, err)
			continue
		}
		
		tasksProcessed++
		
		// Write result to shared resource
		err = w.ResultWriter.WriteResult(result)
		if err != nil {
			w.Logger.Log(fmt.Sprintf("ERROR: %s failed to write result for Task %d: %v", workerName, task.ID, err))
			fmt.Printf("[%s] ERROR: Failed to write result: %v\n", workerName, err)
			continue
		}
		
		w.Logger.Log(fmt.Sprintf("%s completed Task %d", workerName, task.ID))
		fmt.Printf("[%s] Completed Task %d - Result: %s\n", workerName, task.ID, result)
	}
	
	w.Logger.Log(fmt.Sprintf("%s finished after processing %d tasks", workerName, tasksProcessed))
	fmt.Printf("[%s] Finished (processed %d tasks)\n", workerName, tasksProcessed)
}

// processTask simulates task processing with delay
func (w *Worker) processTask(task Task) (string, error) {
	// Simulate processing delay (50-150ms)
	delay := time.Duration(50+rand.Intn(100)) * time.Millisecond
	time.Sleep(delay)
	
	// Process the data (simple computation for demonstration)
	processedValue := task.DataValue*2 + task.ID
	
	result := fmt.Sprintf("Task %d processed by Worker-%d | Original: %d | Processed: %d",
		task.ID, w.ID, task.DataValue, processedValue)
	
	return result, nil
}

// preloadTasks loads tasks into the queue
func preloadTasks(taskQueue *TaskQueue, logger *Logger) {
	fmt.Println("Preloading tasks into queue...")
	for i := 1; i <= NUM_TASKS; i++ {
		task := Task{
			ID:          i,
			Description: fmt.Sprintf("Process data item %d", i),
			DataValue:   i * 100,
		}
		taskQueue.AddTask(task)
		logger.Log(fmt.Sprintf("Task %d added to queue", i))
	}
	fmt.Printf("Loaded %d tasks into the queue.\n\n", NUM_TASKS)
}

// displaySummary shows processing summary
func displaySummary(logger *Logger) {
	fmt.Println("\n=== Processing Summary ===")
	
	// Read and display results
	file, err := os.Open(OUTPUT_FILE)
	if err != nil {
		logger.Log(fmt.Sprintf("ERROR: Failed to open results file: %v", err))
		fmt.Printf("ERROR: Failed to open results file: %v\n", err)
		return
	}
	defer file.Close()
	
	fmt.Printf("\nResults written to: %s\n", OUTPUT_FILE)
	
	// Count lines in results file
	count := 0
	buf := make([]byte, 32*1024)
	lineSep := []byte{'\n'}
	
	for {
		c, err := file.Read(buf)
		if err != nil && err != io.EOF {
			logger.Log(fmt.Sprintf("ERROR: Failed to read results file: %v", err))
			break
		}
		
		count += countBytes(buf[:c], lineSep[0])
		
		if err == io.EOF {
			break
		}
	}
	
	fmt.Printf("Total results processed: %d\n", count)
	fmt.Printf("Processing log written to: %s\n", LOG_FILE)
	
	// Explain the results format
	fmt.Println("\n📊 Understanding the Results:")
	fmt.Println("   Each task in results.txt shows:")
	fmt.Println("   • Original: Initial data value (Task ID × 100)")
	fmt.Println("   • Processed: Computed result (Original × 2 + Task ID)")
	fmt.Println("\n   Example: Task 5 → Original: 500 → Processed: 1005")
	fmt.Println("            Calculation: (5 × 100) → (500 × 2 + 5)")
}

// countBytes counts occurrences of a byte in a slice
func countBytes(s []byte, b byte) int {
	count := 0
	for _, c := range s {
		if c == b {
			count++
		}
	}
	return count
}

func main() {
	fmt.Println("=== Data Processing System - Go Implementation ===\n")
	
	// Seed random number generator
	rand.Seed(time.Now().UnixNano())
	
	// Create logger
	logger, err := NewLogger(LOG_FILE)
	if err != nil {
		log.Fatalf("FATAL ERROR: Failed to initialize logger: %v", err)
	}
	defer func() {
		logger.Log("System shutdown complete")
		logger.Close()
	}()
	
	logger.Log("System initialization started")
	fmt.Printf("Initializing system with %d workers and %d tasks...\n\n", NUM_WORKERS, NUM_TASKS)
	
	// Create result writer
	resultWriter, err := NewResultWriter(OUTPUT_FILE)
	if err != nil {
		logger.Log(fmt.Sprintf("FATAL ERROR: Failed to initialize result writer: %v", err))
		log.Fatalf("FATAL ERROR: Failed to initialize result writer: %v", err)
	}
	defer resultWriter.Close()
	
	// Create task queue
	taskQueue := NewTaskQueue(NUM_TASKS)
	
	// Preload tasks
	preloadTasks(taskQueue, logger)
	
	// Create wait group for workers
	var wg sync.WaitGroup
	
	// Start workers
	logger.Log(fmt.Sprintf("Starting %d worker goroutines", NUM_WORKERS))
	fmt.Println("Starting worker goroutines...\n")
	
	for i := 1; i <= NUM_WORKERS; i++ {
		wg.Add(1)
		worker := &Worker{
			ID:           i,
			TaskQueue:    taskQueue,
			ResultWriter: resultWriter,
			Logger:       logger,
			WaitGroup:    &wg,
		}
		go worker.Run()
	}
	
	// Close the queue after a short delay to allow workers to start
	// In a real system, you'd close it after all tasks are added
	go func() {
		time.Sleep(100 * time.Millisecond)
		taskQueue.Close()
	}()
	
	// Wait for all workers to complete
	wg.Wait()
	
	logger.Log("All workers completed")
	fmt.Println("\nAll workers have completed their tasks.")
	
	// Display summary
	displaySummary(logger)
}
