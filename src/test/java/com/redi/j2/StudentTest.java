package com.redi.j2;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.redi.j2.utils.ReflectionUtils;
import com.redi.j2.utils.StudentProxy;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static com.redi.j2.utils.ReflectionUtils.*;
import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class StudentTest {
    Random random = new Random();

    @Test
    void _1_student_class_should_exist() {
        assertNotNull(getStudentClass(), "Please create the student class in the right package");
    }

    @Test
    void _2_student_class_should_have_the_right_properties() {
        Field id = ReflectionUtils.getFieldForClass(getStudentClass(), "id");
        Field firstName = ReflectionUtils.getFieldForClass(getStudentClass(), "firstName");
        Field lastName = ReflectionUtils.getFieldForClass(getStudentClass(), "lastName");
        Field height = ReflectionUtils.getFieldForClass(getStudentClass(), "height");
        Field weight = ReflectionUtils.getFieldForClass(getStudentClass(), "weight");
        Field dateOfBirth = ReflectionUtils.getFieldForClass(getStudentClass(), "dateOfBirth");

        assertNotNull(id, "Please add the property 'id' with the right type");
        assertNotNull(firstName, "Please add the property 'firstName' with the right type");
        assertNotNull(lastName, "Please add the property 'lastName' with the right type");
        assertNotNull(height, "Please add the property 'height' with the right type");
        assertNotNull(dateOfBirth, "Please add the property 'dateOfBirth' with the right type");

        assertEquals(id.getType(), UUID.class, "Please add the property 'id' with the right type");
        assertEquals(
                firstName.getType(),
                String.class,
                "Please add the property 'firstName' with the right type");
        assertEquals(
                lastName.getType(),
                String.class,
                "Please add the property 'lastName' with the right type");
        assertEquals(
                height.getType(),
                int.class,
                "Please add the property 'height' with the right type");
        assertEquals(
                weight.getType(),
                int.class,
                "Please add the property 'height' with the right type");
        assertEquals(
                dateOfBirth.getType(),
                LocalDate.class,
                "Please add the property 'dateOfBirth' with the right type");
    }

    @Test
    void _4_student_class_should_have_the_right_all_args_constructor() {
        Constructor<?> constructor =
                getConstructor(
                        getStudentClass(),
                        UUID.class,
                        String.class,
                        String.class,
                        int.class,
                        int.class,
                        LocalDate.class);

        assertNotNull(
                constructor,
                "Please add the constructor with all properties in the specified order and with the right type");

        String firstName = UUID.randomUUID().toString();
        UUID id = UUID.randomUUID();
        String lastName = UUID.randomUUID().toString();
        int height = random.nextInt(Integer.MAX_VALUE);
        int weight = random.nextInt(Integer.MAX_VALUE);
        LocalDate dateOfBirth = LocalDate.now();

        StudentProxy studentProxy =
                new StudentProxy(id, firstName, lastName, height, weight, dateOfBirth);

        assertNotNull(
                studentProxy.id(),
                "Please set the property 'id' in the constructor from the parameter");
        assertNotNull(
                studentProxy.firstName(),
                "Please set the property 'firstName' in the constructor from the parameter");
        assertNotNull(
                studentProxy.lastName(),
                "Please initialize the property 'lastName' as an empty map");
        assertNotNull(
                studentProxy.dateOfBirth(),
                "Please initialize the property 'dateOfBirth' as an empty set");

        assertEquals(
                studentProxy.id(),
                id,
                "Please set the property 'id' in the constructor from the parameter");
        assertEquals(
                studentProxy.firstName(),
                firstName,
                "Please set the property 'firstName' in the constructor from the parameter");
        assertEquals(
                studentProxy.lastName(),
                lastName,
                "Please set the property 'lastName' in the constructor from the parameter");
        assertEquals(
                studentProxy.height(),
                height,
                "Please set the property 'height' in the constructor from the parameter");
        assertEquals(
                studentProxy.dateOfBirth(),
                dateOfBirth,
                "Please set the property 'dateOfBirth' in the constructor from the parameter");
    }

    @Test
    void _5_student_class_should_not_have_any_other_constructors() {
        assertEquals(getAllConstructors(getStudentClass()).length, 1);
    }

    @Test
    void _6_all_properties_of_student_class_must_be_private_and_final() {
        Stream.of(
                        ReflectionUtils.getFieldForClass(getStudentClass(), "id"),
                        ReflectionUtils.getFieldForClass(getStudentClass(), "firstName"),
                        ReflectionUtils.getFieldForClass(getStudentClass(), "lastName"),
                        ReflectionUtils.getFieldForClass(getStudentClass(), "height"),
                        ReflectionUtils.getFieldForClass(getStudentClass(), "weight"),
                        ReflectionUtils.getFieldForClass(getStudentClass(), "dateOfBirth"))
                .forEach(
                        field -> {
                            assertTrue(
                                    Modifier.isPrivate(field.getModifiers()),
                                    String.format(
                                            "Please ensure that the property '%s' is private.",
                                            field.getName()));
                            assertTrue(
                                    Modifier.isFinal(field.getModifiers()),
                                    String.format(
                                            "Please ensure that the property '%s' is final.",
                                            field.getName()));
                        });
    }

    @Test
    void _7_1_student_class_should_generate_getters() {
        String firstName = UUID.randomUUID().toString();
        UUID id = UUID.randomUUID();
        String lastName = UUID.randomUUID().toString();
        int height = random.nextInt(Integer.MAX_VALUE);
        int weight = random.nextInt(Integer.MAX_VALUE);
        LocalDate dateOfBirth = LocalDate.now();

        StudentProxy studentProxy =
                new StudentProxy(id, firstName, lastName, height, weight, dateOfBirth);

        assertEquals(studentProxy.getId(), id, "Please generate the getter for property 'id'");
        assertEquals(
                studentProxy.getFirstName(),
                firstName,
                "Please generate the getter for property 'firstName'");
        assertEquals(
                studentProxy.getLastName(),
                lastName,
                "Please generate the getter for property 'lastName'");
        assertEquals(
                studentProxy.getHeight(),
                height,
                "Please generate the getter for property 'height'");
        assertEquals(
                studentProxy.getDateOfBirth(),
                dateOfBirth,
                "Please generate the getter for property 'dateOfBirth'");
    }

    @Test
    void _7_2_student_class_should_generate_toString() {
        try {
            ToStringVerifier.forClass(getStudentClass()).verify();
        } finally {
            System.err.println("Please generate the 'toString' method");
        }
    }

    @Test
    void _7_3_student_class_should_generate_equals_and_hashCode() {
        try {
            EqualsVerifier.forClass(getStudentClass()).suppress(STRICT_INHERITANCE).verify();
        } finally {
            System.out.println("Please generate the 'equals' and 'hashCode' methods");
        }
    }

    @Test
    void _8_validations_student_should_validate_arguments() {
        String firstName = UUID.randomUUID().toString();
        UUID id = UUID.randomUUID();
        String lastName = UUID.randomUUID().toString();
        int height = random.nextInt(Integer.MAX_VALUE);
        int weight = random.nextInt(Integer.MAX_VALUE);
        LocalDate dateOfBirth = LocalDate.now();

        assertThrows(
                NullPointerException.class,
                () -> new StudentProxy(null, firstName, lastName, height, weight, dateOfBirth),
                "null 'id' should throw NullPointerException");
        assertThrows(
                NullPointerException.class,
                () -> new StudentProxy(id, null, lastName, height, weight, dateOfBirth),
                "null 'firstName' should throw NullPointerException");
        assertThrows(
                NullPointerException.class,
                () -> new StudentProxy(id, firstName, null, height, weight, dateOfBirth),
                "null 'lastName' should throw NullPointerException");
        assertThrows(
                NullPointerException.class,
                () -> new StudentProxy(id, firstName, lastName, height, weight, null),
                "null 'dateOfBirth' should throw NullPointerException");
        assertNotNull(
                exception("BadName"),
                "Please create a runtime exception BadNameException");
        assertThrows(
                exception("BadName"),
                () -> new StudentProxy(id, "", lastName, height, weight, dateOfBirth),
                "empty 'firstName' should throw BadNameException");
        assertThrows(
                exception("BadName"),
                () -> new StudentProxy(id, "   \t\t\n\r\n", lastName, height, weight, dateOfBirth),
                "blank 'firstName' should throw BadNameException");
        assertThrows(
                exception("BadName"),
                () -> new StudentProxy(id, firstName, "", height, weight, dateOfBirth),
                "empty 'lastName' should throw BadNameException");
        assertThrows(
                exception("BadName"),
                () -> new StudentProxy(id, firstName, "   \t\t\n\r\n", height, weight, dateOfBirth),
                "empty 'lastName' should throw BadNameException");
        assertNotNull(
                exception("BadHeight"),
                "Please create a runtime exception BadHeightException");
        assertThrows(
                exception("BadHeight"),
                () -> new StudentProxy(id, firstName, lastName, 0, weight, dateOfBirth),
                "0 'height' should throw BadHeightException");
        assertThrows(
                exception("BadHeight"),
                () -> new StudentProxy(id, firstName, lastName, -height, weight, dateOfBirth),
                "negative 'height' should throw BadHeightException");
        assertNotNull(
                exception("BadWeight"),
                "Please create a runtime exception BadWeightException");
        assertThrows(
                exception("BadWeight"),
                () -> new StudentProxy(id, firstName, lastName, height, 0, dateOfBirth),
                "0 'weight' should throw BadWeightException");
        assertThrows(
                exception("BadWeight"),
                () -> new StudentProxy(id, firstName, lastName, height, -weight, dateOfBirth),
                "negative 'weight' should throw BadWeightException");
        assertNotNull(
                exception("BadDate"),
                "Please create a runtime exception BadDateException");
        assertThrows(
                exception("BadDate"),
                () ->
                        new StudentProxy(
                                id,
                                firstName,
                                lastName,
                                height,
                                weight,
                                LocalDate.now().plusDays(1)),
                "future 'dateOfBirth' should throw BadDateException");
    }
}
