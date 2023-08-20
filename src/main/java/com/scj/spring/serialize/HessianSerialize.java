package com.scj.spring.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.scj.spring.constant.Constant;
import io.netty.util.internal.ObjectUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author suchangjie.NANKE
 * @Title: HessianSerialize
 * @date 2023/8/19 20:05
 * @description Hessian序列化类
 */
public class HessianSerialize implements FoolSerialize{

    @Override
    public byte[] serialize(Object obj) {
        // 当 obj == null 时会抛出空指针异常
        ObjectUtil.checkNotNull(obj, "序列化对象为空");
        // Hessian 序列化标准代码
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HessianOutput output = new HessianOutput(outputStream);
        try {
            output.writeObject(obj);
            output.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("序列化异常 error:"
                    + e.getMessage());
        }
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> clazz) {
        ObjectUtil.checkNotNull(data, "反序列化数据为空");
        ObjectUtil.checkNotNull(clazz, "类对象数据为空");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        HessianInput input = new HessianInput(inputStream);
        try {
            return clazz.cast(input.readObject());
        } catch (IOException e) {
            throw new RuntimeException("反序列化异常 error:"
                    + e.getMessage());
        }
    }
}
