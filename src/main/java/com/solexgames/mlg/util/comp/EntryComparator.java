package com.solexgames.mlg.util.comp;

import java.util.Comparator;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 5/31/2021
 */

public class EntryComparator implements Comparator<Map.Entry<String, Integer>> {

    @Override
    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        return o2.getValue() - o1.getValue();
    }
}
