package com.sakuragame.eternal.justattribute.core.attribute.stats;

import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.special.EquipClassify;
import com.sakuragame.eternal.justattribute.core.special.SoulBound;
import com.sakuragame.eternal.justattribute.file.sub.ConfigFile;
import ink.ptms.zaphkiel.ZaphkielAPI;
import ink.ptms.zaphkiel.api.ItemStream;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTag;
import ink.ptms.zaphkiel.taboolib.module.nms.ItemTagData;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@Getter
public class AttributeData {

    private final HashMap<Attribute, Double> ordinary;
    private final HashMap<Attribute, Double> potency;

    public AttributeData() {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();
    }

    public AttributeData(ItemStack item) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();

        if (item.getType() == Material.AIR) return;
        read(ZaphkielAPI.INSTANCE.read(item));
    }

    public AttributeData(ItemStream itemStream) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();

        read(itemStream);
    }

    public AttributeData(Player player, ItemStack item, EquipClassify type) {
        this.ordinary = new HashMap<>();
        this.potency = new HashMap<>();

        if (item.getType() == Material.AIR) return;
        read(player, ZaphkielAPI.INSTANCE.read(item), type);
    }

    public AttributeData(HashMap<Attribute, Double> ordinary, HashMap<Attribute, Double> potency) {
        this.ordinary = ordinary;
        this.potency = potency;
    }

    public void initBaseAttribute() {
        for (Attribute attr : Attribute.values()) {
            ordinary.put(attr, attr.isOnlyPercent() ? 0 : attr.getBase());
            potency.put(attr, attr.isOnlyPercent() ? attr.getBase() : 0);
        }
    }

    private void read(Player player, ItemStream itemStream, EquipClassify type) {
        if (itemStream.isVanilla()) return;
        ItemTag stream = itemStream.getZaphkielData();

        int typeID = stream.getDeepOrElse(EquipClassify.NBT_NODE, new ItemTagData(-1)).asInt();
        if (typeID != type.getId()) return;

        ItemTagData bound = stream.getDeep(SoulBound.NBT_UUID_NODE);
        if (bound != null && !bound.asString().equals(player.getUniqueId().toString())) {
            if (type == EquipClassify.MainHand) {
                player.sendMessage(" &e&l[&4&l!&e&l]&c你不是这件装备的所有者");
            }
            return;
        }

        ItemTagData boundType = stream.getDeep(SoulBound.NBT_ACTION_NODE);
        if (boundType != null) return;

        read(itemStream);
    }

    private void read(ItemStream itemStream) {
        if (itemStream.isVanilla()) return;

        ItemTag itemTag = itemStream.getZaphkielData();

        for (Attribute attr : Attribute.values()) {
            ordinary.put(attr, itemTag.getDeepOrElse(attr.getOrdinaryNode(), new ItemTagData(0)).asDouble());
            potency.put(attr, itemTag.getDeepOrElse(attr.getPotencyNode(), new ItemTagData(0)).asDouble());
        }
    }
}
