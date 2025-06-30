package main.manager;

import main.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    private Node head;
    private Node tail;
    private final Map<Integer, Node> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) return;

        // Если уже есть — удаляем из текущей позиции
        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
        }

        Node newNode = new Node(task);

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        historyMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node node = historyMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.task);
            current = current.next;
        }
        return result;
    }

    @Override
    public void clearHistory() {
        head = tail = null;
        historyMap.clear();
    }
}