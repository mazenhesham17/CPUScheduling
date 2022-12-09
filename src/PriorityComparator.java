import java.util.Comparator;

public class PriorityComparator implements Comparator<ProcessData> {
    @Override
    public int compare(ProcessData o1, ProcessData o2) {
        if (o1.getPriority() == o2.getPriority())
            return 0;
        return (o1.getPriority() < o2.getPriority() ? -1 : 1);
    }
}
