package com.internet.shop.lib;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс NewInjector - альтернатива классу Injector. Обладает следующими преимуществами:
 * - объем кода сокращен почти в 2 раза;
 * - не вылетает с exception если путь к проекту содержит кириллицу или пробелы (для поиска
 *   классов используется не абсолютный путь, а относительный "target/classes" к папке где
 *   лежат скомпилированные классы;
 * - не требуется передавать никаких аргументов для создания объекта (Injector требует
 *   строку "com.internet.shop");
 * - может создавать объекты не только интерфейсов, для которых в проекте есть реализации
 *   помеченные аннотациями @Dao или @Service, но и любых классов. При этом если класс
 *   содержит поля помеченные @Inject туда будет внедрена зависимость.
 *
 */
public class NewInjector {

    private static final String PATH_TO_CLASSES = "target/classes";
    private static Map<Class<?>, Object> interfaceImplementations = new HashMap<>();

    public static Object getInstance(Class<?> clazz) {
        if (interfaceImplementations.size() == 0) {
            fillImplementationsMap();
        }
        Object instance = interfaceImplementations.get(clazz);
        if (instance == null) {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                instance = constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Can't create instance of class "
                                           + clazz.getName(), e);
            }
        }
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object objectToInject = interfaceImplementations.get(field.getType());
                try {
                    field.set(instance, objectToInject);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Can't set value to field ", e);
                }
            }
        }
        return instance;
    }

    private static void fillImplementationsMap() {
        List<Class<?>> classes = getImplementations();
        for (Class<?> clazz : classes) {
            Object instance = null;
            try {
                Constructor<?> constructor = clazz.getConstructor();
                instance = constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Can't create instance of class " + clazz.getName(), e);
            }
            for (Class<?> implInterface : clazz.getInterfaces()) {
                if (interfaceImplementations.containsKey(implInterface)) {
                    throw new RuntimeException(
                            "Two or more classes that implement interface "
                            + implInterface.getName() + " has annotation Dao or Service");
                } else {
                    interfaceImplementations.put(implInterface, instance);
                }
            }
        }
    }

    private static List<Class<?>> getImplementations() {
        try {
            return Files.walk(Path.of(PATH_TO_CLASSES))
                    .filter((path -> path.toString().endsWith(".class")))
                    .map(path -> path.toString().replaceAll("[/\\\\]", ".")
                            .substring(PATH_TO_CLASSES.length() + 1)
                            .replace(".class", ""))
                    .map(NewInjector::loadClass)
                    .filter(clazz -> clazz.isAnnotationPresent(Dao.class)
                                     || clazz.isAnnotationPresent(Service.class))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Can't load classes from path " + PATH_TO_CLASSES);
        }
    }

    private static Class<?> loadClass(String className) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't load classes from path " + PATH_TO_CLASSES, e);
        }
    }
}
