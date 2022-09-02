package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.listener.hook.InfoListener;
import com.sakuragame.eternal.justattribute.listener.hook.LevelListener;
import com.sakuragame.eternal.justattribute.listener.hook.MobListener;
import com.sakuragame.eternal.justattribute.listener.hook.StorageListener;

public class HookRegister extends ListenRegister {

    public HookRegister() {
        super(JustAttribute.getInstance());
        this.registerListener(new InfoListener());
        this.registerListener(new LevelListener());
        this.registerListener(new MobListener());
        this.registerListener(new StorageListener());
    }
}
