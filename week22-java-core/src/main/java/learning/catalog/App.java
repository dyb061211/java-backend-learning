package learning.catalog;

public class App {
    public static void main(String[] args) {

        ProductCatalog catalog = new ProductCatalog();

        // 新增商品
        catalog.addProduct(new Product("A001", "键盘", 10));
        catalog.addProduct(new Product("A002", "鼠标", 20));

        // 查询商品
        Product found = catalog.findBySku("A001");

        if (found != null) {
            System.out.println("查询成功：" + found.getName());
        }

        // 修改商品
        catalog.updateProductName("A001", "机械键盘");
        catalog.updateProductStock("A001", 30);

        // 列出商品
        System.out.println("商品目录：");

        for (Product product : catalog.listProducts()) {
            System.out.println(
                    product.getSku() + " | "
                            + product.getName() + " | "
                            + product.getStock()
            );
        }

        // 验证重复SKU
        try {
            catalog.addProduct(new Product("A001", "重复商品", 5));
        } catch (IllegalArgumentException e) {
            System.out.println("新增失败：" + e.getMessage());
        }

        // 验证不存在的商品
        try {
            catalog.updateProductStock("A999", 10);
        } catch (IllegalArgumentException e) {
            System.out.println("更新失败：" + e.getMessage());
        }
        System.out.println("商品数量：" + catalog.count());

        System.out.println("开始异常实验");
        try{
            catalog.updateProductStock("A001", -5);
            System.out.print("修改成功");
        }
        catch(IllegalArgumentException e) {
            System.out.println("捕获异常"+e.getMessage());
            e.printStackTrace(System.out);
        }
        System.out.println(
                "最终库存：" + catalog.findBySku("A001").getStock()
        );

        System.out.println("程序继续执行");

        System.out.println("=== Debug实验 ===");

        Product debugResult = catalog.findBySku("A020");

        if (debugResult == null) {
            System.out.println("查询失败");
        } else {
            System.out.println("查询成功：" + debugResult.getName());
        }

    }
}