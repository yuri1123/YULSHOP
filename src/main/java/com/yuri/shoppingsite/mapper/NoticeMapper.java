package com.yuri.shoppingsite.mapper;

import com.yuri.shoppingsite.domain.community.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {



//    전체리스트 출력
    @Select("select * from notice order by noticeId")
    List<Notice> listAll();

//    선택 출력
    @Select("select * from notice where noticeId = #{noticeId} order by noticeId")
    Notice selectOne(int noticeId);

//    등록하기
    @Insert("insert into notice (noticeTitle, noticeContent, noticeWriter, noticeRegDate) " +
            "values (#{noticeTitle}, #{noticeContent}, #{noticeWriter}, now()")
    int register(Notice notice);
//    수정하기
    @Update("update notice set noticeTitle=#{noticeTitle}, noticeContent=#{noticeContent}, noticeWriter=#{noticeWriter}" +
            "where noticeId=#{noticeId}")
    int update(Notice notice);

//    삭제하기
    @Delete("delete from notice where noticeId=#{noticeId}")
    int delete(int noticeId);
//     조회수 증가
    @Update("update notice set noticeView=noticeView +1 where noticeId = #{noticeId}")
    int updateView(int noticeId);
}
