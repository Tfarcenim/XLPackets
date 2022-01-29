package tfar.xlpackets.mixin;

import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SCustomPayloadPlayPacket.class)
public class SCustomPayloadPacketMixin {

    @ModifyConstant(method = {"<init>()V","readPacketData"},constant = @Constant(intValue = 1048576))
    private int xlPackets(int constant) {
        return 2000000000;
    }
}
