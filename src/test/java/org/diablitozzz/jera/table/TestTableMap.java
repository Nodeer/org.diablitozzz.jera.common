package org.diablitozzz.jera.table;

import org.diablitozzz.jera.data.DataMapImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestTableMap {

    @Test
    public void testAll() {
        
        final DataMapImpl<Object, Object> map = new DataMapImpl<>();
        //init
        Assert.assertTrue(map.isEmpty());
        Assert.assertNull(map.getFirstKey());
        Assert.assertNull(map.getLastKey());
        //add first
        map.addLastOrReplace(1, "val1");
        Assert.assertEquals(map.getFirstKey(), 1);
        Assert.assertEquals(map.getLastKey(), 1);
        Assert.assertTrue(map.containsKey(1));
        Assert.assertEquals(map.get(1), "val1");
        //add more
        map.addLastOrReplace(2, "val2");
        map.addLastOrReplace(3, "val3");
        Assert.assertEquals(map.getFirstKey(), 1);
        Assert.assertEquals(map.getLastKey(), 3);
        Assert.assertTrue(map.containsKey(2));
        Assert.assertTrue(map.containsKey(3));
        Assert.assertEquals(map.get(2), "val2");
        Assert.assertEquals(map.get(3), "val3");

        //remove
        map.remove(3);
        Assert.assertFalse(map.containsKey(3));
        Assert.assertEquals(map.getFirstKey(), 1);
        Assert.assertEquals(map.getLastKey(), 2);
        Assert.assertEquals(map.get(2), "val2");
        Assert.assertNull(map.get(3));

        //replace
        Assert.assertEquals(map.get(2), "val2");
        map.addLastOrReplace(2, "val22");
        Assert.assertEquals(map.get(2), "val22");
        Assert.assertEquals(map.size(), 2);

        //remove first
        map.remove(1);
        Assert.assertEquals(map.getFirstKey(), 2);
        Assert.assertEquals(map.getLastKey(), 2);
        
        //clear
        map.clear();
        Assert.assertEquals(map.size(), 0);
        Assert.assertTrue(map.isEmpty());
        Assert.assertNull(map.getFirstKey());
        Assert.assertNull(map.getLastKey());
        map.remove(1);
        Assert.assertTrue(map.isEmpty());
        
    }
    
}
