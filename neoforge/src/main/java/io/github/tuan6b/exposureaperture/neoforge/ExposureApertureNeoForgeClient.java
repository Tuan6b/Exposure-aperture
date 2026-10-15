package io.github.tuan6b.exposureaperture.neoforge;

import io.github.tuan6b.exposureaperture.ExposureAperture;
import io.github.tuan6b.exposureaperture.ExposureApertureClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ExposureAperture.ID, dist = Dist.CLIENT)
public class ExposureApertureNeoForgeClient {
	public ExposureApertureNeoForgeClient() {
		NeoForge.EVENT_BUS.addListener((ScreenEvent.Init.Post event) ->
				ExposureApertureClient.onScreenInit(event.getScreen(), event.getListenersList(), event::addListener));
	}
}
