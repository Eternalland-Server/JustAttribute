package com.sakuragame.eternal.justattribute.core.smithy.factory;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.core.special.PotencyGrade;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagList;
import net.sakuragame.eternal.dragoncore.util.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class IdentifyFactory {

    public final static String SCREEN_ID = "identify";

    public final static String EQUIP_SLOT = "identify_equip";
    public final static String PROP_SLOT = "identify_prop";

    private final static Map<String, List<Pair<Integer, Double>>> scope = new HashMap<String, List<Pair<Integer, Double>>>() {{
        put("v1_identify_scroll", Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.35),
                new Pair<>(2, 0.15)
        ));
        put("v2_identify_scroll", Arrays.asList(
                new Pair<>(0, 0.5),
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.16),
                new Pair<>(3, 0.04)
        ));
        put("v3_identify_scroll", Arrays.asList(
                new Pair<>(1, 0.5),
                new Pair<>(2, 0.35),
                new Pair<>(3, 0.13),
                new Pair<>(4, 0.02)));
    }};

    private static Map<Attribute, String> low;
    private static Map<Attribute, String> middle;
    private static Map<Attribute, String> height;

    private final static Random RANDOM = new Random();

    public static void init() {
        loadConfig();
    }

    public static Pair<PotencyGrade, ItemStack> machining(Player player, ItemStack item, String scrollID) {
        ItemStream itemStream = ZaphkielAPI.INSTANCE.read(item);
        ItemTag tag = itemStream.getZaphkielData();

        EquipClassify classify = EquipClassify.getClassify(tag);
        if (classify == null) return new Pair<>(PotencyGrade.NONE, item);

        PotencyGrade grade = PotencyGrade.match(getPotencyGrade(scrollID));
        int v1 = 0;
        int v2 = 0;
        int v3 = 0;
        switch (grade) {
            case SSS:
                v3 = 3;
                break;
            case SS:
                v3 = 2;
                break;
            case S:
                v3 = 1;
                break;
            case A:
                v2 = 2;
                break;
            case B:
                v1 = 2;
                break;
        }
        int v0 = 3 - v3 - v2 - v1;

        ItemTagList potency = new ItemTagList();
        if (v3 != 0) {
            random(height, v3).forEach(s -> potency.add(new ItemTagData(s)));
        }
        if (v2 != 0) {
            random(middle, v2).forEach(s -> potency.add(new ItemTagData(s)));
        }
        if (v1 != 0) {
            random(low, v1).forEach(s -> potency.add(new ItemTagData(s)));
        }
        if (v0 != 0) {
            for (int i = 0; i < v0; i++) {
                if (Math.random() > 0.5) {
                    potency.add(new ItemTagData(random(middle, 1).get(0)));
                }
                else {
                    potency.add(new ItemTagData(random(low, 1).get(0)));
                }
            }
        }

        tag.putDeep(PotencyGrade.NBT_TAG, grade.getId());
        tag.putDeep(Attribute.NBT_NODE_POTENCY, potency);

        return new Pair<>(grade, itemStream.rebuildToItemStack(player));
    }

    private static List<String> random(Map<Attribute, String> map, int count) {
        List<String> result = new ArrayList<>();

        List<Attribute> keys = new ArrayList<>(map.keySet());
        for (int i = 0; i < count; i++) {
            Attribute ident = keys.get(RANDOM.nextInt(keys.size()));
            String s = map.get(ident);
            double min = Double.parseDouble(s.split(" ", 2)[0]);
            double max = Double.parseDouble(s.split(" ", 2)[1]);
            double value = new BigDecimal(min + (max - min) * Math.random()).setScale(3, RoundingMode.HALF_UP).doubleValue();
            result.add(ident.getId() + "|" + value);
        }

        return result;
    }

    private static int getPotencyGrade(String scrollID) {
        List<Pair<Integer, Double>> weight = scope.get(scrollID);

        List<Double> place = new ArrayList<>();
        double total = 0d;
        double value = 0d;

        for (Pair<Integer, Double> pair : weight) {
            total += pair.getValue();
        }

        for (Pair<Integer, Double> pair : weight) {
            value += pair.getValue();
            place.add(value / total);
        }

        double random = Math.random();
        place.add(random);
        Collections.sort(place);
        int index = place.indexOf(random);

        return weight.get(index).getKey();
    }

    private static void loadConfig() {
        low = new HashMap<>();
        middle = new HashMap<>();
        height = new HashMap<>();

        YamlConfiguration yaml = JustAttribute.getFileManager().getSmithyConfig("identify");

        ConfigurationSection lowSection = yaml.getConfigurationSection("low");
        for (String key : lowSection.getKeys(false)) {
            Attribute ident = Attribute.match(key);
            if (ident == null) continue;
            String s = lowSection.getString(key);
            low.put(ident, s);
        }

        ConfigurationSection middleSection = yaml.getConfigurationSection("middle");
        for (String key : middleSection.getKeys(false)) {
            Attribute ident = Attribute.match(key);
            if (ident == null) continue;
            String s = middleSection.getString(key);
            middle.put(ident, s);
        }

        ConfigurationSection heightSection = yaml.getConfigurationSection("height");
        for (String key : heightSection.getKeys(false)) {
            Attribute ident = Attribute.match(key);
            if (ident == null) continue;
            String s = heightSection.getString(key);
            height.put(ident, s);
        }
    }
}
