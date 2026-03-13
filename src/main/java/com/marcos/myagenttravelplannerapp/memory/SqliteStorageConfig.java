package com.marcos.myagenttravelplannerapp.memory;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
class SqliteStorageConfig {

    @Bean
    DataSource dataSource(TravelerMemoryProperties props) throws IOException {
        Path dir = resolvePath(props.storagePath());
        Files.createDirectories(dir);
        return DataSourceBuilder.create()
                .url("jdbc:sqlite:" + dir.resolve("memory.db").toAbsolutePath())
                .driverClassName("org.sqlite.JDBC")
                .build();
    }

    private static Path resolvePath(String storagePath) {
        if (storagePath.startsWith("~")) {
            return Path.of(System.getProperty("user.home") + storagePath.substring(1));
        }
        return Path.of(storagePath);
    }
}