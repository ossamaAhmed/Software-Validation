
package ca.mcgill.ecse429.conformancetest.statemodel;

import ca.mcgill.ecse429.conformancetest.ccoinbox.CCoinBox;
import org.junit.Assert;
import org.junit.Test;

public class TestCCoinBox {


    @Test
    public void Test1() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        target.returnQtrs();
        Assert.assertEquals("empty", target.getStateName());
    }

    @Test
    public void Test2() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        target.reset();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
    }

    @Test
    public void Test3() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        int v0 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("notAllowed", target.getStateName());
        Assert.assertEquals((v0 + 1), target.getCurQtrs());
        target.returnQtrs();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getCurQtrs());
    }

    @Test
    public void Test4() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        int v0 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("notAllowed", target.getStateName());
        Assert.assertEquals((v0 + 1), target.getCurQtrs());
        target.reset();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
    }

    @Test
    public void Test5() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        int v0 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("notAllowed", target.getStateName());
        Assert.assertEquals((v0 + 1), target.getCurQtrs());
        int v1 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals(true, target.getAllowVend());
        Assert.assertEquals((v1 + 1), target.getCurQtrs());
        target.returnQtrs();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
    }

    @Test
    public void Test6() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        int v0 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("notAllowed", target.getStateName());
        Assert.assertEquals((v0 + 1), target.getCurQtrs());
        int v1 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals(true, target.getAllowVend());
        Assert.assertEquals((v1 + 1), target.getCurQtrs());
        target.reset();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
    }

    @Test
    public void Test7() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        int v0 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("notAllowed", target.getStateName());
        Assert.assertEquals((v0 + 1), target.getCurQtrs());
        int v1 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals(true, target.getAllowVend());
        Assert.assertEquals((v1 + 1), target.getCurQtrs());
        int v2 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals((v2 + 1), target.getCurQtrs());
    }

    @Test
    public void Test8() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        int v0 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("notAllowed", target.getStateName());
        Assert.assertEquals((v0 + 1), target.getCurQtrs());
        int v1 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals(true, target.getAllowVend());
        Assert.assertEquals((v1 + 1), target.getCurQtrs());
        int v2 = target.getTotalQtrs();
        target.vend();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        Assert.assertEquals((v2 + 2), target.getTotalQtrs());
    }

    @Test
    public void Test9() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        int v0 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("notAllowed", target.getStateName());
        Assert.assertEquals((v0 + 1), target.getCurQtrs());
        int v1 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals(true, target.getAllowVend());
        Assert.assertEquals((v1 + 1), target.getCurQtrs());
        
        // (1) ADDED //
        int v3 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals(true, target.getAllowVend());
        Assert.assertEquals((v3 + 1), target.getCurQtrs());
        // END ADDED //
        
        int v2 = target.getTotalQtrs();
        target.vend();
        Assert.assertEquals("notAllowed", target.getStateName());
        Assert.assertEquals(1, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        Assert.assertEquals((v2 + 2), target.getTotalQtrs());
    }

    @Test
    public void Test10() {
        CCoinBox target = new CCoinBox();
        Assert.assertEquals("empty", target.getStateName());
        Assert.assertEquals(0, target.getTotalQtrs());
        Assert.assertEquals(0, target.getCurQtrs());
        Assert.assertEquals(false, target.getAllowVend());
        int v0 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("notAllowed", target.getStateName());
        Assert.assertEquals((v0 + 1), target.getCurQtrs());
        int v1 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals(true, target.getAllowVend());
        Assert.assertEquals((v1 + 1), target.getCurQtrs());
        
        // (2) ADDED //
        int v4 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals(true, target.getAllowVend());
        Assert.assertEquals((v4 + 1), target.getCurQtrs());
        
        int v5 = target.getCurQtrs();
        target.addQtr();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals(true, target.getAllowVend());
        Assert.assertEquals((v5 + 1), target.getCurQtrs());
        // END ADDED //
        
        int v2 = target.getTotalQtrs();
        int v3 = target.getCurQtrs();
        target.vend();
        Assert.assertEquals("allowed", target.getStateName());
        Assert.assertEquals((v2 + 2), target.getTotalQtrs());
        Assert.assertEquals((v3 - 2), target.getCurQtrs());
    }

}
