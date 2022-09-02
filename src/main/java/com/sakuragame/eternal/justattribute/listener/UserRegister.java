package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.listener.user.PlayerListener;
import com.sakuragame.eternal.justattribute.listener.user.RoleListener;
import com.sakuragame.eternal.justattribute.listener.user.SlotListener;
import com.sakuragame.eternal.justattribute.listener.user.SoulBoundListener;

public class UserRegister extends ListenRegister {

    public UserRegister() {
        super(JustAttribute.getInstance());
        this.registerListener(new PlayerListener());
        this.registerListener(new RoleListener());
        this.registerListener(new SlotListener());
        this.registerListener(new SoulBoundListener());
    }
}
