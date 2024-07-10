package com.medgatauken.spring.mvc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleORM {
    private final Connection connection;

    public SimpleORM(Connection connection) {
        this.connection = connection;
    }

    public <T> void save(T entity) throws SQLException, IllegalAccessException {
        Class<?> clazz = entity.getClass();
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class must be annotated with @Table");
        }

        Table table = clazz.getAnnotation(Table.class);
        StringBuilder query = new StringBuilder("INSERT INTO " + table.name() + " (");

        Field[] fields = clazz.getDeclaredFields();
        List<Object> values = new ArrayList<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                query.append(column.name()).append(", ");
                values.add(field.get(entity));
            }
        }

        query.setLength(query.length() - 2);
        query.append(") VALUES (");
        for (int i = 0; i < values.size(); i++) {
            query.append("?, ");
        }
        query.setLength(query.length() - 2);
        query.append(")");

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            statement.executeUpdate();
        }
    }

    public <T> T findById(Class<T> clazz, Object id) throws SQLException, ReflectiveOperationException {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class must be annotated with @Table");
        }

        Table table = clazz.getAnnotation(Table.class);
        String idColumnName = null;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    idColumnName = column.name();
                }
            }
        }

        if (idColumnName == null) {
            throw new IllegalArgumentException("No field annotated with @Id and @Column found");
        }

        String query = "SELECT * FROM " + table.name() + " WHERE " + idColumnName + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    T entity = clazz.getDeclaredConstructor().newInstance();
                    for (Field field : clazz.getDeclaredFields()) {
                        if (field.isAnnotationPresent(Column.class)) {
                            field.setAccessible(true);
                            Column column = field.getAnnotation(Column.class);
                            field.set(entity, resultSet.getObject(column.name()));
                        }
                    }
                    return entity;
                } else {
                    return null;
                }
            }
        }
    }
}
