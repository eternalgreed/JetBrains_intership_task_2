package runner;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import processor.Processor;
import processor.ProcessorException;
import tree.Node;
import tree.WayGenerator;





public class SimpleRunner implements Runner<Long> {

    @Override
    public Map<String, List<Long>> runProcessors(Set<Processor<Long>> processors,
                                                 int maxThreads,
                                                 int maxIterations) {


        Map<String, Node<Long>> nodesByIds = initNodes(processors);
        addLinks(processors, nodesByIds);
        Set<Node<Long>> allNodes = new HashSet<>(nodesByIds.values());

        List<Processor<Long>> way = new ArrayList<>();
        for (Node<Long> longNode : WayGenerator.getWay(allNodes)) {
            Processor<Long> processor = longNode.getProcessor();
            way.add(processor);
        }

        List<Iteration<Long>> iterations = new ArrayList<>();
        for (int i = 0; i < maxIterations; i++) {
            Iteration<Long> longIteration = new Iteration<>(i, way);
            iterations.add(longIteration);
        }


        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

        List<Runnable> runnableList = new ArrayList<>();
        for (int i = 0; i < iterations.size(); i++) {
            Iteration<Long> iteration = iterations.get(i);
            if (i == 0) {
                runnableList.add(() -> {
                    executeIteration(iteration, null);
                });
            } else {
                Iteration<Long> previousIteration = iterations.get(i - 1);
                runnableList.add(() -> executeIteration(iteration, previousIteration));
            }
        }

        List<Future<?>> futures = new ArrayList<>();
        for (Runnable runnable : runnableList) {
            futures.add(executor.submit(runnable));
        }

        futures.forEach(x -> {
            try {
                x.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });


        executor.shutdown();
        return collectResult(iterations);
    }

    public Map<String, List<Long>> collectResult(List<Iteration<Long>> iterations) {
        Map<String, List<Long>> result = new HashMap<>();
        for (Iteration<Long> iteration : iterations) {
            for (Processor<Long> processor : iteration.getWay()) {
                String id = processor.getId();
                List<Long> list = result.computeIfAbsent(id, x -> new ArrayList<>());
                list.add(iteration.getResultById(id));
            }
        }
        return result;
    }

    //запускается в отдельном потоке
    private void executeIteration(Iteration<Long> iteration, Iteration<Long> previousIteration) {

        for (Processor<Long> processor : iteration.getWay()) {
            String id = processor.getId();
            waitPreviousRun(id, previousIteration);

            List<Long> input = processor.getInputIds().stream()
                    .map(iteration::getResultById)
                    .collect(Collectors.toList());

            System.out.println(String.format("Run processor: %s, iteration: %s", id, iteration.getNumber()));
            Long result = processor.process(input);
            System.out.println(String.format("Finish processor: %s, iteration: %s", id, iteration.getNumber()));
            iteration.addResult(id, result);
        }
    }

    /**
     * Убедиться, что этот процессор уже выполнился в предыдущей итерации
     */
    private void waitPreviousRun(String id, Iteration<Long> previousIteration) {
        if (previousIteration != null) {
            while (!previousIteration.isProcessorFinished(id)) {
                try {
                    System.out.println("wait... id:" + id);
//                    TimeUnit.MILLISECONDS.sleep(1L);
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Создать ноды без связей parent-child
     */
    private static Map<String, Node<Long>> initNodes(Set<Processor<Long>> processors) {
        Map<String, Node<Long>> nodesByIds = new HashMap<>();
        processors.forEach(x -> nodesByIds.put(x.getId(), new Node<>(x)));
        return nodesByIds;
    }

    /**
     * Добавить нодам связи parent-child
     */
    private static void addLinks(Set<Processor<Long>> processors, Map<String, Node<Long>> nodesByIds) {
        for (Processor<Long> processor : processors) {
            Node<Long> current = nodesByIds.get(processor.getId());
            List<String> parentsIds = processor.getInputIds() == null ? Collections.emptyList() : processor.getInputIds();

            checkUnknownIds(parentsIds, nodesByIds.keySet());

            parentsIds.stream()
                    .map(nodesByIds::get)
                    .forEach(parent -> parent.addChild(current));
        }
    }

    private static void checkUnknownIds(List<String> parentsIds, Set<String> existingIds) {
        if (!existingIds.containsAll(parentsIds)) {
            Set<String> unknownIds = new HashSet<>(parentsIds);
            unknownIds.removeAll(existingIds);
            throw new ProcessorException(
                    String.format("unknown input ids: %s", unknownIds));
        }
    }
}
