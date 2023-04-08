package com.limitless;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import static com.limitless.RealHands.IMAGE;

public class HandRenderer {
    private final Camera camera;
    private final MatrixStack matrixStack;
    private final BufferBuilder bufferBuilder;
    private static final Identifier HAND_TEXTURE = new Identifier("limitless:textures/gui/hand.png");
    public HandRenderer(Camera camera, MatrixStack matrixStack) {
        this.camera = camera;
        this.matrixStack = matrixStack;
        this.bufferBuilder = Tessellator.getInstance().getBuffer();
    }

    public void render() {
        Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(IMAGE).apply(HAND_TEXTURE);

        RenderLayer renderLayer = RenderLayer.getSolid();
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        matrixStack.push();
        matrixStack.translate(-0.6, -0.75, -0.71999998);

        matrixStack.multiply(Direction.UP.getRotationQuaternion());

        float scale = 1.6F;
        matrixStack.scale(scale, scale, scale);

        VertexConsumer vertexConsumer = immediate.getBuffer(renderLayer);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

        bufferBuilder.end();

        immediate.draw(renderLayer);

        matrixStack.pop();
    }
}
