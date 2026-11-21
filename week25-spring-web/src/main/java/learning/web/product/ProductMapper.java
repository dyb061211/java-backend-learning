package learning.web.product;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import java.util.List;

@Mapper
public interface ProductMapper {

    Integer findStockBySku(@Param("sku") String sku);

    Product findProductBySku(@Param("sku") String sku);

    int insertProduct(
            @Param("sku") String sku,
            @Param("name") String name,
            @Param("stock") int stock
    );

    int updateProduct(
            @Param("sku") String sku,
            @Param("name") String name,
            @Param("stock") int stock
    );

    int deleteProduct(@Param("sku") String sku);

    List<Product> listProductById(@Param("limit") int limit);

    List<Product> listProductBySku(@Param("limit") int limit);

    int insertChangeLog(
            @Param("sku") String sku,
            @Param("oldName") String oldName,
            @Param("newName") String newName,
            @Param("oldStock") int oldStock,
            @Param("newStock") int newStock
    );

    Product findProductForUpdate(@Param("sku") String sku);
}
