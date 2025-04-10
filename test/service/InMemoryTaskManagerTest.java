package service;

import main.InMemoryTaskManager;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void testGetTask() {
        Task task = new Task("Test", "Description", Status.NEW);
        manager.addTask(task);

        Task result = manager.getTask(task.getId());
        assertEquals(task, result);
    }
}