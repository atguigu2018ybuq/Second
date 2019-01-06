package com.hucheng.cfms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hucheng.cfms.entity.MemberCertDO;

public interface MemberCertDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberCertDO record);

    MemberCertDO selectByPrimaryKey(Integer id);

    List<MemberCertDO> selectAll();

    int updateByPrimaryKey(MemberCertDO record);

	void insertMemberCertDOList(@Param("memberCertDOList")List<MemberCertDO> memberCertDOList);
}