package tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import processor.ProcessorException;

import static java.util.stream.Collectors.toSet;

public class WayGenerator {
    private WayGenerator() {
    }

    /**
     * Получить путь от корней до листьев в порядке зависимости нод друг от друга
     *
     * @param allNodes - все ноды
     * @return упорядоченный набор нод - путь от листа до корней
     */
    public static <T> List<Node<T>> getWay(Set<Node<T>> allNodes) throws ProcessorException {

        Set<Node<T>> roots = new HashSet<>();
        for (Node<T> allNode : allNodes) {
            if (allNode.isRoot()) {
                roots.add(allNode);
            }
        }
        Queue<Node<T>> queue = new ArrayDeque<>(roots);

        // way - он же visitedSet
        Set<Node<T>> way = new LinkedHashSet<>();

        Node<T> currentNode;

        while (!queue.isEmpty()) {
            currentNode = queue.remove();
            way.add(currentNode);

            for (Node<T> child : currentNode.getChildren()) {// если child уже был в списке, то идем дальше
                if (way.contains(child)) {
                    continue;
                }

                // можем добавить в список только если у child все parent были посещены
                boolean allParentsVisited = way.containsAll(child.getParents());
                if (allParentsVisited) {
                    queue.add(child);
                }
            }
        }

        checkUnattainableNodes(allNodes, way);

        return new ArrayList<>(way);
    }

    /**
     * Проверить, что нет недостижимых нод.
     * Недостижимые ноды будут при наличии циклических зависимостей.
     */
    private static <T> void checkUnattainableNodes(Set<Node<T>> allNodes, Set<Node<T>> visitedSet) throws ProcessorException {
        Set<Node<T>> unattainableNodes = new HashSet<>(allNodes);
        unattainableNodes.removeAll(visitedSet);
        boolean hasUnattainableNodes = !unattainableNodes.isEmpty();

        if (hasUnattainableNodes) {
            throw new ProcessorException(
                    String.format("Dependency graph contains unattainable nodes: %s", unattainableNodes));
        }

    }

}
