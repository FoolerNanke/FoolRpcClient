package com.scj.spring.handler.in;

import com.scj.spring.configration.StaticMap;
import com.scj.spring.constant.Constant;
import com.scj.spring.entity.FoolProtocol;
import com.scj.spring.entity.FoolRequest;
import com.scj.spring.entity.FoolResponse;
import com.scj.spring.serialize.FoolSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author suchangjie.NANKE
 * @Title: FoolProtocolDecode
 * @date 2023/8/13 11:40
 * @description 消息协议解码器
 */

public class FoolProtocolDecode extends ByteToMessageDecoder {

//    private final Logger logger = LoggerFactory.getLogger(FoolProtocolDecode.class);
//
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int len = byteBuf.readableBytes();
        if (len < Constant.HEADER_LENGTH) {
            // 当前接收到的消息长度不足以解码
            return;
        }

        byteBuf.markReaderIndex();

        // 获取魔数
        short magic = byteBuf.readShort();
        if (magic != Constant.MAGIC) {
            System.out.println("协议不匹配");
            return;
        }
        // 获取版本号
        byte version = byteBuf.readByte();

        // 获取请求类型
        byte remoteType = byteBuf.readByte();

        // 获取编序列化方式
        byte serializeType = byteBuf.readByte();

        long reqId = byteBuf.readLong();

        // 获取消息体长度
        int dataLen = byteBuf.readInt();

        if (byteBuf.readableBytes() < dataLen) {
            // 消息体长度不足以解码 指针会退
            byteBuf.resetReaderIndex();
            return;
        }

        // 读取消息体
        byte[] data = new byte[dataLen];
        byteBuf.readBytes(data);

        FoolProtocol<Object> foolProtocol = new FoolProtocol<>();
        foolProtocol.setMagic(magic);
        foolProtocol.setRemoteType(remoteType);
        foolProtocol.setVersion(version);
        foolProtocol.setSerializableType(serializeType);
        foolProtocol.setReqId(reqId);

        // 消息体反序列化
        FoolSerialize foolSerialize = StaticMap.getFoolSerialize(serializeType);

        Object obj = null;
        switch (remoteType) {
            case Constant.REMOTE_REQ:
                obj = foolSerialize.deSerialize(data, FoolRequest.class);
                break;
            case Constant.REMOTE_RESP:
                obj = foolSerialize.deSerialize(data, FoolResponse.class);
                break;
        }
        // 填充消息体
        foolProtocol.setData(obj);
        list.add(foolProtocol);
    }
}
