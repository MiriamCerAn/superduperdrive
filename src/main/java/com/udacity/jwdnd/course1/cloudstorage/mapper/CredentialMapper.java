package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> getCredentialsByUserId(Integer userid);

    @Insert("INSERT INTO CREDENTIALS (url1, username, key1, password1, userid) VALUES(#{url1}, #{username}, #{key1}, #{password1}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET url1 = #{url1}, username = #{username}, key1 = #{key1}, password1 = #{password1} WHERE credentialid = #{credentialid}")
    int update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    int delete(Integer credentialid);
}
