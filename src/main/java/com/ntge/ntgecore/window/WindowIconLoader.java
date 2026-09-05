package com.ntge.ntgecore.window;

import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class WindowIconLoader {

    private static final Logger LOG = Logger.getLogger("NtgeCore");

    private static final String[] ICON_PATHS = {
            "/assets/ntgecore/icon_16x16.png",
            "/assets/ntgecore/icon_32x32.png",
            "/assets/ntgecore/icon_48x48.png",
            "/assets/ntgecore/icon_128x128.png"
    };

    private WindowIconLoader() {
    }

    public static void apply() {
        List<ByteBuffer> buffers = new ArrayList<ByteBuffer>(ICON_PATHS.length);

        for (String path : ICON_PATHS) {
            BufferedImage image = loadImage(path);
            if (image != null) {
                buffers.add(toByteBuffer(image));
            }
        }

        if (buffers.isEmpty()) {
            LOG.warning("No icon files found under /assets/ntgecore, leaving the default icon in place.");
            return;
        }

        try {
            Display.setIcon(buffers.toArray(new ByteBuffer[buffers.size()]));
        } catch (Exception e) {
            // Losing the window icon isn't worth taking the whole game down over.
            LOG.log(Level.WARNING, "Failed to apply the custom window icon", e);
        }
    }

    private static BufferedImage loadImage(String path) {
        InputStream in = WindowIconLoader.class.getResourceAsStream(path);
        if (in == null) {
            return null;
        }

        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Couldn't read " + path, e);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException ignored) {
                // don't care
            }
        }
    }

    private static ByteBuffer toByteBuffer(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        ByteBuffer buffer = ByteBuffer.allocate(width * height * 4);
        for (int pixel : pixels) {
            buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
            buffer.put((byte) ((pixel >> 8) & 0xFF));  // G
            buffer.put((byte) (pixel & 0xFF));         // B
            buffer.put((byte) ((pixel >> 24) & 0xFF)); // A
        }
        buffer.flip();
        return buffer;
    }
}