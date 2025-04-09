package main.manager;

import main.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();
    private final Map<Integer, Task> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            history.remove(task);
        } else if (history.size() >= 10) {
            Task oldestTask = history.removeFirst();
            historyMap.remove(oldestTask.getId());
        }
        history.addLast(task);
        historyMap.put(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        Task task = historyMap.remove(id);
        if (task != null) {
            history.remove(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void clearHistory() {
        history.clear(); // Очищаем список истории
        historyMap.clear(); // Очищаем карту задач
    }
}