package main;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }

        linkLast(task);
        historyMap.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        Node node = historyMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;

        while (current != null) {
            history.add(current.task);
            current = current.next;
        }

        return history;
    }

    // добавляем задачу в конец списка
    private void linkLast(Task task) {
        Node newNode = new Node(task);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    // удаляем узел
    private void removeNode(Node node) {
        if (node == null) return;

        if (node == head && node == tail) {  // если один элемент
            head = null;
            tail = null;
        } else if (node == head) {           // удаление головы
            head = node.next;
            if (head != null) {
                head.prev = null;
            }
        } else if (node == tail) {           // удаление хвоста
            tail = node.prev;
            if (tail != null) {
                tail.next = null;
            }
        } else {                             // удаляем из середины
            Node prev = node.prev;
            Node next = node.next;

            if (prev != null) {
                prev.next = next;
            }
            if (next != null) {
                next.prev = prev;
            }
        }
    }
}
