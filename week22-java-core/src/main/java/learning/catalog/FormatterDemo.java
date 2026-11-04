package learning.catalog;

interface ProductFormatter {
    String format(Product product);
}

class NameFormatter implements ProductFormatter {
    @Override
    public String format(Product product) {
        return product.getName();
    }
}

class DetailFormatter implements ProductFormatter {
    @Override
    public String format(Product product) {
        return product.getSku()+" | "+product.getName()+" | "+product.getStock();
    }
}

class BaseLabel{
    public String label(Product product) {
        return product.getName();
    }
    public String label(Product product, boolean includeSku) {
        if (includeSku) {
            return product.getSku() + " | " + product.getName();
        }

        return product.getName();
    }
}

class StockLabel extends BaseLabel{
    @Override
    public String label(Product product) {
        return super.label(product) + "| 库存 "+product.getStock();
    }
}

abstract class AbstractLabel{
    public abstract String label(Product product);
    public String addPrefix(String content) {
        return "商品："+content;
    }
}

class SimpleLabel extends AbstractLabel{
    @Override
    public String label(Product product) {
        return addPrefix(product.getName());
    }
}

public class FormatterDemo {
    public static void main(String[] args) {
        Product product=new Product("A001", "机械键盘", 30);
        ProductFormatter formatter=new NameFormatter();
        System.out.println(formatter.format(product));
        formatter=new DetailFormatter();
        System.out.println(formatter.format(product));

        BaseLabel label = new StockLabel();
        System.out.println(label.label(product));

        BaseLabel label2 = new StockLabel();

        System.out.println(label2.label(product));

        System.out.println(label2.label(product, true));

        System.out.println(label2.label(product, false));

        AbstractLabel label3 = new SimpleLabel();

        System.out.println(label3.label(product));
    }
}
