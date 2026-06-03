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
        try (InputStream input = ConexaoDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalStateException("Arquivo db.properties nao encontrado no classpath.");
            }
            props.load(input);

            // Tenta obter das variáveis de ambiente primeiro (Docker), senão usa o arquivo
            String url = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : props.getProperty("db.url");
            String user = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : props.getProperty("db.user");
            String pass = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : props.getProperty("db.password");

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

