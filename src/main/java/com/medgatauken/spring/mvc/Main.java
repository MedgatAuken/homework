package com.medgatauken.spring.mvc;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            SimpleORM orm = new SimpleORM(connection);

            User user = new User();
            user.setName("John Doe");
            user.setEmail("john.doe@example.com");
            orm.save(user);

            User retrievedUser = orm.findById(User.class, 1L);
            System.out.println(retrievedUser.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
