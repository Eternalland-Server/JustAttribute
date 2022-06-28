package com.sakuragame.eternal.justattribute.util;

import com.sakuragame.eternal.justattribute.JustAttribute;
import org.bukkit.Bukkit;

import java.util.*;

public class Scheduler {

    private final static Map<UUID, List<Integer>> tasks = new HashMap<>();

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(JustAttribute.getInstance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(JustAttribute.getInstance(), runnable);
    }

    public static void runLater(Runnable runnable, int tick) {
        Bukkit.getScheduler().runTaskLater(JustAttribute.getInstance(), runnable, tick);
    }

    public static void runLaterAsync(Runnable runnable, long tick) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(JustAttribute.getInstance(), runnable, tick);
    }

    public static void runLaterAsync(UUID uuid, Runnable runnable, long tick) {
        int id = Bukkit.getScheduler().runTaskLaterAsynchronously(JustAttribute.getInstance(), runnable, tick).getTaskId();
        tasks.computeIfAbsent(uuid, v -> new ArrayList<>()).add(id);
    }

    public static void cancel(UUID uuid) {
        List<Integer> result = tasks.remove(uuid);
        if (result == null) return;

        result.forEach(i -> Bukkit.getScheduler().cancelTask(i));
    }
}
