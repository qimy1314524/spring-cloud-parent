package com.smart.aiplatformauth.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.smart.aiplatformauth.model.NodeFormInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * BPM工作流节点表单信息mapper服务接口类
 * @author: chengjz
 */
@Repository
//@Mapper
public interface NodeFormInfoMapper extends BaseMapper<NodeFormInfo> {

}
