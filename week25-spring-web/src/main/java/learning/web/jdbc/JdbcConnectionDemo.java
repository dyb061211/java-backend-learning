package learning.web.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import learning.web.product.Product;

public class JdbcConnectionDemo {
    public static void main(String[] args) throws SQLException {
        String url=System.getenv("DB_URL");
        String user=System.getenv("DB_USER");
        String password=System.getenv("DB_PASSWORD");
        if(url==null||user==null||password==null) {
            throw new SQLException("Missing database environment variables");
        }
        try(Connection connection=DriverManager.getConnection(url,user,password)) {
            System.out.println("Connected:"+connection.isValid(2));
            System.out.println("Database:"+connection.getCatalog());
            String sql="SELECT sku,name,stock FROM products WHERE sku=?";
            try(PreparedStatement statement=connection.prepareStatement(sql)) {
                statement.setString(1,"A001");
                try(ResultSet rs=statement.executeQuery()) {
                    if(rs.next()) {
                        String sku=rs.getString("sku");
                        String name=rs.getString("name");
                        int stock=rs.getInt("stock");
                        Product product=new Product(sku,name,stock);
                        System.out.println("sku: " + product.getSku());
                        System.out.println("name: " + product.getName());
                        System.out.println("stock: " + product.getStock());
                    }
                    else {
                        System.out.println("Product not found");
                    }
                }
            }

//            String insertSql="INSERT INTO products(sku,name,stock) VALUES(?,?,?)";
//            try(PreparedStatement insertStmt=connection.prepareStatement(insertSql)) {
//                insertStmt.setString(1,"D2002");
//                insertStmt.setString(2,"测试鼠标垫");
//                insertStmt.setInt(3,12);
//                int affectedRows=insertStmt.executeUpdate();
//                System.out.println("Inserted Rows:"+affectedRows);
//            }

//            String updateSql="UPDATE products SET stock=? WHERE sku=?";
//            try(PreparedStatement updateStmt=connection.prepareStatement(updateSql)) {
//                updateStmt.setInt(1,7);
//                updateStmt.setString(2,"D2002");
//                int affectedRows=updateStmt.executeUpdate();
//                System.out.println("Updated Rows:"+affectedRows);
//            }

//            String deleteSql="DELETE FROM products WHERE sku=?";
//            try(PreparedStatement deleteStmt=connection.prepareStatement(deleteSql)) {
//                deleteStmt.setString(1,"D2002");
//                int affectedRows=deleteStmt.executeUpdate();
//                System.out.println("Deleted Rows:"+affectedRows);
//            }

            System.out.println("AutoCommit:"+connection.getAutoCommit());
            connection.setAutoCommit(false);
            try{
                String updateSql="UPDATE products SET stock=? WHERE sku=?";
                try(PreparedStatement ps=connection.prepareStatement(updateSql)) {
                    ps.setInt(1,7);
                    ps.setString(2,"A001");
                    int rows=ps.executeUpdate();
                    System.out.println("Updated Rows:"+rows);
                    if(rows!=1) {
                        throw new SQLException("A001 update did not match one row");
                    }
                }
                connection.commit();
                System.out.println("Commit executed");
            }catch(SQLException|RuntimeException e) {
                connection.rollback();
                System.out.println("Rollback executed");
                throw e;
            }
        }
    }
}
