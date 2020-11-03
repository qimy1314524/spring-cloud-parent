package com.smart.aiplatformauth.utils;

import com.smart.aiplatformauth.vo.ResultVO;
import com.smart.aiplatformauth.result.Code;

/**
 * 接口返回的数据格式
 * @author: chengjz
 */
public class ResultVOUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(Code.OK.getCode());
        resultVO.setMsg("请求成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(String code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

}