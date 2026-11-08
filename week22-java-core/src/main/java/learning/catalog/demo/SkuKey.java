package learning.catalog.demo;

public class SkuKey {
    private String sku;

    public SkuKey(String sku) {
        this.sku = sku;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(!(obj instanceof SkuKey other)) return false;
        return sku.equals(other.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }

}
