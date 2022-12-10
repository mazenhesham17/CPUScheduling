public class ShortestRemainingTimeFirstScheduler extends Scheduler {

    ShortestRemainingTimeFirstScheduler(ProcessData[] processes, int contextSwitching, int quantum) {
        super(processes, contextSwitching, quantum);
        super.setName("Preemptive Shortest Job First");
    }

    @Override
    void run() {
        int timer = 0, n = processes.length, finish = 0;
        ProcessInterval interval = new ProcessInterval("start", timer);
        while (finish < n) {
            int min = Integer.MAX_VALUE;
            boolean flag = false;
            ProcessData currentProcess = null;
            for (ProcessData processData : processes) {
                if (processData.remainingTime != 0 && processData.getArrivalTime() <= timer && processData.getBurstTime() < min) {
                    min = processData.getBurstTime();
                    currentProcess = processData;
                }
            }
            timer++;
            if (currentProcess != null) {
                currentProcess.decrement(1);
                flag = true;
            }
            if (currentProcess.getRemainingTime() == 0) {
                finish++;
                flag = true;
            }
            if (flag == true) {
                currentProcess.setEndTime(timer + contextSwitching);
                interval = new ProcessInterval(currentProcess.getName(), timer);
            }
            timeLine.add(interval);
        }
    }
}
