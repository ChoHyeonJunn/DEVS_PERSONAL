package com.instagram.clone.model.biz.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instagram.clone.model.dao.member.MemberDao;
import com.instagram.clone.model.vo.FeedVo;
import com.instagram.clone.model.vo.MemberJoinProfileSimpleVo;
import com.instagram.clone.model.vo.MemberJoinProfileVo;
import com.instagram.clone.model.vo.MemberProfileVo;
import com.instagram.clone.model.vo.MemberVo;

@Service
public class MemberBizImpl implements MemberBiz {

	@Autowired
	private MemberDao dao;

	/*
	* 
	*/
	@Override
	public int emailCheck(MemberVo vo) {
		return dao.emailCheck(vo);
	}

	/*
	* 
	*/
	@Override
	public MemberProfileVo selectMemberProfile(int member_code) {
		return dao.selectMemberProfile(member_code);
	}

	@Override
	public int updateMemberProfileImage(MemberProfileVo member_profile) {
		return dao.updateMemberProfileImage(member_profile);
	}

	@Override
	public int updateMemberProfile(MemberProfileVo memberProfileVo) {
		return dao.updateMemberProfile(memberProfileVo);
	}

	@Override
	public int updateMember(MemberVo memberVo) {
		return dao.updateMember(memberVo);
	}

	@Override
	public List<MemberJoinProfileVo> nameSearchAutoComplete(int member_code, String member_id) {
	      return dao.nameSearchAutoComplete(member_code, member_id);
	}

	@Override
	public List<MemberJoinProfileSimpleVo> selectMemberList(List<Integer> codeList) {
		return dao.selectMemberList(codeList);
	}

	@Override
	public MemberProfileVo SearchProfile(String keyword) {
		return dao.SearchProfile(keyword);
	}
	


	@Override
	public int follow(MemberProfileVo vo) {
		return dao.follow(vo);
	}

	@Override
	public int unfollow(MemberProfileVo vo) {
		return dao.unfollow(vo);
	}

	@Override
   public int followCheck(int channel_code,int member_code) {
      return dao.followCheck(channel_code, member_code);
   }

	@Override
	public MemberProfileVo countAll(String member_id) {
		// TODO Auto-generated method stub
		return dao.countAll(member_id);
	}

   @Override
   public List<MemberProfileVo> followerList(int member_code) {
      return dao.followerList(member_code);
   }

   @Override
   public List<MemberProfileVo> followList(int member_code) {
      return dao.followList(member_code);
   }
   
   @Override
   public List<FeedVo> tagsSearchAutoComplete(String tag_hash) {
      return dao.tagsSearchAutoComplete(tag_hash);
   }
}