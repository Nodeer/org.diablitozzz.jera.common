package org.diablitozzz.jera.table;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestTable {

    @Test
    public void testFirst() {

        final Table<MockItem> table = new Table<>();
        final TableIndex<Object, MockItem> index = table.createIndex(MockItem.ID_GETTER);

        final MockItem itemA = new MockItem("a", "A");
        final MockItem itemB = new MockItem("b", "B");
        table.addOrReplace(itemA, itemB);
        
        Assert.assertTrue(table.contains(itemA));
        Assert.assertTrue(table.contains(itemB));
        Assert.assertEquals(index.get("a").getFirst(), itemA);
        Assert.assertEquals(index.get("b").getFirst(), itemB);
    }

    @Test
    public void testMaxSize() {
        
        final Table<MockItem> table = new Table<>(3);
        final TableIndex<Object, MockItem> index = table.createIndex(MockItem.ID_GETTER);
        
        final MockItem itemA = new MockItem("a", "A");
        final MockItem itemB = new MockItem("b", "B");
        final MockItem itemC = new MockItem("c", "C");
        final MockItem itemD = new MockItem("d", "D");
        table.addOrReplace(itemA, itemB, itemC, itemD);
        
        Assert.assertFalse(table.contains(itemA));
        Assert.assertTrue(table.contains(itemB));
        Assert.assertTrue(table.contains(itemC));
        Assert.assertTrue(table.contains(itemD));
        
        Assert.assertEquals(table.getAll().get(0), itemB);
        Assert.assertEquals(table.getAll().get(1), itemC);
        Assert.assertEquals(table.getAll().get(2), itemD);

        Assert.assertEquals(index.size(), 3);
        Assert.assertEquals(index.get("b").getFirst(), itemB);
        Assert.assertEquals(index.get("c").getFirst(), itemC);
        Assert.assertEquals(index.get("d").getFirst(), itemD);
    }

}
