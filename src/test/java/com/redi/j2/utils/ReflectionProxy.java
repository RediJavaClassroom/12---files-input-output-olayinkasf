package com.redi.j2.utils;

import java.lang.reflect.*;

import static java.lang.Class.forName;

public abstract class ReflectionProxy {

    private final Object target;

    public ReflectionProxy(Object... args) {
        this.target = instantiateTarget(args);
    }

    public ReflectionProxy(Object target) {
        this.target = target;
    }

    public abstract String getTargetClassName();

    public Object getTarget() {
        return target;
    }

    public boolean existsClass() {
        return getTargetClass() != null;
    }

    public boolean implementsInterface(Class<?> anInterface) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || anInterface == null) {
            return false;
        }
        return anInterface.isAssignableFrom(targetClass);
    }

    public boolean implementsInterface(String name) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        Class<?>[] interfaces = targetClass.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (anInterface.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasProperty(String name) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            targetClass.getDeclaredField(name);
            return true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isPropertyOfType(String name, Class<?> type) {
        return isPropertyOfType(name, type, null);
    }

    public boolean isPropertyOfType(String name, Class<?> type, Class<?> parameterizedType) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null || type == null) {
            return false;
        }
        try {
            Field f = targetClass.getDeclaredField(name);
            if (!f.getType().equals(type)) {
                return false;
            }
            if (parameterizedType == null) {
                return true;
            }
            if (!(f.getGenericType() instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType pType = (ParameterizedType) f.getGenericType();
            return pType.getActualTypeArguments()[0].equals(parameterizedType);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isPropertyPrivate(String name) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Field f = targetClass.getDeclaredField(name);
            return Modifier.isPrivate(f.getModifiers());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasMethod(String name, Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            targetClass.getDeclaredMethod(name, parameterTypes);
            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isMethodReturnType(
            Class<?> returnType, String name, Class<?>... parameterTypes) {
        return isMethodReturnType(returnType, null, name, parameterTypes);
    }

    public boolean isMethodReturnType(
            Class<?> returnType,
            Class<?> parameterizedType,
            String name,
            Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Method m = targetClass.getDeclaredMethod(name, parameterTypes);
            if (!m.getReturnType().equals(returnType)) {
                return false;
            }
            if (parameterizedType == null) {
                return true;
            }
            if (!(m.getGenericReturnType() instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType pType = (ParameterizedType) m.getGenericReturnType();
            return pType.getActualTypeArguments()[0].equals(parameterizedType);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isMethodPublic(String name, Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null || name == null) {
            return false;
        }
        try {
            Method m = targetClass.getDeclaredMethod(name, parameterTypes);
            return Modifier.isPublic(m.getModifiers());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasConstructor(Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return false;
        }
        try {
            Constructor c = targetClass.getDeclaredConstructor(parameterTypes);
            return c != null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isConstructorPublic(Class<?>... parameterTypes) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return false;
        }
        try {
            Constructor c = targetClass.getDeclaredConstructor(parameterTypes);
            return Modifier.isPublic(c.getModifiers());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean equals(Object obj) {
        if (target == null || !(obj instanceof ReflectionProxy)) {
            return false;
        }
        try {
            Method method = target.getClass().getMethod("equals", Object.class);
            method.setAccessible(true);
            return (boolean) method.invoke(target, ((ReflectionProxy) obj).getTarget());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int hashCode() {
        if (target == null) {
            return 0;
        }
        try {
            Method method = target.getClass().getMethod("hashCode");
            method.setAccessible(true);
            return (int) method.invoke(target);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String toString() {
        return invokeMethod("toString", new Class[] {});
    }

    protected <T> T getPropertyValue(String propertyName) {
        if (target == null || !hasProperty(propertyName)) {
            return null;
        }
        try {
            Field field = target.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected <T> T invokeMethod(
            String methodName, Class<?>[] parameterTypes, Object... parameterValues) {
        if (target == null) {
            return null;
        }
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return (T) method.invoke(target, parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Class<?> getTargetClass() {
        try {
            return forName(this.getTargetClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Constructor<?>[] getAllConstructors() {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return new Constructor[] {};
        }
        return targetClass.getConstructors();
    }

    private Object instantiateTarget(Object... args) {
        Class<?> targetClass = getTargetClass();
        if (targetClass == null) {
            return null;
        }
        Constructor[] constructors = getAllConstructors();
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == args.length) {
                try {
                    return c.newInstance(args);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    throw (RuntimeException) e.getCause();
                }
            }
        }
        return null;
    }
}
