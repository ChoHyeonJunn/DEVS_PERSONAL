package com.instagram.clone.model.dao.feed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.instagram.clone.model.vo.FeedVo;
import com.instagram.clone.model.vo.TagVo;

@Repository
public class FeedDaoImpl implements FeedDao {

	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public int insertFeed(FeedVo vo) {
		return sqlSession.insert(NAMESPACE + "insertFeed", vo);
	}

	@Override
	public int updateFeed(FeedVo vo) {
		// TODO Auto-generated method stub
		System.out.println("^^^^!!!!! daoImpl!");
		System.out.println(vo.getBoard_code());
		System.out.println(vo.getBoard_content());
		System.out.println(vo.getBoard_file_server_name());
		return sqlSession.update(NAMESPACE+"updateFeed", vo);
	}

	@Override
	public int deleteFeed(int board_code) {
		// TODO Auto-generated method stub
		return sqlSession.delete(NAMESPACE+"deleteFeed", board_code);
	}

	@Override
	public FeedVo selectFeed(int board_code) {
		
		return sqlSession.selectOne(NAMESPACE+"selectFeed", board_code);
	}

	@Override
	public List<FeedVo> myFeedList(int channel_code) {
		return sqlSession.selectList(NAMESPACE + "myFeedList", channel_code);
	}

	@Override
	public int insertReply(FeedVo vo) {
		int res = sqlSession.insert(NAMESPACE+"insertReply", vo);
		int reply_code = vo.getReply_code();
		return reply_code;
	}

	@Override
	public List<FeedVo> allReply(int board_code) {
		return sqlSession.selectList(NAMESPACE+"allReply", board_code);
	}

	@Override
	public int deleteReply(int reply_code) {
		return sqlSession.delete(NAMESPACE+"deleteReply", reply_code);
	}

	@Override
	public FeedVo insertReplyView(int reply_code) {
		
		return sqlSession.selectOne(NAMESPACE+"insertReplyView", reply_code);
	}

	@Override
	public List<FeedVo> allFollowingFeed(int start_no, int member_code) {
		Map<String, Object> map = new HashMap<String, Object>();
		int last_no = start_no + 14;
		map.put("start_no", start_no);
		map.put("member_code", member_code);
		map.put("last_no", last_no);
		return sqlSession.selectList(NAMESPACE+"allFollowingFeed", map);
	}

	@Override
	public int isThisFeedLiked(int board_code, int member_code) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("board_code", board_code);
		map.put("member_code", member_code);
		return sqlSession.selectOne(NAMESPACE+"isThisFeedLiked", map);
	}

	@Override
	public int boardLike(int board_code, int member_code) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("board_code", board_code);
		map.put("member_code", member_code);
		int insertres = sqlSession.insert(NAMESPACE+"like", map);
		int updateres = sqlSession.update(NAMESPACE+"likeCountUpdate", board_code);
		if(insertres>0 && updateres>0) {
			return 1;
		}else {
			return 0;
		}
	}

	@Override
	public int boardUnlike(int board_code, int member_code) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("board_code", board_code);
		map.put("member_code", member_code);
		int deleteres = sqlSession.delete(NAMESPACE+"unlike", map);
		int updateres = sqlSession.update(NAMESPACE+"unlikeCountUpdate", board_code);
		if(deleteres>0 && updateres>0) {
			return 1;
		}else {
			return 0;
		}
	}

	@Override
	public FeedVo FeedListReply(int board_code) {
		return sqlSession.selectOne(NAMESPACE+"feedListReply", board_code);
	}

	@Override
	public List<FeedVo> randomFeedList(int member_code) {
		return sqlSession.selectList(NAMESPACE+"randomFeedList", member_code);
	}
	
	@Override
   public int insertHashtag(List<TagVo> tagList) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("tagList", tagList);

      return sqlSession.insert(NAMESPACE + "insertHashtag", map);
   }

	@Override
	public FeedVo selectLatestFeed(int member_code) {
	   return sqlSession.selectOne(NAMESPACE+"selectLatestFeed", member_code);
	}

	@Override
	public List<FeedVo> tagBoardList(String hashtag) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("hashtag", hashtag);
		return sqlSession.selectList(NAMESPACE+"tagBoardList", map);
	}

	@Override
	public int deleteHashtag(int board_code) {
		// TODO Auto-generated method stub
		return sqlSession.delete(NAMESPACE+"deletetag", board_code);
	}
	
	@Override
	   public List<FeedVo> likeFeedList(int start_no, int member_code) {
	      Map<String, Object> map = new HashMap<String, Object>();
	      int last_no = start_no + 14;
	      map.put("start_no", start_no);
	      map.put("member_code", member_code);
	      map.put("last_no", last_no);
	      return sqlSession.selectList(NAMESPACE + "likeFeedList", map);
	   }
}
