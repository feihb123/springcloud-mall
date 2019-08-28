package cn.datacharm.malluserservice.mapper;


import cn.datacharm.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * description:
 *
 * @author Purity
 * @date 2019/08/19
 */
@Mapper
@Repository
public interface UserMapper {

    @Select("select count(user_id) from t_user where user_name=#{userName}")
    int checkUserName(String userName);

    @Insert("insert into t_user (user_id,user_name,user_password,user_nickname,user_email,user_type)" +
            "values (#{userId},#{userName},#{userPassword},#{userNickname},#{userEmail},#{userType})")
    void saveUser(User user);

    @Select("select * from t_user where user_name = #{userName}" +
            " and user_password = #{userPassword}")
    User queryExist(User user);
}
