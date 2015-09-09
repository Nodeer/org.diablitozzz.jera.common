package org.diablitozzz.jera.table;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestTableIndexBasic {

    @Test
    public void testAddGetUpdate() {
        
        final TableIndexBasic<Object, MockItem> index = new TableIndexBasic<>(MockItem.ID_GETTER);
        
        final MockItem itemA = new MockItem("a", "A");
        final MockItem itemB = new MockItem("b", "B");
        
        index.addOrReplace(itemA, itemB);
        Assert.assertEquals(index.get("a").get(0).getValue(), "A");
        Assert.assertEquals(index.get("b").get(0).getValue(), "B");

        itemA.setValue("A2");
        index.addOrReplace(itemA);
        Assert.assertEquals(index.get("a").get(0).getValue(), "A2");
        
        //clear
        index.clear();
        Assert.assertTrue(index.isEmpty());
    }

    @Test
    public void testRemove() {
        final TableIndexBasic<Object, MockItem> index = new TableIndexBasic<>(MockItem.ID_GETTER);
        final MockItem itemA = new MockItem("a", "A");
        Assert.assertFalse(index.contains(itemA));
        index.addOrReplace(itemA);
        Assert.assertTrue(index.contains(itemA));
    }
    
    @Test
    public void testSupportNullKeys() {
        final TableIndexBasic<Object, MockItem> index = new TableIndexBasic<>(MockItem.VALUE_GETTER);
        final MockItem itemA = new MockItem("a", null);
        final MockItem itemB = new MockItem("b", null);
        index.addOrReplace(itemA, itemB);
        Assert.assertEquals(index.get(null).size(), 2);
        Assert.assertEquals(index.get(null).get(0), itemA);
        Assert.assertEquals(index.get(null).get(1), itemB);
    }
}
