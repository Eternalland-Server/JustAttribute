package com.sakuragame.eternal.justattribute.storage;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import net.sakuragame.serversystems.manage.api.database.DataManager;
import net.sakuragame.serversystems.manage.api.database.DatabaseQuery;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;
import org.bukkit.entity.Player;

import java.sql.ResultSet;

public class StorageManager {

    private final JustAttribute plugin;
    private final DataManager dataManager;

    public StorageManager(JustAttribute plugin) {
        this.plugin = plugin;
        this.dataManager = ClientManagerAPI.getDataManager();
    }

    public void init() {
        AccountTable.JUST_ATTRIBUTE_ROLE.createTable();
    }

    public RoleState getPlayerDate(Player player) {
        int uid = ClientManagerAPI.getUserID(player.getUniqueId());

        try (DatabaseQuery query = dataManager.createQuery(
                AccountTable.JUST_ATTRIBUTE_ROLE.getTableName(),
                "uid",
                uid
        )) {
            ResultSet result = query.getResultSet();
            if (result.next()) {
                double health = result.getDouble("health");
                double mana = result.getDouble("mana");

                return new RoleState(player, health, mana);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        insertPlayerData(uid);

        return new RoleState(player);
    }

    private void insertPlayerData(int uid) {
        dataManager.executeInsert(
                AccountTable.JUST_ATTRIBUTE_ROLE.getTableName(),
                new String[]{"uid"},
                new Object[]{uid}
        );
    }

    public void updatePlayerData(Player player, double health, double mana) {
        int uid = ClientManagerAPI.getUserID(player.getUniqueId());

        dataManager.executeReplace(
                AccountTable.JUST_ATTRIBUTE_ROLE.getTableName(),
                new String[] {"uid", "health", "mana"},
                new Object[] {uid, health, mana}
        );
    }
}
