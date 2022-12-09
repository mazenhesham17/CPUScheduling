import java.util.Comparator;

public class TimeComparator implements Comparator<ProcessData> {

    @Override
    public int compare(ProcessData o1, ProcessData o2) {
        return (o1.getArrivalTime() < o2.getArrivalTime() ? 1 : 0);
    }
}
