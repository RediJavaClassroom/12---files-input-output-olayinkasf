package com.redi.j2;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortLine {
  public static void sortLine(String path) throws IOException {
    List<String> lines = new ArrayList<>();

    // read the file content line by line and store them in a list.
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
      String line;

      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    }

    Collections.sort(lines);

    // write the content of our list into the original file
    // the writer is created without append=true because we want to overwrite the initial content of
    // the file with the sorted content
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      for (String line : lines) {
        writer.write(line);
        writer.newLine();
      }
    }
  }
}
