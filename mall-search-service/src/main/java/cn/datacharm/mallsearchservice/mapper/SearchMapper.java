package cn.datacharm.mallsearchservice.mapper;

import cn.datacharm.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description:
 *
 * @author Herb
 * @date 2019/09/2019-09-05
 */
@Repository
@Mapper
public interface SearchMapper {
    @Select("")
    List<Product> queryAll();
}
