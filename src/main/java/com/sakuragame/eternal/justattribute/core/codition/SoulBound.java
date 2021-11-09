package com.sakuragame.eternal.justattribute.core.codition;

import com.sakuragame.eternal.justattribute.JustAttribute;
import lombok.Getter;

public class SoulBound {

    public final static String ACTION_NODE = "justattribute.soulbound.action";
    public final static String UUID_NODE = "justattribute.soulbound.uuid";
    public final static String NAME_NODE = "justattribute.soulbound.name";
    public final static String DISPLAY_NODE = "soulbound.display";

    @Getter
    public enum Action {

        AUTO(0, "自动绑定"),
        USE(1, "使用后绑定");

        private final int id;
        private final String desc;

        Action(int id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public static Action getAction(int id) {
            for (Action action : values()) {
                if (action.getId() == id) {
                    return action;
                }
            }

            return null;
        }
    }

    private final JustAttribute plugin;

    public SoulBound() {
        this.plugin = JustAttribute.getInstance();
    }
}
