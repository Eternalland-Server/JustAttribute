package com.sakuragame.eternal.justattribute.core.smithy.identify;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import com.sakuragame.eternal.justattribute.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sakuragame.eternal.dragoncore.util.Pair;

import java.util.*;

@Getter
@AllArgsConstructor
public class Group {

    private final Entry mini;
    private final Entry pro;
    private final Entry ultra;

    public Pair<PotencyGrade, Map<Attribute, String>> getRandom() {
        return getRandom(null);
    }

    public Pair<PotencyGrade, Map<Attribute, String>> getRandom(PotencyGrade grade) {
        Map<Attribute, String> map = new HashMap<>();

        if (grade == PotencyGrade.SSS) {
            return new Pair<>(grade, random(ultra.getContents(), 3));
        }

        if (grade == PotencyGrade.SS) {
            map.putAll(random(ultra.getContents(), 2));
            Entry entry = lottery(new ArrayList<Entry>() {{ add(mini); add(pro); }});
            map.putAll(random(entry.getContents(), 1, map.keySet()));

            return new Pair<>(grade, map);
        }

        if (grade == PotencyGrade.S) {
            map.putAll(random(ultra.getContents(), 1));
            Entry entry = lottery(new ArrayList<Entry>() {{ add(mini); add(pro); }});
            for (int i = 0; i < 2; i++) {
                map.putAll(random(entry.getContents(), 1, map.keySet()));
            }

            return new Pair<>(grade, map);
        }

        List<Entry> result = lottery(new ArrayList<Entry>() {{ add(mini); add(pro); add(ultra); }}, 3);
        PotencyGrade key = analyzeGrade(result);

        for (Entry entry : result) {
            map.putAll(random(entry.getContents(), 1, map.keySet()));
        }

        return new Pair<>(key, map);
    }

    private Map<Attribute, String> random(Map<Attribute, String> map, int count) {
        return random(map, count, null);
    }

    private Map<Attribute, String> random(Map<Attribute, String> map, int count, Set<Attribute> ignore) {
        Map<Attribute, String> result = new HashMap<>();
        List<Attribute> keys = new ArrayList<>(map.keySet());
        if (ignore != null) keys.removeAll(ignore);

        for (int i = 0; i < count && keys.size() != 0; i++) {
            int index = Utils.getRandomInt(keys.size());
            Attribute attribute = keys.remove(index);
            result.put(attribute, map.get(attribute));
        }

        return result;
    }

    private Entry lottery(List<Entry> entries) {
        return lottery(entries, 1).get(0);
    }

    private List<Entry> lottery(List<Entry> entries, int count) {
        List<Entry> result = new ArrayList<>();

        double totalWeight = 0d;
        List<Double> place = new ArrayList<>();

        for (Entry entry : entries) {
            totalWeight += entry.getWeight();
        }

        double value = 0d;
        for (Entry entry : entries) {
            value += entry.getWeight();
            place.add(value / totalWeight);
        }

        for (int i = 0; i < count; i++) {
            double random = Math.random();
            place.add(random);
            Collections.sort(place);
            int index = place.indexOf(random);

            result.add(entries.get(index));
            place.remove(index);
        }

        return result;
    }

    private PotencyGrade analyzeGrade(List<Entry> entries) {
        int pro = 0;
        int ultra = 0;

        for (Entry entry : entries) {
            if (entry.getLevel() == Level.PRO) {
                pro++;
                continue;
            }

            if (entry.getLevel() == Level.ULTRA) {
                ultra++;
            }
        }

        if (ultra == 3) return PotencyGrade.SSS;
        if (ultra == 2) return PotencyGrade.SS;
        if (ultra == 1) return PotencyGrade.S;
        if (pro >= 2) return PotencyGrade.A;
        return PotencyGrade.B;
    }
}
