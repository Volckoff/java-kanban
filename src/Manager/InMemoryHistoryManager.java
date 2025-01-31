package Manager;

import Task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public static class Node {

        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", prev=" + prev +
                    ", next=" + next +
                    '}';
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();

    private Node first;

    private Node last;

    private List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    private void linkLast(Task task) {
        final Node node = new Node(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        removeNode(task.getId());
        linkLast(task);
        nodeMap.put(task.getId(), last);
    }

    private void removeNode(int id) {
        final Node node = nodeMap.remove(id);
        if (node != null) {
            if (node.prev == null) {
                first = node.next;
                if (node.next != null) {
                    node.next.prev = null;
                }
            } else if (node.next != null && node.prev != null) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            } else {
                last = node.prev;
                if (!(node.prev == null)) {
                    node.prev.next = null;
                }
            }
        }
        if (nodeMap.isEmpty()) {
            first = null;
            last = null;
        }
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }
}