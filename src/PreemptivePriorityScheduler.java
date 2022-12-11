import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class PreemptivePriorityScheduler extends Scheduler {
    PriorityQueue<ProcessData> readyQueue;
    LinkedList<Pair<String, Integer>> temp;

    PreemptivePriorityScheduler(ProcessData[] processes, int contextSwitching, int quantum) {
        super(processes, contextSwitching, quantum);
        super.setName("Preemptive Priority");
        int n = processes.length;
        readyQueue = new PriorityQueue<>(n, new PriorityComparator());
        temp = new LinkedList<>();
    }

    private void aging(int currentTime) {
        Iterator<ProcessData> it = readyQueue.iterator();
        while (it.hasNext()) {
            ProcessData processData = it.next();
            if (processData.getRemainingTime() == processData.getBurstTime() && currentTime - processData.getArrivalTime() >= 15) {
                processData.setPriority(processData.getPriority() - 1);
                System.out.println(processData.getName() + " priority changed");
            }
        }
    }

    @Override
    void run() {
        int time = 0;
        int index = 0;
        int n = processes.length;
        while (index < n || !readyQueue.isEmpty()) {
            // check if process arrived
            while (index < n && processes[index].getArrivalTime() == time) {
                ProcessData processData = processes[index];
                readyQueue.add(processData);
                index++;
            }
            //get the most important process
            ProcessData processData = readyQueue.remove();
            processData.setRemainingTime(processData.getRemainingTime() - 1);
            time++;
            Pair<String, Integer> pair = new Pair<>(processData.getName(), time);
            if (!temp.isEmpty() && processData.getName().equals(temp.peekLast().getFirst()))
                temp.pollLast();
            temp.add(pair);
            if (processData.getRemainingTime() > 0) // still need more time
                readyQueue.add(processData);
            else // jop finished
                processData.setEndTime(time);
            if (time % 15 == 0)
                aging(time);
        }
        fixTimeLine();
    }

    @Override
    protected void fixTimeLine() {
        int last = Integer.MAX_VALUE;
        for (ProcessData processData : processes) {
            last = Math.min(last, processData.getArrivalTime());
        }
        for (Pair<String, Integer> pair : temp) {
            ProcessInterval processInterval = new ProcessInterval(pair.getFirst(), last);
            processInterval.setEnd(pair.getSecond());
            last = pair.getSecond();
            timeLine.add(processInterval);
        }
    }

}
