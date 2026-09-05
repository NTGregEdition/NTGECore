package com.ntge.ntgecore.proxy;

import com.ntge.ntgecore.window.*;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);

        WindowIconLoader.apply();
        WindowTitleLoader.apply();
    }
}
