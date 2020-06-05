package com.instagram.clone.model.dao.member;

import java.util.List;

import com.instagram.clone.model.vo.FeedVo;
import com.instagram.clone.model.vo.MemberJoinProfileSimpleVo;
import com.instagram.clone.model.vo.MemberJoinProfileVo;
import com.instagram.clone.model.vo.MemberProfileVo;
import com.instagram.clone.model.vo.MemberVo;

public interface MemberDao {

   String NAMESPACE = "member.";

   /*
    * 
    */
   public int emailCheck(MemberVo vo);

   /*
    * 
    */
   public int insertProfile(MemberProfileVo memberProfileVo);

   public MemberProfileVo selectMemberProfile(int member_code);

   public int updateMemberProfileImage(MemberProfileVo member_profile);

   public List<MemberJoinProfileVo> nameSearchAutoComplete(int member_code, String member_id);

   public List<MemberJoinProfileSimpleVo> selectMemberList(List<Integer> codeList);

   public int updateMemberProfile(MemberProfileVo memberProfileVo);

   public int updateMember(MemberVo memberVo);

   public MemberProfileVo SearchProfile(String keyword);

   
   public int follow(MemberProfileVo vo);
   
   public int unfollow(MemberProfileVo vo);
   
   public int followCheck(int channel_code,int member_code);
   
   public MemberProfileVo countAll(String member_id);
   
   public List<MemberProfileVo> followerList(int member_code);
   
   public List<MemberProfileVo> followList(int member_code);
   
   public List<FeedVo> tagsSearchAutoComplete(String tag_hash);
   
}