package com.redi.j2.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.redi.j2.utils.ReflectionUtils.getSortLineClass;

public class SortLineProxy {

    public static void sortLine(String path) throws InvocationTargetException {
        try {
            Method method = getSortLineClass().getMethod("sortLine", String.class);
            method.invoke(null, path);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
