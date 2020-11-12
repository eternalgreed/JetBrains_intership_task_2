package runner;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import processor.Processor;

public class Iteration<T> {

    private final int number;
    private final List<Processor<T>> way;
    private final ConcurrentMap<String, T> resultById;

    public Iteration(int number, List<Processor<T>> way) {
        this.number = number;
        this.way = way;
        this.resultById = new ConcurrentHashMap<>();
    }

    public int getNumber() {
        return number;
    }

    public List<Processor<T>> getWay() {
        return way;
    }

    public T getResultById(String id) {
        return resultById.get(id);
    }

    public void addResult(String id, T result) {
        resultById.put(id, result);
    }

    public boolean isProcessorFinished(String id) {
        return resultById.containsKey(id);
    }

}
