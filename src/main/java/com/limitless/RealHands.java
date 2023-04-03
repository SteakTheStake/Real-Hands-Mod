package com.limitless;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class RealHands implements ModInitializer {

	public static final Identifier IMAGE = new Identifier("realhands:assets/textures/gui/hand.png");
	private Method renderHandMethod;

	@Override
	public void onInitialize() {
		try {
			renderHandMethod = GameRenderer.class.getDeclaredMethod("renderHand", MatrixStack.class, MinecraftClient.class, float.class);
			renderHandMethod.setAccessible(true);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		HudRenderCallback.EVENT.register((matrixStack, delta) -> renderCustomArm(matrixStack));


	}

	private void renderCustomArm(MatrixStack stack) {
		MinecraftClient client = MinecraftClient.getInstance();

		try {
			renderHandMethod.invoke(client.gameRenderer, stack, client, client.getTickDelta());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(IMAGE).apply(new Identifier("realhands", "assets/realhands/textures/gui/hand.png"));
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(0, 0, 0).texture(sprite.getMinU(), sprite.getMinV()).next();
		bufferBuilder.vertex(1024, 0, 0).texture(sprite.getMaxU(), sprite.getMinV()).next();
		bufferBuilder.vertex(1024, 1024, 0).texture(sprite.getMaxU(), sprite.getMaxV()).next();
		bufferBuilder.vertex(0, 1024, 0).texture(sprite.getMinU(), sprite.getMaxV()).next();
		bufferBuilder.end();

		MatrixStack matrixStack = new MatrixStack();
// Push a new matrix onto the stack
		matrixStack.push();

// Do some transformations
		matrixStack.translate(1.0, 2.0, 3.0);
		matrixStack.scale(2.0f, 2.0f, 2.0f);

// Get the top entry from the stack and retrieve the transformation matrix
		Matrix4f modelMatrix = matrixStack.peek().getPositionMatrix();

	}
}
