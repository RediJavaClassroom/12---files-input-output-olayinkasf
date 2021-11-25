package com.redi.j2;

import com.redi.j2.utils.CSVStudentsProxy;
import com.redi.j2.utils.ReflectionUtils;
import com.redi.j2.utils.StudentProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.redi.j2.SortLineTest.delete;
import static com.redi.j2.utils.ReflectionUtils.exception;
import static com.redi.j2.utils.ReflectionUtils.getCSVStudentClass;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class CSVStudentsTest {

    Random random = new Random();

    @BeforeEach
    void setUp() throws IOException {
        File tmp = new File("tmp");
        if (tmp.exists()) delete(tmp);
        tmp.mkdir();
    }

    @Test
    void _2_should_have_only_one_private_no_arg_constructor() {
        Constructor<?>[] allConstructors = ReflectionUtils.getAllConstructors(getCSVStudentClass());

        assertEquals(allConstructors.length, 1, "CSVStudents should have only one constructor");
        assertEquals(
                allConstructors[0].getParameterCount(),
                0,
                "CSVStudents constructor should have no parameters");
        assertTrue(
                Modifier.isPrivate(allConstructors[0].getModifiers()),
                "CSVStudents constructor must be private");
    }

    @Test
    void _3_1_read_should_throw_BadCSVException_if_csv_file_extension_is_not_csv() {
        assertNotNull(exception("BadCSV"), "Please create a runtime exception BadCSVException");
        assertThrownRead(
                randomUUID().toString(),
                exception("BadCSV"),
                "'read' should BadCSVException if file extension is not '.csv'");
    }

    @Test
    void _3_2_read_should_throw_IOException_if_csv_file_does_not_exist() {
        assertThrownRead(
                format("%s.csv", randomUUID()),
                FileNotFoundException.class,
                "'read' should throw NoSuchFileException if csv file does not exist");
    }

    @Test
    void _3_3_read_should_throw_BadCSVException_if_csv_file_header_is_wrong_order()
            throws IOException {
        String path = format("tmp/%s.csv", randomUUID());

        Files.write(
                Path.of(path),
                String.join(",", "id", "firstName", "lastName", "weight", "height", "dateOfBirth")
                        .getBytes());

        assertThrownRead(
                path,
                exception("BadCSV"),
                "'read' should BadCSVException if csv file has bad headers");
    }

    @Test
    void _3_3_read_should_throw_BadCSVException_if_csv_file_header_is_wrong_typo()
            throws IOException {
        String path = format("tmp/%s.csv", randomUUID());

        Files.write(
                Path.of(path),
                String.join(",", "id", "firstName", "last0Name", "height", "weight", "dateOfBirth")
                        .getBytes());

        assertThrownRead(
                path,
                exception("BadCSV"),
                "'read' should BadCSVException if csv file has bad headers");
    }

    @Test
    void _3_3_read_should_throw_BadCSVException_if_csv_file_header_is_wrong_incomplete()
            throws IOException {
        String path = format("tmp/%s.csv", randomUUID());

        Files.write(
                Path.of(path),
                String.join(",", "id", "firstName", "last0Name", "height", "weight").getBytes());

        assertThrownRead(
                path,
                exception("BadCSV"),
                "'read' should BadCSVException if csv file has bad headers");
    }

    @Test
    void _3_3_read_should_throw_BadCSVException_if_csv_file_header_is_wrong_completely()
            throws IOException {
        String path = format("tmp/%s.csv", randomUUID());

        Files.write(
                Path.of(path),
                String.join(
                                ",",
                                randomUUID().toString(),
                                randomUUID().toString(),
                                randomUUID().toString(),
                                randomUUID().toString(),
                                randomUUID().toString(),
                                randomUUID().toString())
                        .getBytes());

        assertThrownRead(
                path,
                exception("BadCSV"),
                "'read' should BadCSVException if csv file has bad headers");
    }

    @Test
    void _3_7_read_should_return_list_of_students_in_the_right_order()
            throws IOException, InvocationTargetException {
        List<Map.Entry<StudentProxy, String>> students =
                List.of(
                        randomStudent(),
                        randomStudent(),
                        randomStudent(),
                        randomStudent(),
                        randomStudent(),
                        randomStudent(),
                        randomStudent());

        String path = format("tmp/%s.csv", randomUUID());
        Files.write(
                Path.of(path),
                List.of(
                        String.join(
                                ",",
                                "id",
                                "firstName",
                                "lastName",
                                "height",
                                "weight",
                                "dateOfBirth")));
        Files.write(
                Path.of(path),
                students.stream().map(Map.Entry::getValue).collect(Collectors.toList()),
                StandardOpenOption.APPEND);

        assertEquals(
                CSVStudentsProxy.read(path),
                students.stream()
                        .map(Map.Entry::getKey)
                        .map(StudentProxy::getTarget)
                        .collect(Collectors.toList()));
    }

    @Test
    void _4_1_write_should_throw_BadCSVException_if_csv_file_extension_is_not_csv() {
        assertThrownWrite(
                format("tmp%s", randomUUID()),
                randomStudent().getKey(),
                exception("BadCSV"),
                "'write' should throw BadCSVException if file extension is not '.csv'");
    }

    @Test
    void _4_2_write_should_write_student_to_new_file()
            throws InvocationTargetException, IOException {
        String path = format("tmp/%s/%s/%s.csv", randomUUID(), randomUUID(), randomUUID());
        Map.Entry<StudentProxy, String> student = randomStudent();

        CSVStudentsProxy.write(path, student.getKey());

        assertTrue(
                new File(path).exists(),
                "'write' should create new CSV file and all leading directories");
        assertEquals(
                Files.readAllLines(Path.of(path)).get(0),
                String.join(",", "id", "firstName", "lastName", "height", "weight", "dateOfBirth"),
                "'write' should create CSV file with headers in the right order");
        assertEquals(
                Files.readAllLines(Path.of(path)).get(1),
                student.getValue(),
                "'write' should append a CSV record for the student");
        assertEquals(
                CSVStudentsProxy.read(path),
                List.of(student.getKey().getTarget()),
                "'write' should append a CSV record for the student");
        assertTrue(
                Files.readString(Path.of(path)).contains(student.getValue()),
                "'write' should append a CSV record for the student");
        assertTrue(
                Files.readAllLines(Path.of(path)).contains(student.getValue()),
                "'write' should append a CSV record for the student");
    }

    @Test
    void _4_3_write_should_write_student_to_existing_file()
            throws InvocationTargetException, IOException {
        String path = format("tmp/%s/%s/%s.csv", randomUUID(), randomUUID(), randomUUID());
        List<Map.Entry<StudentProxy, String>> students =
                List.of(
                        randomStudent(),
                        randomStudent(),
                        randomStudent(),
                        randomStudent(),
                        randomStudent(),
                        randomStudent(),
                        randomStudent());

        for (Map.Entry<StudentProxy, String> entry : students) {
            CSVStudentsProxy.write(path, entry.getKey());
        }

        assertEquals(
                CSVStudentsProxy.read(path),
                students.stream()
                        .map(Map.Entry::getKey)
                        .map(StudentProxy::getTarget)
                        .collect(Collectors.toList()),
                "'write' should append new CSV record for each student");
    }

    private Map.Entry<StudentProxy, String> randomStudent() {
        String firstName = UUID.randomUUID().toString();
        UUID id = UUID.randomUUID();
        String lastName = UUID.randomUUID().toString();
        int height = random.nextInt(Integer.MAX_VALUE);
        int weight = random.nextInt(Integer.MAX_VALUE);
        LocalDate dateOfBirth = LocalDate.now();

        return Map.entry(
                new StudentProxy(id, firstName, lastName, height, weight, dateOfBirth),
                format("%s,%s,%s,%s,%s,%s", id, firstName, lastName, height, weight, dateOfBirth));
    }

    private static void assertThrownRead(
            String path, Class<? extends Exception> ex, String message) {
        try {
            CSVStudentsProxy.read(path);
            fail(message);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            assertEquals(e.getCause().getClass(), ex, message);
        }
    }

    private static void assertThrownWrite(
            String path, StudentProxy proxy, Class<? extends Exception> ex, String message) {
        try {
            CSVStudentsProxy.write(path, proxy);
            fail(message);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            assertEquals(e.getCause().getClass(), ex, message);
        }
    }
}
