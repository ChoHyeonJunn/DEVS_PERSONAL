package com.instagram.clone.model.dao.feed;

import java.util.List;

import com.instagram.clone.model.vo.FeedVo;
import com.instagram.clone.model.vo.TagVo;

public interface FeedDao {

	String NAMESPACE = "feed.";

	// 팔로잉 피드 전체 조회
	public List<FeedVo> allFollowingFeed(int start_no, int member_code);
	// 게시글 등록
	public int insertFeed(FeedVo vo);

	// 게시글 수정
	public int updateFeed(FeedVo vo);

	// 게시글 삭제
	public int deleteFeed(int board_code);

	// 게시글 조회
	public FeedVo selectFeed(int board_code);

	// 내 피드, 남의피드 전체조회
	public List<FeedVo> myFeedList(int channel_code);
	// 댓글 등록
	public int insertReply(FeedVo vo);
	// 댓글 조회
	public List<FeedVo> allReply(int board_code);
	// 등록 댓글 출력
	public FeedVo insertReplyView(int reply_code);
	// 댓글 삭제
	public int deleteReply(int reply_code);
	// 좋아요 확인
	public int isThisFeedLiked(int board_code, int member_code);
	// 좋아요 클릭
	public int boardLike(int board_code, int member_code);
	// 좋아요 취소
	public int boardUnlike(int board_code, int member_code);
	
	// 첫번쨰 댓글만 조회(전체피드)
	public FeedVo FeedListReply(int board_code);
	
	public List<FeedVo> randomFeedList(int member_code);
	
	// 해시태그 insert
	public int insertHashtag(List<TagVo> tagList);
	// 해시태그 update
	public int deleteHashtag(int board_code);
	// 내가 최근 쓴 글
	public FeedVo selectLatestFeed(int member_code);
	
	public List<FeedVo> tagBoardList(String hashtag);
	
	// 좋아요한 게시물 조회
	   public List<FeedVo> likeFeedList(int start_no, int member_code);
}
