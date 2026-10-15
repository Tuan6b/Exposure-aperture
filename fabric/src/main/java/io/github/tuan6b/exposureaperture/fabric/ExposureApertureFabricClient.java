package io.github.tuan6b.exposureaperture.fabric;

import io.github.tuan6b.exposureaperture.ExposureApertureClient;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.AbstractWidget;

public class ExposureApertureFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			List<AbstractWidget> widgets = Screens.getButtons(screen);
			ExposureApertureClient.onScreenInit(screen, widgets, widgets::add);
		});
	}
}
