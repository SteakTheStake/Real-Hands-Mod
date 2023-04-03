package com.limitless;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

public class ClientTickCallback {
    private static boolean hasRendered = false;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !client.isPaused()) {
                if (!hasRendered) {
                    MatrixStack matrixStack = new MatrixStack();
                    Camera camera = client.gameRenderer.getCamera();
                    HandRenderer renderer = new HandRenderer(camera, matrixStack);
                    renderer.render();
                    hasRendered = true;
                }
            } else {
                hasRendered = false;
            }
        });
    }
}
