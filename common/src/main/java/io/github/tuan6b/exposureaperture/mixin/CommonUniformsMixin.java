package io.github.tuan6b.exposureaperture.mixin;

import io.github.tuan6b.exposureaperture.ExposureApertureClient;
import net.irisshaders.iris.gl.uniform.FloatSupplier;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.shaderpack.IdMap;
import net.irisshaders.iris.shaderpack.properties.PackDirectives;
import net.irisshaders.iris.uniforms.CommonUniforms;
import net.irisshaders.iris.uniforms.FrameUpdateNotifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Iris has no API for extra uniforms. addNonDynamicUniforms builds the input set every program of a pack gets (and that
 * shaders.properties custom uniforms can read), so adding to it reaches gbuffers, composites, final and computes.
 */
@Mixin(value = CommonUniforms.class, remap = false)
public class CommonUniformsMixin {
	@Inject(method = "addNonDynamicUniforms", at = @At("TAIL"))
	private static void exposureAperture$addLensUniforms(UniformHolder uniforms, IdMap idMap, PackDirectives directives,
			FrameUpdateNotifier updateNotifier, CallbackInfo ci) {
		uniforms.uniform1f(UniformUpdateFrequency.PER_FRAME, "exposureFNumber", (FloatSupplier) ExposureApertureClient::fNumber)
				.uniform1f(UniformUpdateFrequency.PER_FRAME, "exposureFocalLength", (FloatSupplier) ExposureApertureClient::focalLength)
				.uniform1f(UniformUpdateFrequency.PER_FRAME, "exposureShutterTime", (FloatSupplier) ExposureApertureClient::shutterTime);
	}
}
