package com.redi.j2;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CSVStudents {

    private CSVStudents() {}

    private static String[] HEADER = {
        "id", "firstName", "lastName", "height", "weight", "dateOfBirth"
    };

    public static List<Student> read(String path) throws IOException {
        List<Student> students = new ArrayList<>();

        if (!"csv".equalsIgnoreCase(getExtension(path))) throw new BadCSVException();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            String line;
            boolean seenHeader = false;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (!seenHeader) {
                    if (!Arrays.equals(HEADER, row)) throw new BadCSVException();
                    seenHeader = true;
                } else {
                    UUID id = UUID.fromString(row[0]);
                    String firstName = row[1];
                    String lastName = row[2];
                    int height = Integer.parseInt(row[3]);
                    int weight = Integer.parseInt(row[4]);
                    LocalDate dateOfBirth = LocalDate.parse(row[5]);

                    Student student =
                            new Student(id, firstName, lastName, height, weight, dateOfBirth);

                    students.add(student);
                }
            }
        }

        return students;
    }

    public static void write(String path, Student student) throws IOException {
        if (!"csv".equalsIgnoreCase(getExtension(path))) throw new BadCSVException();

        File csvFile = new File(path);

        if (!csvFile.exists()) {
            File parentFile = csvFile.getParentFile();

            parentFile.mkdirs();

            writToCsvFile(csvFile, HEADER);
        }

        writToCsvFile(
                csvFile,
                student.getId().toString(),
                student.getFirstName(),
                student.getLastName(),
                String.valueOf(student.getHeight()),
                String.valueOf(student.getWeight()),
                student.getDateOfBirth().toString());
    }

    private static void writToCsvFile(File file, String... row) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(String.join(",", row));
            writer.newLine();
        }
    }

    private static String getExtension(String path) {
        String[] strings = path.split("\\.");

        return strings[strings.length - 1];
    }

    // todo check for -1
    // do not use until fixed
    private static String getExtensionAnother(String path) {
        return path.substring(path.lastIndexOf("."));
    }
}
