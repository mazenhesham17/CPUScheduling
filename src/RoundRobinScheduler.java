import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinScheduler extends Scheduler {
    RoundRobinScheduler(ProcessData[] processes, int contextSwitching, int quantum) {
        super(processes, contextSwitching, quantum);
        super.setName("Round Robin");
    }

    public void run() {
        Queue<ProcessData> readyQueue = new LinkedList<>();
        int maxTime = 0;
        int n = processes.length;
        int pointer = 0;
        ProcessInterval interval = new ProcessInterval("start", 0);
        ProcessData currentProcess = null;
        for (ProcessData process : processes) {
            maxTime = Math.max(maxTime, process.getArrivalTime());
        }
        for (int t = 0; t <= maxTime || (!readyQueue.isEmpty() || currentProcess != null); t++) {
            if (pointer < n && processes[pointer].getArrivalTime() == t) {
                readyQueue.add(processes[pointer]);
                pointer++;
            }
            interval.setEnd(t);
            if (currentProcess == null && (interval.getName() == "start" || (interval.getName() == "context" && interval.getDuration() == contextSwitching))) {
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.poll();
                    if (interval.getName() == "context") {
                        timeLine.add(interval);
                    }
                    interval = new ProcessInterval(currentProcess.getName(), t);
                }
            }

            if (interval.getName() != "context" && interval.getName() != "start") {
                int runTime = Math.min(quantum, currentProcess.getRemainingTime());
                if (interval.getDuration() == runTime) {
                    currentProcess.decrement(runTime);

                    if (currentProcess.getRemainingTime() == 0) {
                        currentProcess.setEndTime(t + contextSwitching);
                    } else {
                        readyQueue.add(currentProcess);
                    }
                    currentProcess = null;
                    timeLine.add(interval);
                    interval = new ProcessInterval("context", t);
                }
            }
        }
    }


}