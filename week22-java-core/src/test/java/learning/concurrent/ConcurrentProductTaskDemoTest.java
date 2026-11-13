package learning.concurrent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ConcurrentProductTaskDemoTest {
    @Test
    void formatsProductAndStockStatus() throws InterruptedException {
        var p1 = new ConcurrentProductTaskDemo.ProductSnapshot("A001", "键盘", 10);

        var p2 = new ConcurrentProductTaskDemo.ProductSnapshot("A003", "显示器", 0);

        assertEquals("A001 | 键盘 | 库存：10 | 有货", ConcurrentProductTaskDemo.process(p1));

        assertEquals("A003 | 显示器 | 库存：0 | 缺货", ConcurrentProductTaskDemo.process(p2));
    }
}
