package com.sakuragame.eternal.justattribute.listener;

import com.sakuragame.eternal.justattribute.JustAttribute;
import com.sakuragame.eternal.justattribute.listener.smithy.*;

public class SmithyRegister extends ListenRegister {

    public SmithyRegister() {
        super(JustAttribute.getInstance());
        this.registerListener(new UIListener());
        this.registerListener(new IdentifyListener());
        this.registerListener(new SealListener());
        this.registerListener(new TransferListener());
        this.registerListener(new EnhanceListener());
        this.registerListener(new CommonListener());
    }
}
