package com.kk.dao;

import com.kk.model.CodeContent;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface CodeContentDao {

    public void insert(CodeContent codeContent);

    public CodeContent getById(long id);

    String getLastContent(@Param("userId") String userId, @Param("type") String type);

    int getTotalContentByUserId(@Param("userId") String userId, @Param("type") String type);

    public void updateById(CodeContent codeContent);

    public void deleteById(long id);

    void deleteContentBeforeTime(@Param("time") Date time);

    void deleteContentByUserId(@Param("userId") String userId);

    void deleteContentLimitedByUserId(@Param("userId") String userId, @Param("limit") int limit);
}
