package org.diablitozzz.jera.table;

import java.util.Comparator;

import org.diablitozzz.jera.data.DataSet;
import org.diablitozzz.jera.data.DataUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestTableSet {
    
    @Test
    public void testAll() {
        
        final DataSet<Object> set = DataUtils.set(null);
        //init
        Assert.assertTrue(set.isEmpty());
        Assert.assertNull(set.getFirst());
        Assert.assertNull(set.getLast());
        //add first
        set.addLastOrReplace(1);
        Assert.assertEquals(set.getFirst(), 1);
        Assert.assertEquals(set.getLast(), 1);
        Assert.assertTrue(set.contains(1));
        //add more
        set.addLastOrReplace(2);
        set.addLastOrReplace(3);
        Assert.assertEquals(set.getFirst(), 1);
        Assert.assertEquals(set.getLast(), 3);
        Assert.assertTrue(set.contains(2));
        Assert.assertTrue(set.contains(3));
        
        //remove
        set.remove(3);
        Assert.assertFalse(set.contains(3));
        Assert.assertEquals(set.getFirst(), 1);
        Assert.assertEquals(set.getLast(), 2);
        
        //replace
        set.addLastOrReplace(2);
        Assert.assertEquals(set.size(), 2);
        
        //remove first
        set.remove(1);
        Assert.assertEquals(set.getFirst(), 2);
        Assert.assertEquals(set.getLast(), 2);
        
        //clear
        set.clear();
        Assert.assertEquals(set.size(), 0);
        Assert.assertTrue(set.isEmpty());
        Assert.assertNull(set.getFirst());
        Assert.assertNull(set.getLast());
        set.remove(1);
        Assert.assertTrue(set.isEmpty());
    }
    
    @Test
    public void testOrdered() {
        final Comparator<Integer> comparator = (a, b) -> {
            return a.compareTo(b);
        };
        final DataSet<Integer> set = DataUtils.set(comparator);
        set.addLastOrReplace(3);
        set.addLastOrReplace(1);
        set.addLastOrReplace(2);
        
        Assert.assertEquals(set.getFirst(), Integer.valueOf(1));
        Assert.assertEquals(set.getLast(), Integer.valueOf(3));
    }

}
