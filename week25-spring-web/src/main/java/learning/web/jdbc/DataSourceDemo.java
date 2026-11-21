package learning.web.jdbc;

import learning.web.Week25SpringWebApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import javax.sql.DataSource;
import java.sql.Connection;

public class DataSourceDemo {
    public static void main(String[] args) throws Exception {
        try(ConfigurableApplicationContext context=new SpringApplicationBuilder(Week25SpringWebApplication.class)
                .web(WebApplicationType.NONE)
                .run(args)) {
            DataSource dataSource=context.getBean(DataSource.class);
            System.out.println("DataSource:"+dataSource.getClass().getName());
            try(Connection connection=dataSource.getConnection()) {
                System.out.println("Connected:"+connection.isValid(2));
                System.out.println("DataBase:"+connection.getCatalog());
            }
            System.out.println("Connection returned");
        }
    }
}
