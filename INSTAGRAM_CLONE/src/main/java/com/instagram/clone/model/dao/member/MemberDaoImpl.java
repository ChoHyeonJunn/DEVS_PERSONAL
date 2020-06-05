package com.instagram.clone.model.dao.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.instagram.clone.model.vo.FeedVo;
import com.instagram.clone.model.vo.MemberJoinProfileSimpleVo;
import com.instagram.clone.model.vo.MemberJoinProfileVo;
import com.instagram.clone.model.vo.MemberProfileVo;
import com.instagram.clone.model.vo.MemberVo;

@Repository
public class MemberDaoImpl implements MemberDao {

   @Autowired
   private SqlSessionTemplate sqlSession;

   
   /*
    * 
    */
   @Override
   public int emailCheck(MemberVo vo) {
      return sqlSession.selectOne(NAMESPACE + "emailCheck", vo);
   }

   /*
    * 
    */
   @Override
   public int insertProfile(MemberProfileVo memberProfileVo) {
      return sqlSession.insert(NAMESPACE + "insertProfile", memberProfileVo);
   }

   @Override
   public MemberProfileVo selectMemberProfile(int member_code) {
      return sqlSession.selectOne(NAMESPACE + "selectMemberProfile", member_code);
   }

   @Override
   public int updateMemberProfileImage(MemberProfileVo member_profile) {
      System.out.println(member_profile);
      return sqlSession.update(NAMESPACE + "updateMemberProfileImage", member_profile);
   }

   @Override
   public int updateMemberProfile(MemberProfileVo memberProfileVo) {
      System.out.println(memberProfileVo);
      return sqlSession.update(NAMESPACE + "updateMemberProfile", memberProfileVo);
   }

   @Override
   public int updateMember(MemberVo memberVo) {
      System.out.println(memberVo);
      return sqlSession.update(NAMESPACE + "updateMember", memberVo);
   }

   @Override
   public List<MemberJoinProfileVo> nameSearchAutoComplete(int member_code, String member_id) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("member_code", member_code);
      map.put("member_id", member_id);

      return sqlSession.selectList(NAMESPACE + "nameSearchAutoComplete", map);
   }

   @Override
   public List<MemberJoinProfileSimpleVo> selectMemberList(List<Integer> codeList) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("codeList", codeList);

      return sqlSession.selectList(NAMESPACE + "selectMemberList", map);
   }

   @Override
   public MemberProfileVo SearchProfile(String keyword) {
      return sqlSession.selectOne(NAMESPACE + "searchProfile", keyword);
   }

	@Override
	public int follow(MemberProfileVo vo) {
		return sqlSession.insert(NAMESPACE+"follow", vo);
	}

	@Override
	public int unfollow(MemberProfileVo vo) {
		return sqlSession.delete(NAMESPACE+"unfollow", vo);
	}

	@Override
   public int followCheck(int channel_code, int member_code) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("channel_code", channel_code);
      map.put("member_code", member_code);
      return sqlSession.selectOne(NAMESPACE+"followCheck", map);
   }

	@Override
	public MemberProfileVo countAll(String member_id) {
		int board_count = sqlSession.selectOne(NAMESPACE +"searchBoard_Count", member_id);
		int follow_count = sqlSession.selectOne(NAMESPACE +"searchFollow_Count", member_id);
		int follower_count = sqlSession.selectOne(NAMESPACE+"searchFollowers_Count", member_id);
		MemberProfileVo count = new MemberProfileVo();
		count.setBoard_count(board_count);
		count.setFollow_count(follow_count);
		count.setFollower_count(follower_count);
		
		return count;
	}
	
	   @Override
	   public List<MemberProfileVo> followerList(int member_code) {
	      return sqlSession.selectList(NAMESPACE + "followerList", member_code);
	   }

	   @Override
	   public List<MemberProfileVo> followList(int member_code) {
	      return sqlSession.selectList(NAMESPACE + "followList", member_code);
	   }	
	   @Override
	   public List<FeedVo> tagsSearchAutoComplete(String tag_hash) {
	      Map<String, Object> map = new HashMap<String, Object>();
	      map.put("tag_hash", tag_hash);
	      
	      return sqlSession.selectList(NAMESPACE + "tagsSearchAutoComplete", map);
	   }   

}