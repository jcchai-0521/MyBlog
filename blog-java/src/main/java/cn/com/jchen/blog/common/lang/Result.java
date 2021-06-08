package cn.com.jchen.blog.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * 前后端交付的统一结果封装类
 */
@Data
public class Result implements Serializable
{
    private int code;
    private String  messgae;
    private Object data;

    /**
     * 成功的响应
     * @param data 响应数据
     * @return 成功响应的统一对象
     */
    public static Result success(Object data)
    {
        Result result = new Result();
        result.code = 200;
        result.messgae = "操作成功！";
        result.data = data;
        return result;
    }

    /**
     * 失败的响应
     * @param errMsg 错误信息
     * @return 成功响应的统一对象
     */
    public static Result fail(String errMsg)
    {
        Result result = new Result();
        result.code = 500;
        result.messgae = errMsg;
        result.data = null;
        return result;
    }

    /**
     * 失败的响应
     * @param errMsg 错误信息
     * @return 成功响应的统一对象
     */
    public static Result fail(int errCode, String errMsg)
    {
        Result result = new Result();
        result.code = errCode;
        result.messgae = errMsg;
        result.data = null;
        return result;
    }
}
