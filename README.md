[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-f059dc9a6f8d3a56e377f745f24479a46679e63a5d9fe6f495e02850cd0d8118.svg)](https://classroom.github.com/online_ide?assignment_repo_id=6442833&assignment_repo_type=AssignmentRepo)
# [Files Input & Output](https://redi-school.github.io/intermediate-java/)

You have been provided by the class  [`ReadFileLineByLine`](src/main/java/com/redi/j2/ReadFileLineByLine.java)
with two methods demonstrating how to copy from one file to another file line by line.

Please go through those methods and observe the differences between them.

**For these exercises, please only
use [`ReadFileLineByLine#theEasierWay`](src/main/java/com/redi/j2/ReadFileLineByLine.java) to read or write from the
file.**

## Exercise

**For these exercises, please only
use [`ReadFileLineByLine#theEasierWay`](src/main/java/com/redi/j2/ReadFileLineByLine.java) to read or write from the
file.**

### 1 - Sort Line

Write a method that sorts a given file line by line. At the end of the execution, the file must be sorted.

In the package [`com.redi.j2`](src/main/java/com/redi/j2), create a public class `SortLine`, and in this class create a
public static method `sortLine`. Input of the method should be a `String path` which is the path to the file that needs
to be sorted.

For example if I have a file `/path/to/my/MyFile.txt` with the following content

```text
This is because we feel pain.
Land is good.
Who's calling on the phone this late at night?
The quick brown fox jump over the lazy dog.
A high APR is a bad thing.
This is my younger brother.
Lenora only had eight fingers, after losing both of her pinkies to a freak accident with a hay baler.
```

After I invoke `SortLine.sortLine("/path/to/my/MyFile.txt")`, then the file should have the following content.

```text
A high APR is a bad thing.
Land is good.
Lenora only had eight fingers, after losing both of her pinkies to a freak accident with a hay baler.
The quick brown fox jump over the lazy dog.
This is because we feel pain.
This is my younger brother.
Who's calling on the phone this late at night?
```

### 2 - CSV Students

1. In the package [`com.redi.j2`](src/main/java/com/redi/j2), create a class `Student` to represent a student
    1. with the following properties
        - `UUID id` a unique identifier of the student
        - `String firstName`
        - `String lastName`
        - `int height`
        - `int weight`
        - `LocalDate dateOfBirth`
    2. Add a constructor for `Student` with all the properties in the order specified above.
    3. All properties of `Student` must be private and final.
    4. Please implement getters, `equals`, `hashCode` and `toString`
2. Validations - All exceptions are custom and must be unchecked i.e. should extend `RuntimeException`
    1. if the `firstName` or `lastName` is empty, throw a custom `BadNameException`
    2. if the `height` is not positive throw a `BadHeightException`
    3. if the `weight` is not positive throw a `BadWeightException`
    4. if the `dateOfBirth` is in the future, throw a `BadDateException`
    4. if any property is null, throw `NullPointerException`.
3. In the package [`com.redi.j2`](src/main/java/com/redi/j2), create a class `CSVStudents`
    1. **In this exercise, DO NOT CATCH or HANDLE any `IOException`!!**
    2. Create a private no-arg constructor for `CSVStudents`. We do this for utility classes so that they cannot be
       initialized nor inherited from. An utility class is a class that only has static methods.
    3. Create a public static method `read(path)` which accepts a `String` path and reads a CSV file that contains the
       students and returns a list of students in the order found in the CSV file.
        1. Must throw `BadCSVException` if the file extension is not `.csv`
        2. The first line of the CSV file is always the header.
        3. The header must be the name of the properties of the class `Student` in the order as listed above.
        4. The CSV is comma separated.
        5. If any of the conditions above is not satisfied, throw a custom runtime exception `BadCSVException`
        6. The method must return a list of students in the order found in the CSV file.
    4. Create a public static method `write(path,student)` which accepts a `Student` and a `String` path and writes this
       student to the CSV record.
        1. Must throw `BadCSVException` if the file extension is not `.csv`
        2. If the CSV file path doesn't exist, it must be created along with any missing parent directories.
        3. A new CSV file must be created with the headers in the order of the properties specified above
        4. If the CSV file does exist, then the student CSV record is appended to the CSV file.
