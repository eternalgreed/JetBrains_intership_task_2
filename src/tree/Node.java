package tree;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import processor.Processor;

/**
 * Нода
 *
 * @param <T> - тип результата процессора
 */
public class Node<T> {

    private final String id;
    private final Processor<T> processor;
    //зависимые от этой ноды
    private final Set<Node<T>> children;
    //необходимые для этой ноды
    private final Set<Node<T>> parents;

    public Node(Processor<T> processor) {
        this.processor = processor;
        this.id = processor.getId();
        children = new HashSet<>();
        parents = new HashSet<>();
    }

    /**
     * Важно: метод добавляет связь в том числе и в объект child.
     */
    public void addChild(Node<T> child) {
        checkNode(child, children);
        children.add(child);

        child.parents.add(this);
    }


    public Set<Node<T>> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Set<Node<T>> getParents() {
        return Collections.unmodifiableSet(parents);
    }

    public boolean isRoot() {
        return parents.isEmpty();
    }

    public boolean isLeave() {
        return children.isEmpty();
    }

    public String getId() {
        return id;
    }

    public Processor<T> getProcessor() {
        return processor;
    }

    /**
     * Проверить, что такой родитель/потомок еще не был добавлен
     */
    private void checkNode(Node<T> newNode, Set<Node<T>> existingNodes) {
        if (existingNodes.contains(newNode)) {
            throw new IllegalArgumentException(String.format("%s already added", newNode));
        }

        if (newNode == this) {
            throw new IllegalArgumentException(String.format("Can't add same node %s", newNode));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node<?> node = (Node<?>) o;
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                '}';
    }
}
