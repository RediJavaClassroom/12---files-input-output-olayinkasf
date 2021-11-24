package com.redi.j2.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.redi.j2.utils.ReflectionUtils.getCSVStudentClass;
import static com.redi.j2.utils.ReflectionUtils.getStudentClass;

public class CSVStudentsProxy {

    public static List<?> read(String path) throws InvocationTargetException {
        try {
            Method method = getCSVStudentClass().getMethod("read", String.class);
            return (List<?>) method.invoke(null, path);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void write(String path, StudentProxy proxy) throws InvocationTargetException {
        try {
            Method method =
                    getCSVStudentClass().getMethod("write", String.class, getStudentClass());
            method.invoke(null, path, proxy.getTarget());
        } catch (NoSuchMethodException | IllegalAccessException  e) {
            e.printStackTrace();
        }
    }
}
