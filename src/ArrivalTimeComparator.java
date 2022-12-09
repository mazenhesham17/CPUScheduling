import java.util.Comparator;

public class ArrivalTimeComparator implements Comparator<ProcessData> {

    @Override
    public int compare(ProcessData o1, ProcessData o2) {
        if (o1.getArrivalTime() == o2.getArrivalTime())
            return 0;
        return (o1.getArrivalTime() < o2.getArrivalTime() ? -1 : 1);
    }
}
