package com.example.concurrency;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class SearchFile {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        System.out.println("please enter directory");
        Scanner scanner = new Scanner(System.in);
        String directory = scanner.nextLine();
        System.out.println("a keyword");
        String keyword = scanner.nextLine();
        MatchCounter match = new MatchCounter(new File(directory), keyword, service);
        Future<Integer> task = service.submit(match);
        try {

            System.out.println(task.get() + "file found");
        } catch (ExecutionException | InterruptedException e) {

        }
        service.shutdown();
        int size = ((ThreadPoolExecutor) service).getLargestPoolSize();
        System.out.println("largest size" + size);
    }

    static class MatchCounter implements Callable<Integer> {
        private File file;
        private String keyword;
        private ExecutorService service;

        public MatchCounter(File file, String keyword, ExecutorService service) {
            this.file = file;
            this.keyword = keyword;
            this.service = service;
        }

        public Integer call() {
            int count = 0;
            List<Future<Integer>> tasks = new ArrayList<>();
            File[] files = file.listFiles();
            for (File file : files) {

                if (file.isDirectory()) {
                    MatchCounter match = new MatchCounter(file, keyword, service);
                    Future<Integer> task = service.submit(match);
                    tasks.add(task);
                } else {
                    if (search(file)) count++;
                }
            }
            for (Future<Integer> task : tasks) {
                try {
                    count += task.get();
                } catch (ExecutionException | InterruptedException e) {

                }
            }

            return count;
        }

        public boolean search(File file) {
            boolean found = false;
            try {

                Scanner in = new Scanner(file);
                while (!found && in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.contains(keyword)) {
                        found = true;
                    }
                }
            } catch (FileNotFoundException e) {

            }

            return found;
        }

    }
}
