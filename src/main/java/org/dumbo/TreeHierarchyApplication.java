package org.dumbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class TreeHierarchyApplication {
    public static void main(String[] args) {
        SpringApplication.run(TreeHierarchyApplication.class, args);
    }
}
