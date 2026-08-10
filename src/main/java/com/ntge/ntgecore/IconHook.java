package com.ntge.ntgecore;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public final class IconHook {

    // Sizes LWJGL2's Display.setIcon actually looks for per platform:
    // Windows wants 16x16 + 32x32, Linux wants 32x32, Mac OS X wants 128x128.
    // 256 is included too, for anything that prefers a larger source.
    private static final int[] ICON_SIZES = { 16, 32, 128, 256 };

    private IconHook() {}

    public static ByteBuffer[] getIconBuffers() {
        BufferedImage source = loadSourceImage(Config.ICON_PATH);

        ByteBuffer[] buffers = new ByteBuffer[ICON_SIZES.length];
        for (int i = 0; i < ICON_SIZES.length; i++) {
            buffers[i] = toBuffer(scale(source, ICON_SIZES[i]));
        }
        return buffers;
    }

    private static BufferedImage loadSourceImage(String path) {
        InputStream inputStream = null;
        try {
            inputStream = IconHook.class.getResourceAsStream(path);
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("Unable to decode icon resource: " + path);
            }
            return image;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private static BufferedImage scale(BufferedImage source, int size) {
        if (source.getWidth() == size && source.getHeight() == size) {
            return source;
        }
        BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaled.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(source, 0, 0, size, size, null);
        graphics.dispose();
        return scaled;
    }

    private static ByteBuffer toBuffer(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();
        return buffer;
    }
}
