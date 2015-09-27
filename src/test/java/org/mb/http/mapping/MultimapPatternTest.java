package org.mb.http.mapping;

import com.google.common.collect.*;
import org.junit.Assert;
import org.junit.Test;
import org.mb.http.mapping.utils.MultimapPattern;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.07.2015.
 */
public class MultimapPatternTest {

    @Test
    public void matchesMap_emptyRules_returnTrue() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        SetMultimap<String, String> rules = HashMultimap.create();

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertTrue(result);
    }

    @Test
    public void matchesMap_emptyCheckedAndNotEmptyRules_returnFalse() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertFalse(result);
    }

    @Test
    public void matchesMap_checkedIsSameAsRules_returnTrue() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "1");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertTrue(result);
    }

    @Test
    public void matchesMap_checkedContainsRules_returnTrue() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "1");
        checked.put("2", "2");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertTrue(result);
    }

    @Test
    public void matchesMap_missingKeyInChecked_returnFalse() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "1");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("2", "2");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertFalse(result);
    }

    @Test
    public void matchesMap_wrongValueInChecked_returnFalse() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "2");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertFalse(result);
    }

    @Test
    public void matchesMap_missingDuplicateValueInChecked_returnTrue() throws Exception {
        Map<String, String> checked = Maps.newHashMap();
        checked.put("1", "1");
        Multimap<String, String> rules = ArrayListMultimap.create();
        rules.put("1", "1");
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertTrue(result);
    }



    @Test
    public void matchesMultimap_emptyRules_returnTrue() throws Exception {
        SetMultimap<String, String> checked = HashMultimap.create();
        SetMultimap<String, String> rules = HashMultimap.create();

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertTrue(result);
    }

    @Test
    public void matchesMultimap_emptyCheckedAndNotEmptyRules_returnFalse() throws Exception {
        Multimap<String, String> checked = HashMultimap.create();
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertFalse(result);
    }

    @Test
    public void matchesMultimap_checkedIsSameAsRules_returnTrue() throws Exception {
        SetMultimap<String, String> checked = HashMultimap.create();
        checked.put("1", "1");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertTrue(result);
    }

    @Test
    public void matchesMultimap_checkedContainsRules_returnTrue() throws Exception {
        Multimap<String, String> checked = HashMultimap.create();
        checked.put("1", "1");
        checked.put("2", "2");
        Multimap<String, String> rules = ArrayListMultimap.create();
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertTrue(result);
    }

    @Test
    public void matchesMultimap_missingKeyInChecked_returnFalse() throws Exception {
        Multimap<String, String> checked = HashMultimap.create();
        checked.put("1", "1");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("2", "2");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertFalse(result);
    }

    @Test
    public void matchesMultimap_wrongValueInChecked_returnFalse() throws Exception {
        Multimap<String, String> checked = HashMultimap.create();
        checked.put("1", "2");
        Multimap<String, String> rules = HashMultimap.create();
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertFalse(result);
    }

    @Test
    public void matchesMultimap_missingDuplicateValueInChecked_returnFalse() throws Exception {
        Multimap<String, String> checked = ArrayListMultimap.create();
        checked.put("1", "1");
        Multimap<String, String> rules = ArrayListMultimap.create();
        rules.put("1", "1");
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertFalse(result);
    }

    @Test
    public void matchesMultimap_extraDuplicateValueInChecked_returnFalse() throws Exception {
        Multimap<String, String> checked = ArrayListMultimap.create();
        checked.put("1", "1");
        checked.put("1", "1");
        Multimap<String, String> rules = ArrayListMultimap.create();
        rules.put("1", "1");

        boolean result = MultimapPattern.from(rules).matches(checked);

        Assert.assertFalse(result);
    }
}