package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexaoDB {
    private static final HikariDataSource dataSource;

    static {
        Properties props = new Properties();
        try {
            InputStream input = ConexaoDB.class.getClassLoader().getResourceAsStream("db.properties");
            if (input != null) {
                props.load(input);
                input.close();
            }

            String url = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : props.getProperty("db.url");
            String user = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : props.getProperty("db.user");
            String pass = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : props.getProperty("db.password");

            if (url == null || user == null || pass == null) {
                throw new IllegalStateException("Configuracao de banco nao encontrada. Defina DB_URL, DB_USER e DB_PASS ou crie db.properties.");
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(pass);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            // Configurações do pool
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            config.setMaxLifetime(1800000);

            dataSource = new HikariDataSource(config);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Erro ao carregar db.properties: " + e.getMessage());
        }
    }

    private ConexaoDB() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

