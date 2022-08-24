package com.sakuragame.eternal.justattribute.storage;


import net.sakuragame.eternal.dragoncore.database.mysql.DatabaseTable;

public enum AccountTable {

    JUST_ATTRIBUTE_ROLE(new DatabaseTable("justattribute_role",
            new String[] {
                    "`uid` int NOT NULL PRIMARY KEY",
                    "`health` double(16,1) DEFAULT 20",
                    "`mana` double(16,1) DEFAULT 20",
                    "`combat` int DEFAULT 0"
            }));

    private final DatabaseTable table;

    AccountTable(DatabaseTable table) {
        this.table = table;
    }

    public String getTableName() {
        return table.getTableName();
    }

    public String[] getColumns() {
        return table.getTableColumns();
    }

    public DatabaseTable getTable() {
        return table;
    }

    public void createTable() {
        table.createTable();
    }
}
