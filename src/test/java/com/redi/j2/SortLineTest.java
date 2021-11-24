package com.redi.j2;

import com.redi.j2.utils.SortLineProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SortLineTest {

    @BeforeEach
    void setUp() throws IOException {
        File tmp = new File("tmp");
        if (tmp.exists()) delete(tmp);
        tmp.mkdir();
    }

    public static void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : Objects.requireNonNull(f.listFiles())) delete(c);
        }
        if (!f.delete()) throw new FileNotFoundException("Failed to delete file: " + f);
    }

    @Test
    void should_sort_lines() throws Exception {
        shouldSortLines(
                IntStream.range(0, 1000)
                        .mapToObj(i -> UUID.randomUUID().toString())
                        .collect(Collectors.toList()));
    }

    @Test
    void should_sort_empty_file() throws Exception {
        shouldSortLines(new ArrayList<>());
    }

    private void shouldSortLines(List<String> lines) throws IOException, InvocationTargetException {
        String file = format("tmp/%s.txt", UUID.randomUUID());

        Files.write(Path.of(file), lines);

        SortLineProxy.sortLine(file);

        Collections.sort(lines);

        assertEquals(
                lines,
                Files.readAllLines(Path.of(file)),
                format("the file is not sorted: %s", file));
    }
}
