package com.scj.spring.handler.out;

import com.scj.spring.configration.StaticMap;
import com.scj.spring.entity.FoolProtocol;
import com.scj.spring.serialize.FoolSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author suchangjie.NANKE
 * @Title: FoolProtocolDecodec
 * @date 2023/8/13 00:34
 * @description 消息协议编码组件
 */

public class FoolProtocolEncode<T> extends MessageToByteEncoder<FoolProtocol<T>> {

    @Override
    protected void encode(ChannelHandlerContext context,
                          FoolProtocol<T> foolProtocol, ByteBuf byteBuf) throws Exception {
        // 写入魔数
        byteBuf.writeShort(foolProtocol.getMagic());

        // 写入版本号
        byteBuf.writeByte(foolProtocol.getVersion());

        // 写入请求类型
        byteBuf.writeByte(foolProtocol.getRemoteType());

        // 写入序列化方法
        byteBuf.writeByte(foolProtocol.getSerializableType());

        // 写入请求ID
        byteBuf.writeLong(foolProtocol.getReqId());

        // 获取序列化实例
        FoolSerialize foolSerialize = StaticMap.getFoolSerialize(foolProtocol.getSerializableType());

        // 序列化请求体数据
        byte[] data = foolSerialize.serialize(foolProtocol.getData());

        // 写入请求体长度
        byteBuf.writeInt(data.length);

        // 写入请求体
        byteBuf.writeBytes(data);
    }
}
