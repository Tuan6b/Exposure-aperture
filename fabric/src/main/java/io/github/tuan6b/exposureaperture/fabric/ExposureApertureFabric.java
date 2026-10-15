package io.github.tuan6b.exposureaperture.fabric;

import io.github.tuan6b.exposureaperture.ExposureAperture;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ExposureApertureFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ExposureAperture.APERTURE_ID, ExposureAperture.APERTURE_COMPONENT);
	}
}
