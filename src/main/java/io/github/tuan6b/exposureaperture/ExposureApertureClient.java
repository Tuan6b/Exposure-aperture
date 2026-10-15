package io.github.tuan6b.exposureaperture;

import io.github.mortuusars.exposure.Config;
import io.github.mortuusars.exposure.client.camera.CameraClient;
import io.github.mortuusars.exposure.client.camera.viewfinder.Viewfinder;
import io.github.mortuusars.exposure.client.camera.viewfinder.ViewfinderCameraControlsScreen;
import io.github.mortuusars.exposure.client.gui.component.CycleButton;
import io.github.mortuusars.exposure.client.gui.screen.camera.button.ShutterSpeedButton;
import io.github.mortuusars.exposure.util.Fov;
import io.github.mortuusars.exposure.world.camera.Camera;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/**
 * Feeds the camera in use to the shader (read by the Iris uniforms in CommonUniformsMixin) and adds the aperture dial
 * to Exposure's camera controls. Nothing is written to the shader pack's settings: while no camera is in use both
 * values are 0 and Lumarit falls back to its own options, so they come back by themselves when the camera is lowered.
 */
public class ExposureApertureClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (!(screen instanceof ViewfinderCameraControlsScreen controls)) return;
			List<AbstractWidget> widgets = Screens.getButtons(screen);
			// Stacked on top of the shutter speed dial, wherever Exposure laid that out.
			widgets.stream().filter(w -> w instanceof ShutterSpeedButton).findFirst()
					.map(shutter -> apertureDial(controls.getCamera(), shutter.getX(), shutter.getY() - shutter.getHeight() - 1))
					.ifPresent(widgets::add);
		});
	}

	/** The viewfinder the player is looking through right now, or null. */
	private static @Nullable Viewfinder viewfinder() {
		Viewfinder viewfinder = CameraClient.viewfinder();
		return viewfinder != null && viewfinder.isLookingThrough() ? viewfinder : null;
	}

	/** f-number of the camera in use; 0 when there is none. */
	public static float fNumber() {
		Viewfinder viewfinder = viewfinder();
		return viewfinder == null ? 0f : ExposureAperture.APERTURE.getOrDefault(viewfinder.camera());
	}

	/** Focal length in mm of the camera in use, following the zoom animation; 0 when there is none. */
	public static float focalLength() {
		Viewfinder viewfinder = viewfinder();
		return viewfinder == null ? 0f : (float) Fov.fovToFocalLength(viewfinder.zoom().getCurrentFov());
	}

	private static AbstractWidget apertureDial(Camera camera, int x, int y) {
		float current = ExposureAperture.nearestStop(ExposureAperture.APERTURE.getOrDefault(camera));
		int mainColor = Config.getColor(Config.Client.VIEWFINDER_FONT_MAIN_COLOR);
		int shadowColor = Config.getColor(Config.Client.VIEWFINDER_FONT_SECONDARY_COLOR);
		// Exposure's shutter speed dial sprite: a blank knurled dial, the text is drawn on top.
		return new CycleButton<Float>(x, y, 69, 12, ExposureAperture.F_STOPS, current,
				stop -> ViewfinderCameraControlsScreen.SHUTTER_SPEED_SPRITES) {
			@Override
			public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
				super.renderWidget(graphics, mouseX, mouseY, partialTick);
				float stop = getCurrentValue();
				String text = "f/" + (stop == Math.round(stop) ? Integer.toString(Math.round(stop)) : Float.toString(stop));
				Font font = Minecraft.getInstance().font;
				int textX = getX() + width / 2 - font.width(text) / 2 + 1;
				graphics.drawString(font, text, textX, getY() + 4, shadowColor, false);
				graphics.drawString(font, text, textX, getY() + 3, mainColor, false);
			}
		}
				.setDefaultTooltip(Tooltip.create(Component.translatable("gui.exposure_aperture.camera_controls.aperture.tooltip")))
				.onCycle(stop -> ExposureAperture.APERTURE.setAndSync(camera, stop));
	}
}
