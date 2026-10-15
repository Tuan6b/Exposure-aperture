package io.github.tuan6b.exposureaperture;

import com.mojang.serialization.Codec;
import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.world.item.camera.CameraSetting;
import io.github.mortuusars.exposure.world.item.camera.CameraSettings;
import io.github.mortuusars.exposure.world.sound.SoundEffect;
import java.util.List;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

/**
 * Adds an aperture to Exposure cameras. It is an Exposure {@link CameraSetting}, so it lives on the camera item and
 * reaches the server through Exposure's own setting packet, exactly like zoom and shutter speed.
 * Loaded on both sides: the server resolves settings by id. Each loader registers {@link #APERTURE_COMPONENT}
 * under {@link #APERTURE_ID} - built here, like Exposure's own components, because the setting needs the instance.
 */
public final class ExposureAperture {
	public static final String ID = "exposure_aperture";
	public static final ResourceLocation APERTURE_ID = ResourceLocation.fromNamespaceAndPath(ID, "aperture");

	/** Full stops - the same list as Lumarit's DSLR_APERTURE option. */
	public static final List<Float> F_STOPS = List.of(1.4f, 2f, 2.8f, 4f, 5.6f, 8f, 11f, 16f, 22f);

	public static final DataComponentType<Float> APERTURE_COMPONENT =
			DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build();

	public static final CameraSetting<Float> APERTURE = CameraSettings.register(APERTURE_ID,
			new CameraSetting<>(APERTURE_COMPONENT, 2.8f, new SoundEffect(Exposure.SoundEvents.CAMERA_LENS_RING_CLICK)));

	private ExposureAperture() {
	}

	/** The stop in {@link #F_STOPS} closest to {@code fNumber} - a command or datapack can store any float. */
	public static float nearestStop(float fNumber) {
		float best = F_STOPS.getFirst();
		for (float stop : F_STOPS) {
			if (Math.abs(stop - fNumber) < Math.abs(best - fNumber)) best = stop;
		}
		return best;
	}
}
