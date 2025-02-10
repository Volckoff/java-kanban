package manager;

import task.Task;

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
        removeNode(new Node(task, null, null));
        linkLast(task);
        nodeMap.put(task.getId(), last);
    }

    private void removeNode(Node removeNode) {
        Integer id = removeNode.task.getId();
        final Node node = nodeMap.remove(id);
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
                if (node.next == null) {
                    last = node.prev;
                } else {
                    node.next.prev = node.prev;
                }
            } else {
                first = node.next;
                if (first == null) {
                    last = null;
                } else {
                    first.prev = null;
                }
            }
        }
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            Node node = nodeMap.get(id);
            removeNode(node);
        }
    }
}