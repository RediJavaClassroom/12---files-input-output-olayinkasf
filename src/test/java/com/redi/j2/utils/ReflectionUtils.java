package com.redi.j2.utils;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

import static java.lang.Class.forName;

public class ReflectionUtils {

    public static Class<?> getStudentClass() {
        try {
            return forName("com.redi.j2.Student");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCSVStudentClass() {
        try {
            return forName("com.redi.j2.CSVStudents");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getSortLineClass() {
        try {
            return forName("com.redi.j2.SortLine");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<? extends Exception> exception(String name) {
        try {
            return (Class<? extends Exception>)
                    forName(String.format("com.redi.j2.%sException", name));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field getFieldForClass(Class<?> aClass, String propertyName) {
        if (aClass == null || propertyName == null) {
            return null;
        }
        try {
            return aClass.getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isPrivate(Field field) {
        if (field == null) {
            return false;
        }
        return Modifier.isPrivate(field.getModifiers());
    }

    public static boolean isPublic(Method method) {
        if (method == null) {
            return false;
        }
        return Modifier.isPublic(method.getModifiers());
    }

    public static boolean hasType(Class<?> aClass, Field field) {
        if (aClass == null || field == null) {
            return false;
        }
        return field.getType().getName() == aClass.getName();
    }

    public static boolean isListOf(Class<?> aClass, Field field) {
        if (aClass == null || field == null) {
            return false;
        }
        Type type = field.getGenericType();
        if (field.getType() == List.class && type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            return pType.getActualTypeArguments()[0].getTypeName() == aClass.getTypeName();
        }
        return false;
    }

    public static boolean returnsListOf(Class<?> aClass, Method method) {
        if (aClass == null || method == null) {
            return false;
        }
        Type type = method.getGenericReturnType();
        if (method.getReturnType() == List.class && type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            return pType.getActualTypeArguments()[0].getTypeName() == aClass.getTypeName();
        }
        return false;
    }

    public static Method getMethodForClass(
            Class<?> aClass, String methodName, Class<?>... parameterTypes) {
        try {
            return aClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasReturnType(Class<?> aClass, Method method) {
        if (aClass == null || method == null) {
            return false;
        }
        return method.getReturnType() == aClass;
    }

    public static boolean hasParameters(Method method) {
        if (method == null) {
            return false;
        }
        return method.getParameterCount() > 0;
    }

    public static Constructor<?>[] getAllConstructors(Class<?> aClass) {
        if (aClass == null) {
            return new Constructor[] {};
        }
        return aClass.getDeclaredConstructors();
    }

    public static Constructor<?> getConstructor(Class<?> aClass, int which) {
        if (aClass == null) {
            return null;
        }
        return aClass.getConstructors()[which];
    }

    public static Constructor<?> getConstructor(Class<?> aClass, Class<?>... classes) {
        if (aClass == null) {
            return null;
        }
        try {
            return aClass.getConstructor(classes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Constructor<?> getDefaultConstructor(Class<?> aClass) {
        if (aClass == null) {
            return null;
        }
        try {
            return aClass.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasExactParameterList(
            Constructor<?> constructor, Class[] parameterClasses) {
        if (constructor == null || parameterClasses == null) {
            return false;
        }
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        return Arrays.equals(parameterClasses, parameterTypes);
    }


    public static <T> T getValueFromProperty(Object instance, String propertyName) {
        Field field;
        try {
            field = instance.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getValueFromGetter(Object instance, String propertyName) {
        String setter =
                "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        return invokeMethod(instance, setter);
    }

    public static <T> T invokeMethod(Object object, String name) {
        if (object == null || name == null) {
            return null;
        }
        return invokeMethod(object, name, new Class[] {}, new Object[] {});
    }

    public static <T> T invokeMethod(
            Object object, String name, Class<?>[] parameterTypes, Object... parameterValues) {
        Method method = null;
        try {
            method = object.getClass().getMethod(name, parameterTypes);
            method.setAccessible(true);
            return (T) method.invoke(object, parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object createStudent(
            UUID id,
            String name,
            Map<BigDecimal, Integer> ratings,
            SortedSet<String> tags,
            Set<String> actors) {
        try {
            return getConstructor(
                            getStudentClass(),
                            UUID.class,
                            String.class,
                            Map.class,
                            SortedSet.class,
                            Set.class)
                    .newInstance(id, name, ratings, tags, actors);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
