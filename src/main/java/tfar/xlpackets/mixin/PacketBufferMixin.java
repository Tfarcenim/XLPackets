package tfar.xlpackets.mixin;

import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PacketBuffer.class)
public class PacketBufferMixin {

    @ModifyConstant(method = "readCompoundTag",constant = @Constant(longValue = 2097152L))
    private long xlPackets(long constant) {
        return 2_000_000_000L;
    }
}
