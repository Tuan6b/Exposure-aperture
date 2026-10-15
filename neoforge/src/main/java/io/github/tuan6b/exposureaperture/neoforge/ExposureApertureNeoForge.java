package io.github.tuan6b.exposureaperture.neoforge;

import io.github.tuan6b.exposureaperture.ExposureAperture;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ExposureAperture.ID)
public class ExposureApertureNeoForge {
	private static final DeferredRegister<DataComponentType<?>> COMPONENTS =
			DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ExposureAperture.ID);

	public ExposureApertureNeoForge(IEventBus modBus) {
		// NeoForge registries only accept entries during RegisterEvent; the instance itself is built in common code.
		COMPONENTS.register(ExposureAperture.APERTURE_ID.getPath(), () -> ExposureAperture.APERTURE_COMPONENT);
		COMPONENTS.register(modBus);
	}
}
