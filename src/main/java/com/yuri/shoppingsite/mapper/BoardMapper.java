package com.yuri.shoppingsite.mapper;

import com.yuri.shoppingsite.domain.community.BoardDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Select("select * from board order by bid desc")
    public List<BoardDTO> listAll();

    @Select("select * from board where bid =#{bid} order by bid desc")
    public BoardDTO selectOne(int bid);

    @Insert("insert into board (title,content,userid) values (#{title}, #{content}, #{userid})")
    public int register(BoardDTO boardDTO);

    @Update("update board set title=#{title}, content = #{content}, userid=#{userid} where bid = #{bid}")
    public int update(BoardDTO boardDTO);

    @Delete("delete from board where bid =#{bid}")
    public int delete(int bid);


}
