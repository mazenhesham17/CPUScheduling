import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

public class AGScheduler extends Scheduler {

    Vector<Vector<Pair<String, Integer>>> quantums;
    ProcessData currentProcess;
    ProcessInterval interval;
    Queue<ProcessData> readyQueue;
    PriorityQueue<ProcessData> priorityQueue;
    PriorityQueue<ProcessData> remainingQueue;
    int mode;

    AGScheduler(ProcessData[] processes, int contextSwitching, int quantum) {
        super(processes, contextSwitching, quantum);
        super.setName("AG");
        quantums = new Vector<>();
    }

    private void init() {
        mode = -1;
        currentProcess = null;
        interval = new ProcessInterval("waiting", 0);
        int n = processes.length;
        readyQueue = new LinkedList<>();
        priorityQueue = new PriorityQueue<>(n, new PriorityComparator());
        remainingQueue = new PriorityQueue<>(n, new RemainingTimeComparator());
        updateQuantum();
    }

    private void updateQuantum() {
        Vector<Pair<String, Integer>> pairs = new Vector<>();
        for (ProcessData processData : processes) {
            Pair<String, Integer> pair = new Pair<>();
            pair.setFirst(processData.getName());
            pair.setSecond(processData.getQuantum());
            pairs.add(pair);
        }
        quantums.add(pairs);
    }

    private int takeTop(int t) {
        if (readyQueue.isEmpty()) {
            currentProcess = null;
            mode = -1;
            interval = new ProcessInterval("waiting", t);
            return 0;
        } else {
            currentProcess = readyQueue.poll();
            priorityQueue.remove(currentProcess);
            remainingQueue.remove(currentProcess);
            mode = 0;
            interval = new ProcessInterval(currentProcess.getName(), t);
            return Math.min(currentProcess.getRemainingTime(), getFirst(currentProcess.getQuantum()));
        }
    }

    private void reset() {
        readyQueue.add(currentProcess);
        priorityQueue.add(currentProcess);
        remainingQueue.add(currentProcess);
    }

    private int updatePriority(int t) {
        // scenario 2
        int value = currentProcess.getQuantum();
        value -= (value + 3) / 4;
        currentProcess.increment((value + 1) / 2);
        updateQuantum();
        reset();
        currentProcess = priorityQueue.poll();
        readyQueue.remove(currentProcess);
        remainingQueue.remove(currentProcess);
        mode = 0;
        interval = new ProcessInterval(currentProcess.getName(), t);
        return Math.min(currentProcess.getRemainingTime(), getFirst(currentProcess.getQuantum()));
    }

    private int updateSJF(int t) {
        // scenario 3
        int timePast = currentProcess.getQuantum();
        timePast = (timePast + 1) / 2 + (interval.getEnd() == t ? 0 : interval.getDuration());
        int value = currentProcess.getQuantum() - timePast;
        currentProcess.increment(value);
        updateQuantum();
        reset();
        currentProcess = remainingQueue.poll();
        readyQueue.remove(currentProcess);
        priorityQueue.remove(currentProcess);
        mode = 0;
        interval = new ProcessInterval(currentProcess.getName(), t);
        return Math.min(currentProcess.getRemainingTime(), getFirst(currentProcess.getQuantum()));
    }

    private int getFirst(int value) {
        // ceil(25)
        return (value + 3) / 4;
    }

    private int getSecond(int value) {
        // ceil(50)
        return (value + 1) / 2 - (value + 3) / 4;
    }

    private int getRemaining(int value) {
        // remaining
        return value - (value + 1) / 2;
    }

    @Override
    void run() {
        init();
        int maxTime = 0;
        int n = processes.length;
        int pointer = 0;
        for (ProcessData process : processes) {
            maxTime = Math.max(maxTime, process.getArrivalTime());
        }
        int runTime = 0;
        for (int t = 0; t <= maxTime || !readyQueue.isEmpty() || currentProcess != null; t++) {
            if (pointer < n && processes[pointer].getArrivalTime() == t) {
                readyQueue.add(processes[pointer]);
                priorityQueue.add(processes[pointer]);
                remainingQueue.add(processes[pointer]);
                pointer++;
            }
            interval.setEnd(t);
            // if there is no running process take FCFS
            if (currentProcess == null) {
                runTime = takeTop(t);
            } else {
                if (interval.getDuration() == runTime) {
                    currentProcess.decrement(runTime);
                    timeLine.add(interval);
                    if (currentProcess.getRemainingTime() == 0) {
                        // process is finished
                        currentProcess.setEndTime(t);
                        // scenario 4
                        currentProcess.clear();
                        updateQuantum();
                        runTime = takeTop(t);
                    } else {
                        // process is not finished
                        if (mode == 0) {
                            if (!priorityQueue.isEmpty() && priorityQueue.peek().getPriority() < currentProcess.getPriority()) {
                                runTime = updatePriority(t);
                            } else {
                                interval = new ProcessInterval(currentProcess.getName(), t);
                                runTime = Math.min(currentProcess.getRemainingTime(), getSecond(currentProcess.getQuantum()));
                                mode++;
                            }
                        } else if (mode == 1) {
                            if (!remainingQueue.isEmpty() && remainingQueue.peek().getRemainingTime() < currentProcess.getRemainingTime()) {
                                runTime = updateSJF(t);
                            } else {
                                interval = new ProcessInterval(currentProcess.getName(), t);
                                runTime = Math.min(currentProcess.getRemainingTime(), getRemaining(currentProcess.getQuantum()));
                                mode++;
                            }
                        } else {
                            // scenario 1
                            currentProcess.increment(2);
                            updateQuantum();
                            reset();
                            runTime = takeTop(t);
                        }
                    }
                } else {
                    if (mode == 2) {
                        if (!remainingQueue.isEmpty() && remainingQueue.peek().getRemainingTime() < currentProcess.getRemainingTime()) {
                            runTime = updateSJF(t);
                        }
                    }
                }
            }
        }
        fixTimeLine();
    }

    @Override
    public void printDetails(FileWriter fileWriter) {
        super.printDetails(fileWriter);
        for (Vector<Pair<String, Integer>> pairs : quantums) {
            try {
                fileWriter.write("Quantum : ");
                for (Pair<String, Integer> pair : pairs) {
                    fileWriter.write("(" + pair.getFirst() + "," + pair.getSecond() + ") ");
                }
                fileWriter.write("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
