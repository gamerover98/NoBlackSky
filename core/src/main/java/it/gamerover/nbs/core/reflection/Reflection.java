package it.gamerover.nbs.core.reflection;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class Reflection {

    private static final String DOT = ".";

    /**
     * @param packageName The package name without the latest '.'.
     * @param className The class name.
     * @return The class instance.
     * @throws ReflectionException This will be invoked when the class will not be found.
     */
    public Class<?> getClass(@NotNull String packageName,
                             @NotNull String className) throws ReflectionException {

        if (packageName.endsWith(DOT)) {
            throw new ReflectionException("Package name argument cannot be end with '.'");
        }

        if (className.contains(DOT)) {
            throw new ReflectionException("Class name cannot contains the package");
        }

        if (!packageName.isEmpty()) {
            packageName += DOT;
        }

        try {

            return Class.forName(packageName + className);

        } catch (ClassNotFoundException cnfEx) {
            throw new ReflectionException("Class not found", cnfEx);
        }

    }

    /**
     * @param clazz The class.
     * @param fieldName The field name.
     * @return The reflection field instance.
     * @throws ReflectionException This will be invoked when any field will be found.
     */
    @NotNull
    @SuppressWarnings("squid:S3011")
    public Field getField(@NotNull Class<?> clazz,
                          @NotNull String fieldName) throws ReflectionException {

        if (fieldName.isEmpty()) {
            throw new ReflectionException("The field name cannot be empty");
        }

        Field field;

        try {

            field = clazz.getDeclaredField(fieldName);

        } catch (Exception ex) {

            try {

                field = clazz.getField(fieldName);

            } catch (NoSuchFieldException nsfEx) {
                throw new ReflectionException("Cannot find any field called '"
                        + fieldName + "' in " + clazz.getName() + " class");
            }

        }

        field.setAccessible(true);
        return field;

    }

    /**
     * @param clazz The class.
     * @param methodName The method name.
     * @param methodParams The method parameter classes.
     * @return The reflection method instance.
     * @throws ReflectionException This will be invoked when any method will be found.
     */
    @NotNull
    @SuppressWarnings("squid:S3011")
    public Method getMethod(@NotNull Class<?> clazz,
                            @NotNull String methodName,
                            Class<?>... methodParams) throws ReflectionException {

        if (methodName.isEmpty()) {
            throw new ReflectionException("The method name cannot be empty");
        }

        Method method;

        try {

            method = clazz.getDeclaredMethod(methodName, methodParams);

        } catch (Exception ex) {

            try {

                method = clazz.getMethod(methodName, methodParams);

            } catch (NoSuchMethodException nsmEx) {
                throw new ReflectionException("Cannot found any method called '"
                        + methodName + "' in " + clazz.getName() + " class");
            }

        }

        method.setAccessible(true);
        return method;

    }

    /**
     * @param clazz The class.
     * @param constructorParams The constructor parameter classes.
     * @return The reflection method instance.
     */
    @SuppressWarnings("squid:S1452")
    public Constructor<?> getConstructor(@NotNull Class<?> clazz, Class<?>... constructorParams) {

        Constructor<?>[] constructors = clazz.getConstructors();

        for (Constructor<?> c : constructors) {

            if (c.getParameterCount() != constructorParams.length) {
                continue;
            }

            Class<?>[] parameterTypes = c.getParameterTypes();
            boolean found = true;

            for (int i = 0 ; i < parameterTypes.length ; i++) {

                Class<?> paramClass = parameterTypes[i];
                Class<?> argClass = constructorParams[i];

                String paramClassName = paramClass.getName();
                String argClassName = argClass.getName();

                if (!paramClassName.equals(argClassName)) {

                    found = false;
                    break;

                }

            }

            if (found) {
                return c;
            }

        }

        return null;

    }

}

