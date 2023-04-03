package com.limitless;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import net.minecraft.util.math.Direction;


import static com.limitless.RealHands.IMAGE;

public class HandRenderer {
    private final Camera camera;
    private final MatrixStack matrixStack;
    private final BufferBuilder bufferBuilder;

    private final HandModel model = new HandModel();

    private static final Identifier HAND_TEXTURE = new Identifier("realhands:textures/gui/hand.png");

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

        int light = 0x00F000F0;

        for (int i = 0; i < model.quads.length; i++) {
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), model.quads[i][0], model.quads[i][1], model.quads[i][2])
                    .color(1.0F, 1.0F, 1.0F, 1.0F)
                    .texture(sprite.getFrameU(model.quads[i][4]), sprite.getFrameV(model.quads[i][5]))
                    .light(light)
                    .normal(model.quads[i][6], model.quads[i][7], model.quads[i][8])
                    .next();
        }

        bufferBuilder.end();

        immediate.draw(renderLayer);

        matrixStack.pop();
    }
}
