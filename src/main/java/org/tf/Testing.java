package org.tf;

public class Testing {
    private final long time;
    private final long operations;
    public Testing(int[] array) {
        SmoothSortService smoothSort = new SmoothSortService();
        smoothSort.checkAndSort(array);
        time = smoothSort.getTime();
        operations = smoothSort.getOperations();
    }

    public long getTime() {
        return time;
    }

    public long getOperations() {
        return operations;
    }
}
