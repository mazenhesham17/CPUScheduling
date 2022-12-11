import java.util.Vector;

public class ShortestRemainingTimeFirstScheduler extends Scheduler {

    ShortestRemainingTimeFirstScheduler(ProcessData[] processes, int contextSwitching, int quantum) {
        super(processes, contextSwitching, quantum);
        super.setName("Preemptive Shortest Job First");
    }

    @Override
    void run() {
        int timer = 0, finish = 0, n = processes.length;
        ProcessInterval Interval = new ProcessInterval("start", 0);
        int min = Integer.MAX_VALUE;
        ProcessData currentProcess = null;
        while (finish < n) {
            boolean finishFlag = false , changeFlag = false ;
            for (ProcessData processData : processes) {
                if (processData.getRemainingTime() != 0 && processData.getArrivalTime() <= timer && processData.getRemainingTime() < min) {
                    if(min!=Integer.MAX_VALUE) {
                        changeFlag = true;
                    }
                    min = processData.getRemainingTime();
                    currentProcess = processData;
                }
            }
            if (currentProcess != null) {
                currentProcess.decrement(1);
                min-- ;
                if (currentProcess.getRemainingTime() == 0) {
                    finish++;
                    min = Integer.MAX_VALUE ;
                    finishFlag = true;
                }
                if(changeFlag && (!timeLine.isEmpty() && timeLine.lastElement().getName()!="context")) {
                    currentProcess.setEndTime(timer);
                    Interval = new ProcessInterval("context" , timer) ;
                    timer+=contextSwitching;
                    timeLine.add(Interval) ;
                    Interval.setEnd(timer);
                }
                Interval = new ProcessInterval(currentProcess.getName(), timer);
                Interval.setEnd(++timer);
                timeLine.add(Interval);
                if (finishFlag && (!timeLine.isEmpty() && timeLine.lastElement().getName()!="context")) {
                    currentProcess.setEndTime(timer + contextSwitching);
                    Interval = new ProcessInterval("context" , timer) ;
                    timer+=contextSwitching;
                    timeLine.add(Interval) ;
                    Interval.setEnd(timer);
                }
            }
            else{
                timer++;
            }
        }
        fixTimeLine();
    }
    private void fixTimeLine() {
        Vector<ProcessInterval> temp = new Vector<>();
        for (ProcessInterval processInterval : timeLine) {
            if (temp.isEmpty() || !temp.lastElement().getName().equals(processInterval.getName())) {
                temp.add(processInterval);
            } else {
                temp.lastElement().setEnd(processInterval.getEnd());
            }
        }
        timeLine = temp;
    }
}
