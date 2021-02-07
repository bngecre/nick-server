package me.bungeecore.nickserver;

import lombok.Getter;
import me.xkuyax.utils.config.Config;
import me.xkuyax.utils.mysql.MysqlConnection;
import me.xkuyax.utils.mysql.MysqlConnectionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Getter
@SpringBootApplication
public class NickServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NickServerApplication.class, args);
    }

    @Bean(name = "config")
    Config config() {
        return new Config("settings/config.yml");
    }

    @Bean(name = "mysqlConfig")
    Config mysqlConfig() {
        return new Config("settings/mysql.yml");
    }

    @Bean
    MysqlConnection mysqlConnection(final Config mysqlConfig) {
        return MysqlConnectionUtils.hikari(mysqlConfig, "mysql");
    }
}
