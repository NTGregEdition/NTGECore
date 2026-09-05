package com.ntge.ntgecore.window;

import org.lwjgl.opengl.Display;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class WindowTitleLoader {

    private static final Logger LOG = Logger.getLogger("NtgeCore");
    private static final String TITLE = "Nuclear Tech: Greg Edition";

    private WindowTitleLoader() {
    }

    public static void apply() {
        try {
            Display.setTitle(TITLE);
        } catch (Exception e) {
            // not worth crashing the game over a title bar
            LOG.log(Level.WARNING, "Failed to set the window title", e);
        }
    }
}