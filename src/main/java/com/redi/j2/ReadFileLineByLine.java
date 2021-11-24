package com.redi.j2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Different ways to read the content of a file line by line and write them into another file. Note
 * that all these methods are still dangerous because one could in theory have a file that has a
 * line that is so long it doesn't fit in memory
 */
public class ReadFileLineByLine {

    /**
     * This incredible tedious way of reading from and writing to a file creates the reader and
     * writer in a try...catch block and closes them in a finally block. Note that when closing the
     * reader and writer that the IOException is handled. This is very important as must ensure that
     * the final block executes smoothly and completely.
     */
    public static void theHardWay() throws IOException {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader("input-file.txt"));
            writer = new BufferedWriter(new FileWriter("output-file.txt"));

            String next;

            while ((next = reader.readLine()) != null) {
                writer.write(next);
                writer.newLine();
            }

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This easier way of reading from and writing to a file creates both the reader and writer in a
     * try with resources block so no need to close the reader and writer in a finally that the
     * IOException is handled. This approach is cleaner and more compact than the hard approach. By
     * using a try with resources, we ensure that the reader and writer are closed.
     */
    public static void theEasierWay() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("input-file.txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter("output-file.txt"))) {

            String next;

            while ((next = reader.readLine()) != null) {
                writer.write(next);
                writer.newLine();
            }
        }
    }

    /**
     * This simple yet dangerous approach delegates reading the entire file to a method that is in
     * the java API. It is considered dangerous because the entire file could be extremely large and
     * does not fit in the memory. Use this only when you are certain that the file content is small
     * enough to fit in memory.
     */
    public static void theSimpleButDangerousWay() throws IOException {
        String rawContent = Files.readString(Path.of("input-file.txt"));
        Files.write(Path.of("output-file.txt"), rawContent.getBytes());
    }

    /**
     * This simple yet dangerous approach delegates reading the entire file to a method that is in
     * the java API. It is considered dangerous because the entire file could be extremely large and
     * does not fit in the memory. Use this only when you are certain that the file content is small
     * enough to fit in memory.
     */
    public static void anotherSimpleButDangerousWay() throws IOException {
        List<String> rawContent = Files.readAllLines(Path.of("input-file.txt"));
        Files.write(Path.of("output-file.txt"), rawContent);
    }
}
