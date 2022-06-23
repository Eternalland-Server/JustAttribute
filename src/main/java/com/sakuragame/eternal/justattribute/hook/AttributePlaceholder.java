package com.sakuragame.eternal.justattribute.hook;

import com.sakuragame.eternal.justattribute.api.JustAttributeAPI;
import com.sakuragame.eternal.justattribute.core.attribute.Attribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleAttribute;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AttributePlaceholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "attribute";
    }

    @Override
    public @NotNull String getAuthor() {
        return "justwei";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
        if (player == null) return "0";

        RoleAttribute role = JustAttributeAPI.getRoleAttribute(player.getUniqueId());
        if (role == null) return "0";

        Attribute ident = Attribute.match(params);
        if (ident == null) return "0";

        if (ident == Attribute.Damage) {
            return String.valueOf(role.getValue(Attribute.Damage));
        }

        if (ident == Attribute.Defence) {
            return String.valueOf(role.getValue(Attribute.Defence));
        }

        if (ident == Attribute.Health) {
            return String.valueOf(role.getValue(Attribute.Health));
        }

        if (ident == Attribute.Mana) {
            return String.valueOf(role.getValue(Attribute.Mana));
        }

        return String.valueOf(role.getValue(ident));
    }
}
