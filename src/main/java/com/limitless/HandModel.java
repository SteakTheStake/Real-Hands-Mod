package com.limitless;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

public class HandModel {
    private static final float SIZE = 0.5f; // size of the square
    private static final float[] POSITIONS = {
            -SIZE, -SIZE, 0.0f,
            -SIZE, SIZE, 0.0f,
            SIZE, SIZE, 0.0f,
            SIZE, -SIZE, 0.0f
    };
    private static final float[] TEX_COORDS = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };
    private static final int[] INDICES = {
            0, 1, 2,
            2, 3, 0
    };

    private final VertexConsumerProvider vertexConsumers;

    public HandModel(VertexConsumerProvider vertexConsumers) {
        this.vertexConsumers = vertexConsumers;
    }

    public void renderHand(MatrixStack matrices, float tickDelta) {
        if (vertexConsumers == null) {
        }
            // Handle null vertexConsumers field appropriately, e.g. throw an exception or return early
            return;
        }


    public void renderHand(float tickDelta) {
        MatrixStack stack = new MatrixStack();
            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(Identifier.tryParse("textures/gui/hand.png")));

        stack.push();
        stack.multiply(new Quaternion(-90.0f, 0.0f, 0.0f, true)); // rotate the model to face the player
        Matrix4f modelMatrix = stack.peek().getModel();
        for (int i = 0; i < INDICES.length; i += 3) {
            int i1 = INDICES[i];
            int i2 = INDICES[i + 1];
            int i3 = INDICES[i + 2];

            consumer.vertex(modelMatrix, POSITIONS[i1], POSITIONS[i1 + 1], POSITIONS[i1 + 2])
                    .texture(TEX_COORDS[i1], TEX_COORDS[i1 + 1])
                    .next();
            consumer.vertex(modelMatrix, POSITIONS[i2], POSITIONS[i2 + 1], POSITIONS[i2 + 2])
                    .texture(TEX_COORDS[i2], TEX_COORDS[i2 + 1])
                    .next();
            consumer.vertex(modelMatrix, POSITIONS[i3], POSITIONS[i3 + 1], POSITIONS[i3 + 2])
                    .texture(TEX_COORDS[i3], TEX_COORDS[i3 + 1])
                    .next();
        }
        stack.pop();
    }
}
