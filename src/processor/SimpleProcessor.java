package processor;

import java.util.List;

public class SimpleProcessor implements Processor<Long> {

    private final String id;
    private final List<String> inputIds;

    public SimpleProcessor(String id, List<String> inputIds) {
        this.id = id;
        this.inputIds = inputIds;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<String> getInputIds() {
        return inputIds;
    }


    @Override
    public Long process(List<Long> input) {
        if (input.isEmpty()) {
            return (long) (Math.random() * 10);
        }
        boolean seen = false;
        Long acc = null;
        for (Long aLong : input) {
            if (!seen) {
                seen = true;
                acc = aLong;
            } else {
                acc = acc + aLong;
            }
        }
        return seen ? acc : 0L;
    }
}
