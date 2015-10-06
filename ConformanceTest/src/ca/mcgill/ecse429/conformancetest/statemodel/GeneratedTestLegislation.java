
package ca.mcgill.ecse429.conformancetest.statemodel;

import ca.mcgill.ecse429.conformancetest.legislation.Legislation;
import org.junit.Assert;
import org.junit.Test;

public class GeneratedTestLegislation {


    @Test
    public void Test1() {
        Legislation target = new Legislation();
        Assert.assertEquals("inPreparation", target.getStateName());
        Assert.assertEquals(true, target.getIsCommonsBill());
        target.introduceInSenate();
        Assert.assertEquals("inSenate", target.getStateName());
        Assert.assertEquals(false, target.getIsCommonsBill());
        target.voteFails();
        Assert.assertEquals("inPreparation", target.getStateName());
    }

    @Test
    public void Test2() {
        Legislation target = new Legislation();
        Assert.assertEquals("inPreparation", target.getStateName());
        Assert.assertEquals(true, target.getIsCommonsBill());
        target.introduceInSenate();
        Assert.assertEquals("inSenate", target.getStateName());
        Assert.assertEquals(false, target.getIsCommonsBill());
        target.setIsCommonsBill(false);
        target.votePasses();
        Assert.assertEquals("inHouseOfCommons", target.getStateName());
    }

    @Test
    public void Test3() {
        Legislation target = new Legislation();
        Assert.assertEquals("inPreparation", target.getStateName());
        Assert.assertEquals(true, target.getIsCommonsBill());
        target.introduceInSenate();
        Assert.assertEquals("inSenate", target.getStateName());
        Assert.assertEquals(false, target.getIsCommonsBill());
        target.setIsCommonsBill(true);
        target.votePasses();
        Assert.assertEquals("finalized", target.getStateName());
    }

    @Test
    public void Test4() {
        Legislation target = new Legislation();
        Assert.assertEquals("inPreparation", target.getStateName());
        Assert.assertEquals(true, target.getIsCommonsBill());
        target.introduceInSenate();
        Assert.assertEquals("inSenate", target.getStateName());
        Assert.assertEquals(false, target.getIsCommonsBill());
        target.introduceInHouse();
        Assert.assertEquals("inHouseOfCommons", target.getStateName());
        target.voteFails();
        Assert.assertEquals("inPreparation", target.getStateName());
    }

    @Test
    public void Test5() {
        Legislation target = new Legislation();
        Assert.assertEquals("inPreparation", target.getStateName());
        Assert.assertEquals(true, target.getIsCommonsBill());
        target.introduceInSenate();
        Assert.assertEquals("inSenate", target.getStateName());
        Assert.assertEquals(false, target.getIsCommonsBill());
        target.introduceInHouse();
        Assert.assertEquals("inHouseOfCommons", target.getStateName());
        target.setIsCommonsBill(true);
        target.votePasses();
        Assert.assertEquals("inSenate", target.getStateName());
        target.voteFails();
        Assert.assertEquals("inPreparation", target.getStateName());
    }

    @Test
    public void Test6() {
        Legislation target = new Legislation();
        Assert.assertEquals("inPreparation", target.getStateName());
        Assert.assertEquals(true, target.getIsCommonsBill());
        target.introduceInSenate();
        Assert.assertEquals("inSenate", target.getStateName());
        Assert.assertEquals(false, target.getIsCommonsBill());
        target.introduceInHouse();
        Assert.assertEquals("inHouseOfCommons", target.getStateName());
        target.setIsCommonsBill(true);
        target.votePasses();
        Assert.assertEquals("inSenate", target.getStateName());
        target.setIsCommonsBill(false);
        target.votePasses();
        Assert.assertEquals("inHouseOfCommons", target.getStateName());
    }

    @Test
    public void Test7() {
        Legislation target = new Legislation();
        Assert.assertEquals("inPreparation", target.getStateName());
        Assert.assertEquals(true, target.getIsCommonsBill());
        target.introduceInSenate();
        Assert.assertEquals("inSenate", target.getStateName());
        Assert.assertEquals(false, target.getIsCommonsBill());
        target.introduceInHouse();
        Assert.assertEquals("inHouseOfCommons", target.getStateName());
        target.setIsCommonsBill(true);
        target.votePasses();
        Assert.assertEquals("inSenate", target.getStateName());
        target.setIsCommonsBill(true);
        target.votePasses();
        Assert.assertEquals("finalized", target.getStateName());
    }

    @Test
    public void Test8() {
        Legislation target = new Legislation();
        Assert.assertEquals("inPreparation", target.getStateName());
        Assert.assertEquals(true, target.getIsCommonsBill());
        target.introduceInSenate();
        Assert.assertEquals("inSenate", target.getStateName());
        Assert.assertEquals(false, target.getIsCommonsBill());
        target.introduceInHouse();
        Assert.assertEquals("inHouseOfCommons", target.getStateName());
        target.setIsCommonsBill(false);
        target.votePasses();
        Assert.assertEquals("finalized", target.getStateName());
    }

}
