package tfar.xlpackets.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.CompressionEncoder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.logging.PacketDump;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.zip.Deflater;

@Mixin(CompressionEncoder.class)
public class CompressionEncoderMixin {

    @Shadow @Final private static boolean DISABLE_PACKET_DEBUG;
    @Shadow private int threshold;
    @Shadow private Deflater deflater;
    @Shadow private byte[] encodeBuf;

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "encode*", at = @At("HEAD"), cancellable = true)
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2, CallbackInfo ci) {
        ci.cancel();
        int i = byteBuf.readableBytes();
        FriendlyByteBuf friendlybytebuf = new FriendlyByteBuf(byteBuf2);
        if (i < this.threshold) {
            friendlybytebuf.writeVarInt(0);
            friendlybytebuf.writeBytes(byteBuf);
        } else {
            if (!DISABLE_PACKET_DEBUG && i > 2000000000) {
                byteBuf.markReaderIndex();
                LOGGER.error("Attempted to send packet over maximum protocol size: {} > 2000000000\nData:\n{}", i, PacketDump.getContentDump(byteBuf));
                byteBuf.resetReaderIndex();
            }

            byte[] abyte = new byte[i];
            byteBuf.readBytes(abyte);
            friendlybytebuf.writeVarInt(abyte.length);
            this.deflater.setInput(abyte, 0, i);
            this.deflater.finish();

            while(!this.deflater.finished()) {
                int j = this.deflater.deflate(this.encodeBuf);
                friendlybytebuf.writeBytes(this.encodeBuf, 0, j);
            }

            this.deflater.reset();
        }
    }
}