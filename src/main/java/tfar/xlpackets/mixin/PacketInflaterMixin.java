package tfar.xlpackets.mixin;

import net.minecraft.network.NettyCompressionDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NettyCompressionDecoder.class)
public class PacketInflaterMixin {
	@ModifyConstant(method = "decode",constant = @Constant(intValue = 2097152))
	private int xlPackets(int old) {
		return 2000000000;
	}
}
