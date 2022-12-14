package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.listener.build.*;

public class BuildRegister extends ListenRegister {

    public BuildRegister() {
        super(JustAttribute.getInstance());
        this.registerListener(new EquipListener());
        this.registerListener(new ExpireListener());
        this.registerListener(new PetListener());
        this.registerListener(new SkinListener());
        this.registerListener(new CommonListener());
        this.registerListener(new FishingListener());
    }
}
