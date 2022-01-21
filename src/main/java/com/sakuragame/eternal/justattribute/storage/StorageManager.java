package com.sakuragame.eternal.justattribute.storage;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.core.attribute.stats.RoleState;
import net.sakuragame.serversystems.manage.api.database.DataManager;
import net.sakuragame.serversystems.manage.api.database.DatabaseQuery;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;

import java.sql.ResultSet;
import java.util.UUID;

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

    public RoleState loadData(UUID uuid) {
        int uid = ClientManagerAPI.getUserID(uuid);
        if (uid == -1) return null;

        try (DatabaseQuery query = dataManager.createQuery(
                AccountTable.JUST_ATTRIBUTE_ROLE.getTableName(),
                "uid",
                uid
        )) {
            ResultSet result = query.getResultSet();
            if (result.next()) {
                double health = result.getDouble("health");
                double mana = result.getDouble("mana");

                return new RoleState(uuid, health, mana);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new RoleState(uuid);
    }

    public void saveData(UUID uuid, double health, double mana) {
        int uid = ClientManagerAPI.getUserID(uuid);
        if (uid == -1) return;

        dataManager.executeReplace(
                AccountTable.JUST_ATTRIBUTE_ROLE.getTableName(),
                new String[] {"uid", "health", "mana"},
                new Object[] {uid, health, mana}
        );
    }
}
