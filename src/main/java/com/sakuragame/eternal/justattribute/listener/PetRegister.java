package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.listener.pet.CommonListener;
import com.sakuragame.eternal.justattribute.listener.pet.SlotListener;

public class PetRegister extends ListenRegister {

    public PetRegister() {
        super(JustAttribute.getInstance());
        this.registerListener(new CommonListener());
        this.registerListener(new SlotListener());
    }
}
