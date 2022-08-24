package com.sakuragame.eternal.justattribute.storage;

import com.sakuragame.eternal.justattribute.core.attribute.character.PlayerCharacter;
import net.sakuragame.serversystems.manage.api.database.DataManager;
import net.sakuragame.serversystems.manage.api.database.DatabaseQuery;
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StorageManager {

    private final DataManager dataManager;

    public StorageManager() {
        this.dataManager = ClientManagerAPI.getDataManager();
    }

    public void init() {
        AccountTable.JUST_ATTRIBUTE_ROLE.createTable();
    }

    public PlayerCharacter loadAccount(UUID uuid) {
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

                return new PlayerCharacter(uuid, health, mana);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PlayerCharacter(uuid);
    }

    public void saveAccount(UUID uuid, double health, double mana, int combatPower) {
        int uid = ClientManagerAPI.getUserID(uuid);
        if (uid == -1) return;

        dataManager.executeReplace(
                AccountTable.JUST_ATTRIBUTE_ROLE.getTableName(),
                new String[] {"uid", "health", "mana", "combat"},
                new Object[] {uid, health, mana, combatPower}
        );
    }

    public Map<Integer, Integer> getAllPlayerCombatPower() {
        Map<Integer, Integer> map = new HashMap<>();

        try (DatabaseQuery query = this.dataManager.createQuery("SELECT * FROM " + AccountTable.JUST_ATTRIBUTE_ROLE.getTableName())) {
            ResultSet result = query.getResultSet();
            while (result.next()) {
                int uid = result.getInt("uid");
                int combat = result.getInt("combat");
                map.put(uid, combat);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
