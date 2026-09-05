package com.ntge.ntgecore;

import com.ntge.ntgecore.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = NtgeCore.MODID, name = "NTGE Core", version = NtgeCore.VERSION)
public class NtgeCore {

    public static final String MODID = "ntgecore";
    public static final String VERSION = "0.0.2";

    @SidedProxy(
            clientSide = "com.ntge.ntgecore.proxy.ClientProxy",
            serverSide = "com.ntge.ntgecore.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.onPreInit(event);
    }
}