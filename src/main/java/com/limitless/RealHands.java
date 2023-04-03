package com.limitless;

		import net.fabricmc.api.ClientModInitializer;
		import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
		import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
		import net.minecraft.client.MinecraftClient;
		import net.minecraft.client.render.*;
		import net.minecraft.client.texture.Sprite;
		import net.minecraft.client.util.math.MatrixStack;
		import net.minecraft.util.Identifier;
		import org.joml.Matrix4f;

		import java.lang.reflect.InvocationTargetException;
		import java.lang.reflect.Method;


public class RealHands implements ClientModInitializer {

	public static final Identifier IMAGE = new Identifier("realhands", "textures/gui/hand.png");
	private Method renderHandMethod;
	private HandModel handModel; // Declare a HandModel object


	@Override
	public void onInitializeClient() {
		try {
			renderHandMethod = GameRenderer.class.getDeclaredMethod("renderHand", MatrixStack.class, VertexConsumerProvider.class, BufferBuilder.class, float.class, int.class);
			renderHandMethod.setAccessible(true);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		// Instantiate the HandModel object
		handModel = new HandModel();

		HudRenderCallback.EVENT.register((matrixStack, delta) -> renderCustomArm(matrixStack));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// Use the HandModel object to render the hand
			handModel.renderHand(client.getTickDelta());
		});
	}

	private void renderCustomArm(MatrixStack stack) {
		MinecraftClient client = MinecraftClient.getInstance();

		try {
			renderHandMethod.invoke(client.gameRenderer, stack, client.getBufferBuilders().getEntityVertexConsumers(), Tessellator.getInstance().getBuffer(), client.getTickDelta(), 0);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(IMAGE).apply(new Identifier("realhands", "textures/gui/hand.png"));
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(0, 0, 0).texture(sprite.getMinU(), sprite.getMinV()).next();
		bufferBuilder.vertex(1024, 0, 0).texture(sprite.getMaxU(), sprite.getMinV()).next();
		bufferBuilder.vertex(1024, 1024, 0).texture(sprite.getMaxU(), sprite.getMaxV()).next();
		bufferBuilder.vertex(0, 1024, 0).texture(sprite.getMinU(), sprite.getMaxV()).next();
		bufferBuilder.end();

		MatrixStack matrixStack = new MatrixStack();
		matrixStack.push();

		matrixStack.translate(1.0, 2.0, 3.0);
		matrixStack.scale(2.0f, 2.0f, 2.0f);

		Matrix4f modelMatrix = matrixStack.peek().getPositionMatrix();

	}
}
