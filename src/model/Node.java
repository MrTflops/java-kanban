package model;

public class Node<T extends Task> {
    public Task task;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, Task task, Node<T> next) {
        this.prev = prev;
        this.task = task;
        this.next = next;
    }

}