package learning.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties={
		"spring.datasource.url=jdbc:mysql://127.0.0.1:3306/week26_catalog_test",
		"spring.datasource.username=backend_user",
		"spring.datasource.password=${TEST_DB_PASSWORD}"
})
class Week25SpringWebApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() throws SQLException{
		try(Connection connection=dataSource.getConnection()) {
			assertEquals("week26_catalog_test",connection.getCatalog());
		}
	}

}
