package org.openbot.projects;

import java.util.HashMap;
import java.util.Map;

public class TaskStorage {
    private Map<String, String> taskMap = new HashMap<>();

    public void addTask(String key, String task) {
        taskMap.put(key, task);
    }

    public void removeTask(String key) {
        taskMap.remove(key);
    }

    public Map<String, String> getAllTasks() {
        return taskMap;
    }

    public void clearTasks() {
        taskMap.clear();
    }
}

