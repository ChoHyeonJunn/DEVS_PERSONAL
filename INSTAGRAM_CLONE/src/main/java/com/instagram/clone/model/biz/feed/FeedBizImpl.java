package com.instagram.clone.model.biz.feed;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instagram.clone.model.dao.feed.FeedDao;
import com.instagram.clone.model.vo.FeedVo;
import com.instagram.clone.model.vo.TagVo;

@Service
public class FeedBizImpl implements FeedBiz {

	@Autowired
	private FeedDao dao;

	
	@Override
	public List<FeedVo> allFollowingFeed(int start_no,int member_code) {
		return dao.allFollowingFeed(start_no, member_code);
	}
	
	@Override
	public int insertFeed(FeedVo vo) {
		return dao.insertFeed(vo);
	}

	
	
	@Override
	public int updateFeed(FeedVo vo) {
		return dao.updateFeed(vo);
	}

	@Override
	public int deleteFeed(int board_code) {
		return dao.deleteFeed(board_code);
	}

	@Override
	public FeedVo selectFeed(int board_code) {
		return dao.selectFeed(board_code);
	}

	@Override
	public List<FeedVo> myFeedList(int channel_code) {
		
		return dao.myFeedList(channel_code);
	}

	@Override
	public int insertReply(FeedVo vo) {
		return dao.insertReply(vo);
	}

	@Override
	public List<FeedVo> allReply(int board_code) {
		return dao.allReply(board_code);
	}

	@Override
	public int deleteReply(int reply_code) {
		return dao.deleteReply(reply_code);
	}

	@Override
	public FeedVo insertReplyView(int reply_code) {
		return dao.insertReplyView(reply_code);
	}

	@Override
	public int isThisFeedLiked(int board_code, int member_code) {
		return dao.isThisFeedLiked(board_code, member_code);
	}

	@Override
	public int boardLike(int board_code, int member_code) {
		return dao.boardLike(board_code, member_code);
	}

	@Override
	public int boardUnlike(int board_code, int member_code) {
		return dao.boardUnlike(board_code, member_code);
	}

	@Override
	public FeedVo FeedListReply(int board_code) {
		return dao.FeedListReply(board_code);
	}

	@Override
	public List<FeedVo> randomFeedList(int member_code) {
		// TODO Auto-generated method stub
		return dao.randomFeedList(member_code);
	}

	@Override
   public int insertHashtag(List<TagVo> tagList) {
      return dao.insertHashtag(tagList);
   }

	@Override
	   public FeedVo selectLatestFeed(int member_code) {
	      return dao.selectLatestFeed(member_code);
	   }

	@Override
	public List<FeedVo> tagBoardList(String hashtag) {
		// TODO Auto-generated method stub
		return dao.tagBoardList(hashtag);
	}

	@Override
	public int deleteHashtag(int board_code) {
		// TODO Auto-generated method stub
		return dao.deleteHashtag(board_code);
	}
	
	@Override
	   public List<FeedVo> likeFeedList(int start_no, int member_code) {
	      return dao.likeFeedList(start_no, member_code);
	   }
}
