package main.manager;

import main.tasks.Task;
import java.util.List;

// Интерфейс для управления историей просмотров задач
public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
    void clearHistory();
}