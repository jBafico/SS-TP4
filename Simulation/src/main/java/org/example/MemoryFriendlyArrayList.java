package org.example;

import java.util.ArrayList;
import java.util.List;

public class MemoryFriendlyArrayList<T> {
    private record Wrapper<T>(T elem, int index) {}

    private final int saveEveryN; // The arraylist will save one every N elements passed to it
    private final int allowLastN; // The arraylist will allow the last N elements to be saved avoiding the loss of data
    private final ArrayList<Wrapper<T>> arrayList = new ArrayList<>();
    private int counter = 0;

    public MemoryFriendlyArrayList(int saveEveryN, int allowLastN) {
        if (saveEveryN < 1 || allowLastN < 1){
            throw new IllegalArgumentException("saveEveryN and allowLastN must be greater than 0");
        }
        this.saveEveryN = saveEveryN;
        this.allowLastN = allowLastN;
    }

    public boolean add(T t) {
        Wrapper<T> wrapper = new Wrapper<>(t, counter++);

        if (arrayList.size() > allowLastN){
            Wrapper<T> maybeToDelete = arrayList.get(arrayList.size() - allowLastN);
            if (maybeToDelete.index() % saveEveryN != 0){
                arrayList.remove(arrayList.size() - allowLastN);
            }
        }

        return arrayList.add(wrapper);
    }

    public T getFirst() {
        return arrayList.getFirst().elem();
    }

    public void addFirst(T elem) {
        Wrapper<T> wrapper = new Wrapper<>(elem, -1);
        arrayList.addFirst(wrapper);
    }

    public void removeFirst() {
        arrayList.removeFirst();
    }

    public T getLast() {
        return arrayList.getLast().elem();
    }

    public T getSecondLast() {
        return arrayList.get(arrayList.size() - 2).elem();
    }

    public List<T> getList() {
        List<T> list = new ArrayList<>();
        for (Wrapper<T> wrapper : arrayList) {
            list.add(wrapper.elem());
        }
        return list;
    }
}

