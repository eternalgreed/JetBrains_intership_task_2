package test;

import java.util.Set;

import processor.ProcessorException;
import processor.SimpleProcessor;
import tree.Node;

import static tree.WayGenerator.getWay;
import static java.util.Collections.emptyList;

class WayGeneratorTest {

    public static void main(String[] args) throws ProcessorException {

       /* simple_one_way();
        two_leaves_two_roots();
        simple_success();
        simple_cycles();
        success();
        two_nodes_loop();
*/
    }

    private static void simple_one_way() throws ProcessorException {
        Node<Long> node1 = createTestNode("1");
        Node<Long> node2 = createTestNode("2");
        Node<Long> node3 = createTestNode("3");

        node1.addChild(node2);
        node1.addChild(node3);

        node2.addChild(node3);

        Set<Node<Long>> allNodes = Set.of(node1, node2, node3);


        System.out.println("WAY:");
        getWay(allNodes).forEach(System.out::println);

    }

    private static void two_leaves_two_roots() throws ProcessorException {
        Node<Long> node1 = createTestNode("1");
        Node<Long> node2 = createTestNode("2");
        Node<Long> node3 = createTestNode("3");
        Node<Long> node4 = createTestNode("4");
        Node<Long> node5 = createTestNode("5");
        Node<Long> node6 = createTestNode("6");
        Node<Long> node7 = createTestNode("7");

        node1.addChild(node3);
        node2.addChild(node3);

        node3.addChild(node4);

        node4.addChild(node5);
        node4.addChild(node6);
        node4.addChild(node7);

        node5.addChild(node6);

        Set<Node<Long>> allNodes = Set.of(node1, node2, node3, node4, node5, node6, node7);

        System.out.println("WAY:");
        getWay(allNodes).forEach(System.out::println);
    }

    private static void simple_success() throws ProcessorException {
        Node<Long> node1 = createTestNode("1");
        Node<Long> node2 = createTestNode("2");
        Node<Long> node3 = createTestNode("3");

        node1.addChild(node3);
        node2.addChild(node3);

        Set<Node<Long>> allNodes = Set.of(node1, node2, node3);

        System.out.println("WAY:");
        getWay(allNodes).forEach(System.out::println);
    }


    private static void simple_cycles() throws ProcessorException {
        Node<Long> node1 = createTestNode("1");
        Node<Long> node2 = createTestNode("2");
        Node<Long> node3 = createTestNode("3");
        Node<Long> node4 = createTestNode("4");

        node1.addChild(node2);
        node2.addChild(node3);
        node3.addChild(node4);
        node4.addChild(node2);

        Set<Node<Long>> allNodes = Set.of(node1, node2, node3, node4);

        System.out.println("WAY:");
        getWay(allNodes).forEach(System.out::println);
    }


    private static void success() throws ProcessorException {
        Node<Long> node1 = createTestNode("1");
        Node<Long> node2 = createTestNode("2");
        Node<Long> node3 = createTestNode("3");
        Node<Long> node4 = createTestNode("4");
        Node<Long> node5 = createTestNode("5");

        node1.addChild(node3);
        node2.addChild(node3);

        node3.addChild(node4);
        node3.addChild(node5);

        node4.addChild(node5);

        Set<Node<Long>> allNodes = Set.of(node1, node2, node3, node4, node5);

        System.out.println("WAY:");
        getWay(allNodes).forEach(System.out::println);
    }


    private static void two_nodes_loop() throws ProcessorException {
        Node<Long> node1 = createTestNode("1");
        Node<Long> node2 = createTestNode("2");

        node1.addChild(node2);
        node2.addChild(node1);

        Set<Node<Long>> allNodes = Set.of(node1, node2);

        System.out.println("WAY:");
        getWay(allNodes).forEach(System.out::println);
    }

    private static Node<Long> createTestNode(String id) {
        return new Node<>(new SimpleProcessor(id, emptyList()));
    }

}