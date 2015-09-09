package org.mb.binding;

import com.google.common.collect.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

/**
 * Created by Dmitriy Dzhevaga on 11.07.2015.
 */
public class CommonMarshallerTest {

    @Test
    public void checkMap_emptyRules_returnTrue() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        SetMultimap<String, String> rules = HashMultimap.create();

        boolean result = Utils.checkMap(checked, rules);

        Assert.assertTrue(result);
    }

    @Test
    public void checkMap_emptyCheckedAndNotEmptyRules_returnFalse() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = Utils.checkMap(checked, rules);

        Assert.assertFalse(result);
    }

    @Test
    public void checkMap_checkedIsSameAsRules_returnTrue() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "1");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = Utils.checkMap(checked, rules);

        Assert.assertTrue(result);
    }

    @Test
    public void checkMap_checkedContainsRules_returnTrue() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "1");
        checked.put("2", "2");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = Utils.checkMap(checked, rules);

        Assert.assertTrue(result);
    }

    @Test
    public void checkMap_missingKeyInChecked_returnFalse() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "1");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("2", "2");

        boolean result = Utils.checkMap(checked, rules);

        Assert.assertFalse(result);
    }

    @Test
    public void checkMap_wrongValueInChecked_returnFalse() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "2");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = Utils.checkMap(checked, rules);

        Assert.assertFalse(result);
    }

    @Test
    public void checkMap_missingDuplicateValueInChecked_returnTrue() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "1");
        Multimap<String, String> rules = ArrayListMultimap.create();
        rules.put("1", "1");
        rules.put("1", "1");

        boolean result = Utils.checkMap(checked, rules);

        Assert.assertTrue(result);
    }



    @Test
    public void checkMultimap_emptyRules_returnTrue() throws Exception {
        SetMultimap<String, String> checked = HashMultimap.create();
        SetMultimap<String, String> rules = HashMultimap.create();

        boolean result = Utils.checkMultimap(checked, rules);

        Assert.assertTrue(result);
    }

    @Test
    public void checkMultimap_emptyCheckedAndNotEmptyRules_returnFalse() throws Exception {
        Multimap<String, String> checked = HashMultimap.create();
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = Utils.checkMultimap(checked, rules);

        Assert.assertFalse(result);
    }

    @Test
    public void checkMultimap_checkedIsSameAsRules_returnTrue() throws Exception {
        SetMultimap<String, String> checked = HashMultimap.create();
        checked.put("1", "1");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = Utils.checkMultimap(checked, rules);

        Assert.assertTrue(result);
    }

    @Test
    public void checkMultimap_checkedContainsRules_returnTrue() throws Exception {
        Multimap<String, String> checked = HashMultimap.create();
        checked.put("1", "1");
        checked.put("2", "2");
        Multimap<String, String> rules = ArrayListMultimap.create();
        rules.put("1", "1");

        boolean result = Utils.checkMultimap(checked, rules);

        Assert.assertTrue(result);
    }

    @Test
    public void checkMultimap_missingKeyInChecked_returnFalse() throws Exception {
        Multimap<String, String> checked = HashMultimap.create();
        checked.put("1", "1");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("2", "2");

        boolean result = Utils.checkMultimap(checked, rules);

        Assert.assertFalse(result);
    }

    @Test
    public void checkMultimap_wrongValueInChecked_returnFalse() throws Exception {
        Multimap<String, String> checked = HashMultimap.create();
        checked.put("1", "2");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = Utils.checkMultimap(checked, rules);

        Assert.assertFalse(result);
    }

    @Test
    public void checkMultimap_missingDuplicateValueInChecked_returnFalse() throws Exception {
        Multimap<String, String> checked = ArrayListMultimap.create();
        checked.put("1", "1");
        Multimap<String, String> rules = ArrayListMultimap.create();
        rules.put("1", "1");
        rules.put("1", "1");

        boolean result = Utils.checkMultimap(checked, rules);

        Assert.assertFalse(result);
    }

    @Test
    public void checkMultimap_extraDuplicateValueInChecked_returnFalse() throws Exception {
        Multimap<String, String> checked = ArrayListMultimap.create();
        checked.put("1", "1");
        checked.put("1", "1");
        Multimap<String, String> rules = ArrayListMultimap.create();
        rules.put("1", "1");

        boolean result = Utils.checkMultimap(checked, rules);

        Assert.assertFalse(result);
    }
}