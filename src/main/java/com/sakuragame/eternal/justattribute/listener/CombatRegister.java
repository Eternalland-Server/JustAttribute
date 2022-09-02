package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.listener.combat.CombatListener;
import com.sakuragame.eternal.justattribute.listener.combat.VampireListener;

public class CombatRegister extends ListenRegister {

    public CombatRegister() {
        super(JustAttribute.getInstance());
        this.registerListener(new CombatListener());
        this.registerListener(new VampireListener());
    }
}
