import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import processor.ProcessorException;
import processor.SimpleProcessor;
import runner.SimpleRunner;


public class Main {

    public static void main(String[] args) throws ProcessorException {
        SimpleProcessor proc1 = new SimpleProcessor("1", Collections.emptyList());
        SimpleProcessor proc2 = new SimpleProcessor("2", Collections.emptyList());
        SimpleProcessor proc3 = new SimpleProcessor("3", List.of("1", "2"));

        SimpleRunner simpleRunner = new SimpleRunner();
        Map<String, List<Long>> resultMap = simpleRunner.runProcessors(Set.of(proc1, proc2, proc3), 1, 2);

        System.out.println(resultMap);
    }

}
