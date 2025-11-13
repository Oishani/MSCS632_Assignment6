import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main class for the Data Processing System
 * Manages worker threads that process tasks from a shared queue
 */
public class DataProcessingSystem {
    private static final String OUTPUT_FILE = "results.txt";
    private static final String LOG_FILE = "processing.log";
    private static final int NUM_WORKERS = 5;
    private static final int NUM_TASKS = 20;
    
    public static void main(String[] args) {
        System.out.println("=== Data Processing System - Java Implementation ===\n");
        
        try {
            // Create shared resources
            TaskQueue taskQueue = new TaskQueue();
            ResultWriter resultWriter = new ResultWriter(OUTPUT_FILE);
            Logger logger = new Logger(LOG_FILE);
            
            logger.log("System initialization started");
            System.out.println("Initializing system with " + NUM_WORKERS + " workers and " + NUM_TASKS + " tasks...\n");
            
            // Preload tasks into the queue
            preloadTasks(taskQueue, logger);
            
            // Create thread pool
            ExecutorService executor = Executors.newFixedThreadPool(NUM_WORKERS);
            CountDownLatch latch = new CountDownLatch(NUM_WORKERS);
            
            // Start worker threads
            logger.log("Starting " + NUM_WORKERS + " worker threads");
            System.out.println("Starting worker threads...\n");
            
            for (int i = 0; i < NUM_WORKERS; i++) {
                Worker worker = new Worker(i + 1, taskQueue, resultWriter, logger, latch);
                executor.submit(worker);
            }
            
            // Wait for all workers to complete
            try {
                latch.await();
                logger.log("All workers completed");
                System.out.println("\nAll workers have completed their tasks.");
            } catch (InterruptedException e) {
                logger.log("ERROR: Main thread interrupted: " + e.getMessage());
                System.err.println("Main thread interrupted: " + e.getMessage());
            }
            
            // Shutdown executor
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                logger.log("ERROR: Executor shutdown interrupted: " + e.getMessage());
            }
            
            // Close resources
            resultWriter.close();
            
            // Display summary
            displaySummary(logger);
            
            logger.log("System shutdown complete");
            logger.close();
            
        } catch (IOException e) {
            System.err.println("FATAL ERROR: Failed to initialize system: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Preload tasks into the shared queue
     */
    private static void preloadTasks(TaskQueue taskQueue, Logger logger) {
        System.out.println("Preloading tasks into queue...");
        for (int i = 1; i <= NUM_TASKS; i++) {
            Task task = new Task(i, "Process data item " + i, i * 100);
            taskQueue.addTask(task);
            logger.log("Task " + i + " added to queue");
        }
        System.out.println("Loaded " + NUM_TASKS + " tasks into the queue.\n");
    }
    
    /**
     * Display summary of processing
     */
    private static void displaySummary(Logger logger) {
        System.out.println("\n=== Processing Summary ===");
        
        try {
            // Read and display results
            File resultsFile = new File(OUTPUT_FILE);
            if (resultsFile.exists()) {
                System.out.println("\nResults written to: " + OUTPUT_FILE);
                BufferedReader reader = new BufferedReader(new FileReader(resultsFile));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null) {
                    count++;
                }
                reader.close();
                System.out.println("Total results processed: " + count);
            }
            
            // Display log file location
            System.out.println("Processing log written to: " + LOG_FILE);
            
            // Explain the results format
            System.out.println("\n📊 Understanding the Results:");
            System.out.println("   Each task in results.txt shows:");
            System.out.println("   • Original: Initial data value (Task ID × 100)");
            System.out.println("   • Processed: Computed result (Original × 2 + Task ID)");
            System.out.println("\n   Example: Task 5 → Original: 500 → Processed: 1005");
            System.out.println("            Calculation: (5 × 100) → (500 × 2 + 5)");
            
        } catch (IOException e) {
            logger.log("ERROR: Failed to read summary: " + e.getMessage());
            System.err.println("Failed to read summary: " + e.getMessage());
        }
    }
}

/**
 * Represents a task to be processed
 */
class Task {
    private final int id;
    private final String description;
    private final int dataValue;
    
    public Task(int id, String description, int dataValue) {
        this.id = id;
        this.description = description;
        this.dataValue = dataValue;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getDataValue() {
        return dataValue;
    }
    
    @Override
    public String toString() {
        return "Task[id=" + id + ", description='" + description + "', data=" + dataValue + "]";
    }
}

/**
 * Thread-safe queue for managing tasks
 * Uses ReentrantLock for synchronization
 */
class TaskQueue {
    private final Queue<Task> queue;
    private final ReentrantLock lock;
    
    public TaskQueue() {
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
    }
    
    /**
     * Add a task to the queue (thread-safe)
     */
    public void addTask(Task task) {
        lock.lock();
        try {
            queue.offer(task);
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Retrieve and remove a task from the queue (thread-safe)
     * Returns null if queue is empty
     */
    public Task getTask() {
        lock.lock();
        try {
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Check if queue is empty (thread-safe)
     */
    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Get current queue size (thread-safe)
     */
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * Worker thread that processes tasks from the queue
 */
class Worker implements Runnable {
    private final int workerId;
    private final TaskQueue taskQueue;
    private final ResultWriter resultWriter;
    private final Logger logger;
    private final CountDownLatch latch;
    
    public Worker(int workerId, TaskQueue taskQueue, ResultWriter resultWriter, 
                  Logger logger, CountDownLatch latch) {
        this.workerId = workerId;
        this.taskQueue = taskQueue;
        this.resultWriter = resultWriter;
        this.logger = logger;
        this.latch = latch;
    }
    
    @Override
    public void run() {
        String workerName = "Worker-" + workerId;
        logger.log(workerName + " started");
        System.out.println("[" + workerName + "] Started");
        
        int tasksProcessed = 0;
        
        try {
            while (true) {
                Task task = null;
                
                try {
                    // Retrieve task from queue
                    task = taskQueue.getTask();
                    
                    if (task == null) {
                        // Queue is empty, worker can finish
                        logger.log(workerName + " found empty queue, finishing");
                        break;
                    }
                    
                    // Process the task
                    logger.log(workerName + " processing " + task);
                    System.out.println("[" + workerName + "] Processing Task " + task.getId() + 
                                     ": " + task.getDescription());
                    
                    String result = processTask(task);
                    tasksProcessed++;
                    
                    // Write result to shared resource
                    try {
                        resultWriter.writeResult(result);
                        logger.log(workerName + " completed Task " + task.getId());
                        System.out.println("[" + workerName + "] Completed Task " + task.getId() + 
                                         " - Result: " + result);
                    } catch (IOException e) {
                        logger.log("ERROR: " + workerName + " failed to write result for Task " + 
                                 task.getId() + ": " + e.getMessage());
                        System.err.println("[" + workerName + "] ERROR: Failed to write result: " + 
                                         e.getMessage());
                    }
                    
                } catch (Exception e) {
                    logger.log("ERROR: " + workerName + " encountered exception: " + e.getMessage());
                    System.err.println("[" + workerName + "] ERROR: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
        } finally {
            logger.log(workerName + " finished after processing " + tasksProcessed + " tasks");
            System.out.println("[" + workerName + "] Finished (processed " + tasksProcessed + " tasks)");
            latch.countDown();
        }
    }
    
    /**
     * Process a task - simulates computational work with delay
     */
    private String processTask(Task task) {
        try {
            // Simulate processing delay (50-150ms)
            Thread.sleep(50 + (long)(Math.random() * 100));
            
            // Process the data (simple computation for demonstration)
            int processedValue = task.getDataValue() * 2 + task.getId();
            
            return "Task " + task.getId() + " processed by Worker-" + workerId + 
                   " | Original: " + task.getDataValue() + " | Processed: " + processedValue;
                   
        } catch (InterruptedException e) {
            logger.log("ERROR: Worker-" + workerId + " interrupted during processing");
            Thread.currentThread().interrupt();
            return "Task " + task.getId() + " interrupted";
        }
    }
}

/**
 * Thread-safe result writer for writing to output file
 */
class ResultWriter {
    private final String filename;
    private final ReentrantLock lock;
    private BufferedWriter writer;
    
    public ResultWriter(String filename) throws IOException {
        this.filename = filename;
        this.lock = new ReentrantLock();
        this.writer = new BufferedWriter(new FileWriter(filename, false));
    }
    
    /**
     * Write a result to the output file (thread-safe)
     */
    public void writeResult(String result) throws IOException {
        lock.lock();
        try {
            writer.write(result);
            writer.newLine();
            writer.flush();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Close the writer
     */
    public void close() {
        lock.lock();
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("ERROR: Failed to close result writer: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}

/**
 * Thread-safe logger for writing to log file
 */
class Logger {
    private final String filename;
    private final ReentrantLock lock;
    private BufferedWriter writer;
    private final DateTimeFormatter formatter;
    
    public Logger(String filename) throws IOException {
        this.filename = filename;
        this.lock = new ReentrantLock();
        this.writer = new BufferedWriter(new FileWriter(filename, false));
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    }
    
    /**
     * Write a log entry (thread-safe)
     */
    public void log(String message) {
        lock.lock();
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = "[" + timestamp + "] " + message;
            writer.write(logEntry);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("ERROR: Failed to write log: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Close the logger
     */
    public void close() {
        lock.lock();
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("ERROR: Failed to close logger: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
