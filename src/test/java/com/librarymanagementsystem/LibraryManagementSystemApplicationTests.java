package com.librarymanagementsystem;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

class LibraryManagementSystemApplicationTests {

    @Test
    @Disabled("Disabling context test to avoid connecting to real DB during unit tests")
    void contextLoads() {
    }

}
