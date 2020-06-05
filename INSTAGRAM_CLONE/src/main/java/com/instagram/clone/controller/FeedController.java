package com.instagram.clone.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.instagram.clone.common.properties.ApplicationProperties;
import com.instagram.clone.model.biz.channel.ChannelBiz;
import com.instagram.clone.model.biz.channel.ChannelBizImpl;
import com.instagram.clone.model.biz.feed.FeedBiz;
import com.instagram.clone.model.biz.feed.FeedBizImpl;
import com.instagram.clone.model.biz.member.MemberBiz;
import com.instagram.clone.model.vo.FeedVo;
import com.instagram.clone.model.vo.MemberVo;
import com.instagram.clone.model.vo.TagVo;
import com.instagram.clone.ssohandler.domain.entity.Member;
import com.instagram.clone.ssohandler.service.MemberService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@RestController
@RequestMapping("/feed/*")
public class FeedController implements ApplicationProperties {

	private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberBiz memberBiz;

	@Autowired
	private FeedBiz biz;

	@Autowired
	private ChannelBiz cbiz;

	// feed.jsp 로 이동
	@GetMapping(value = "")
	public ModelAndView mainPage(HttpServletRequest request, ModelMap map) {
		logger.info("FEED/FEED.GET");

		MemberVo memberVo = (MemberVo) request.getSession().getAttribute("user");
		System.out.println("\n## user in session : " + memberVo);

		if (memberVo == null) {
			return new ModelAndView("redirect:/");
		}

		Member member = memberService.getUser(memberVo.getMember_id());
		System.out.println("\n## user : " + member);

		if (member.getTokenId() == null) {
			request.getSession().removeAttribute("user");
			return new ModelAndView("redirect:/");
		} else {
			map.put("user", member);
			// 프로필 session 주입
			request.getSession().setAttribute("profile", memberBiz.selectMemberProfile(member.getMembercode()));
			// 서버 포트를 session에 셋팅하여 jsp 페이지에서 사용한다.
			request.getSession().setAttribute("SERVER_PORT", SERVER_PORT);
		}

		return new ModelAndView("feed/feed");
	}

	
	// feedDetail.jsp 로 이동
	@GetMapping(value="/pickedFeed")
	public ModelAndView pickedFeed(Model model, int board_code){
		FeedVo vo = biz.selectFeed(board_code);
		List<FeedVo> reply = biz.allReply(board_code);
		model.addAttribute("feed", vo);
		model.addAttribute("reply", reply);
		return new ModelAndView("feed/feedDetail");
	}	
	
	
	@ResponseBody
	@GetMapping(value = "/feedlist")
	public String feedList(int startNo, int member_code) {
		logger.info("FEED/FEEDLIST");
		System.out.println("*&*&*&**&*&**&*&*컨트롤러들어왔다!");
		List<FeedVo> list = biz.allFollowingFeed(startNo, member_code);
		Gson gson = new Gson();
		String feedList = gson.toJson(list);
		return feedList;
	}	

	@GetMapping(value = "/insertpage")
	public ModelAndView insertPage(HttpSession session, int member_code) {
		logger.info("FEED/INSERTPAGE");
		
		return new ModelAndView("feed/insertboard");
	}

	@PostMapping(value = "/insertFeed")
	   public Map<String, Object> fileUpload(@RequestParam("mpfile") MultipartFile multi, HttpServletRequest request,
	         FeedVo vo, Model model) throws IOException {

	      // 저장 경로
	      String filePath = "/resources/images/feedupload";
	      String savePath = request.getSession().getServletContext().getRealPath(filePath);

	      // 디렉토리 없다면 생성
	      File file;
	      if (!(file = new File(savePath)).isDirectory()) {
	         file.mkdirs();
	      }

	      String board_file_original_name = multi.getOriginalFilename();
	      String uuid = UUID.randomUUID().toString();
	      String board_file_server_name = uuid + board_file_original_name;
	      File targetFile = new File(savePath, board_file_server_name);
	      String board_file_ext = board_file_server_name.substring(board_file_server_name.lastIndexOf(".") + 1);
	      try {
	         InputStream fileStream = multi.getInputStream();
	         FileUtils.copyInputStreamToFile(fileStream, targetFile);
	      } catch (IOException e) {
	         FileUtils.deleteQuietly(targetFile);
	         e.printStackTrace();
	      }
	      int member_code = vo.getMember_code();
	      System.out.println("*******멤버코드 들어왔다 : " + member_code);

	      int channel_code = (cbiz.findChannelCode(member_code));

	      FeedVo voObj = new FeedVo();
	      voObj.setMember_code(vo.getMember_code());
	      voObj.setBoard_content(vo.getBoard_content());
	      voObj.setChannel_code(channel_code);
	      voObj.setBoard_file_original_name(board_file_original_name);
	      voObj.setBoard_file_server_name(board_file_server_name);
	      voObj.setBoard_file_path(savePath);
	      voObj.setBoard_file_ext(board_file_ext);

	      System.out.println(voObj.getBoard_content());
	      System.out.println(voObj.getBoard_file_original_name());
	      System.out.println(voObj.getBoard_file_server_name());
	      System.out.println(voObj.getBoard_file_ext());
	      int res = biz.insertFeed(voObj);

	      //////////////////////////////////////// 해시태그 db에 저장

	      int Latestboard_code = 0;
	      if (res > 0) {
	         FeedVo insertedFeedVo = biz.selectLatestFeed(member_code);
	         Latestboard_code = insertedFeedVo.getBoard_code();

	      }
	      ////////////////////////////////////////

	      // 동영상 업로드시 썸네일 이미지 생성
	      String str = null;
	      int i = board_file_server_name.indexOf(".");
	      String noExtension = board_file_server_name.substring(0, i);
	      String[] cmd = new String[] {
	            "C:\\instaclone\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\INSTAGRAM_CLONE_temp\\resources\\images\\feedupload\\ffmpeg",
	            "-i",
	            "C:\\instaclone\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\INSTAGRAM_CLONE_temp\\resources\\images\\feedupload\\"
	                  + board_file_server_name,
	            "-an", "-ss", "00:00:01", "-r", "1", "-vframes", "1", "-y",
	            "C:\\instaclone\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\INSTAGRAM_CLONE_temp\\resources\\images\\feedupload\\"
	                  + noExtension + ".jpg" };
	      Process process = null;
	      try {
	         if (board_file_ext.equals("mp4") || board_file_ext.equals("avi")) {
	            // 프로세스 빌더를 통하여 외부 프로그램 실행
	            process = new ProcessBuilder(cmd).start();
	            // 외부 프로그램의 표준출력 상태 버퍼에 저장
	            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            // 표준출력 상태를 출력
	            while ((str = stdOut.readLine()) != null) {
	               System.out.println(str);
	            }
	         }
	      } catch (IOException e) {
	         e.printStackTrace();
	      }

	      Map<String, Object> map = new HashMap<String, Object>();
	      map.put("board_code", Latestboard_code);
	      return map;
	   }
	
	@PostMapping(value = "/insertHashtag")
	   public Map<String, Object> insertHashtag(@RequestBody List<Map<String, Object>> listMap) {

	      int board_code = 0;
	      List<String> tags = new ArrayList<String>();
	      
	      for (Map<String, Object> map : listMap) {
	         if (map.get("tag") != null) {
	            tags.add((String) map.get("tag"));
	         } else if (map.get("board_code") != null) {
	            board_code = (Integer) map.get("board_code");
	         }
	      }
	      
	      List<TagVo> tagList = new ArrayList<TagVo>();
	      for (String tag : tags) {
	      
	         TagVo tagVo = new TagVo();
	         tagVo.setBoard_code(board_code);
	         tagVo.setTag_hash(tag);
	         
	         tagList.add(tagVo);
	      }
	      
	      System.out.println(tagList);
	      
	      //그럼 여기서 biz로 보내는거 만들어야겟네
	      int res = biz.insertHashtag(tagList);
	      Map<String, Object> resMap = new HashMap<String, Object>();
	      resMap.put("res", res);
	      return resMap;
	   }
	
	// 해시태그 달린 게시물 목록
	@GetMapping(value="/searchTagBoard")
	public ModelAndView tagBoardList(Model model, String hashtag) {
		System.out.println("######## hashcode 넘어옴!!! :"+hashtag);
		List<FeedVo> list = biz.tagBoardList("#" + hashtag);
		FeedVo vo = list.get(0);
		model.addAttribute("tag", "#"+hashtag);
		model.addAttribute("vo", vo);
		model.addAttribute("feedList", list);
		
		return new ModelAndView("/feed/taglist");
	}
	
	// 선택된 게시물
	@GetMapping(value = "/selectContent")
	public Map<String, Object> selectContent(int board_code) {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("FEED/SELECTCONTENT(AJAX)");
		FeedVo vo = biz.selectFeed(board_code);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String board_regdate = format.format(vo.getBoard_regdate());
		map.put("board_file_server_name", vo.getBoard_file_server_name());
		map.put("board_content", vo.getBoard_content());
		map.put("board_file_ext", vo.getBoard_file_ext());
		map.put("board_like_count", vo.getBoard_like_count());
		map.put("board_regdate", board_regdate);
		map.put("member_id", vo.getMember_id());
		map.put("member_img_server_name", vo.getMember_img_server_name());
		return map;
	}
	
	// 댓글 리스트
	@ResponseBody
	@GetMapping(value="/replyList")
	public String replyList(int board_code) {
		logger.info("FEED/REPLYLIST(AJAX)");
		List<FeedVo> replyList = biz.allReply(board_code);
		Gson gson = new Gson();
		String reply = gson.toJson(replyList);
		return reply;
	}
	
	// 댓글 달기
	@PostMapping(value="/insertReply")
	public Map<String, Object> insertReply(FeedVo vo) {
		logger.info("FEED/INSERTREPLY(AJAX)");
		Map<String, Object> map = new HashMap<String, Object>();
		int reply_code = biz.insertReply(vo);
		map.put("re_code", reply_code);
		return map;
	}
	
	// 댓글 달면 방금 등록된 댓글 출력
	@GetMapping(value="/insertReplyView")
	public Map<String, Object> insertReplyView(int reply_code) {
		logger.info("FEED/INSERTREPLYVIEW(AJAX)");
		Map<String, Object> map = new HashMap<String, Object>();
		FeedVo res = biz.insertReplyView(reply_code);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String reply_regdate = format.format(res.getReply_regdate());
		map.put("reply_code", res.getReply_code());
		map.put("board_code", res.getBoard_code());
		map.put("reply_content", res.getReply_content());
		map.put("reply_regdate", reply_regdate);
		map.put("reply_like_count", res.getReply_like_count());
		return map;
	}

	// 좋아요 되어있는지 확인
	@GetMapping(value="/isLiked")
	public Map<String, Boolean> isThisFeedLiked(int board_code, int member_code) {
		logger.info("FEED/ISLIKED(AJAX)");
		int count = biz.isThisFeedLiked(board_code, member_code);
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if(count != 0) {
			map.put("cnt", true);
		}else {
			map.put("cnt", false);
		}
		return map;
	}
	
	// 좋아요
	@PostMapping(value="/boardLike")
	public Map<String, Object> boardLike(int board_code, int member_code){
		logger.info("FEED/BOARDLIKE(AJAX)");
		Map<String, Object> map = new HashMap<String, Object>();
		int res = biz.boardLike(board_code, member_code);
		FeedVo vo = biz.selectFeed(board_code);
		int board_like_count = vo.getBoard_like_count();
		map.put("res", res);
		map.put("board_like_count", board_like_count);
		return map;
	}
	
	// 좋아요 취소
	@PostMapping(value="/boardUnlike")
	public Map<String, Object> boardUnlike(int board_code, int member_code){
		logger.info("FEED/BOARDUNLIKE(AJAX)");
		Map<String, Object> map = new HashMap<String, Object>();
		int res = biz.boardUnlike(board_code, member_code);
		FeedVo vo = biz.selectFeed(board_code);
		int board_like_count = vo.getBoard_like_count();
		map.put("res", res);
		map.put("board_like_count", board_like_count);
		return map;
	}
	
	// 댓글
	@GetMapping(value="/feedreply")
	public Map<String, Object> feedReply(int board_code){
		logger.info("FEED/FEEDREPLY");
		Map<String, Object> map = new HashMap<String, Object>();
		FeedVo vo = biz.FeedListReply(board_code);
		
		if(vo != null) {
		map.put("member_id", vo.getMember_id());
		map.put("board_code", vo.getBoard_code());
		map.put("reply_content", vo.getReply_content());
		map.put("reply_regdate", vo.getReply_regdate());
		
		return map;
		} else {
		return null;
		}
	}
	
	// 글 삭제
	@PostMapping(value="/boardDelete")
	public Map<String, Boolean> boardDelete(int board_code){
		logger.info("FEED/BOARDDELETE(AJAX)");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		int res = biz.deleteFeed(board_code);
		if(res>0) {
			map.put("res", true);
		}else {
			map.put("res", false);
		}
		return map;
	}
	
	// 글 수정
	@GetMapping(value="/updatepage")
	public ModelAndView updatePage(Model model, int board_code) {
		FeedVo vo = biz.selectFeed(board_code);
		model.addAttribute("board_code", board_code);
		model.addAttribute("board_content", vo.getBoard_content());
		return new ModelAndView("/feed/updateform");
	}
	
	
	@PostMapping(value = "/updateFeed")
	public Map<String, Object> updateFeed(@RequestParam("mpfile") MultipartFile multi, HttpServletRequest request, FeedVo vo,
			Model model) throws IOException {

	      // 저장 경로
	      String filePath = "/resources/images/feedupload";
	      String savePath = request.getSession().getServletContext().getRealPath(filePath);
	      int board_code = vo.getBoard_code();
	      String board_file_original_name = multi.getOriginalFilename();
	      String uuid = UUID.randomUUID().toString();
	      String board_file_server_name = uuid + board_file_original_name;
	      File targetFile = new File(savePath, board_file_server_name);
	      String board_file_ext = board_file_server_name.substring(board_file_server_name.lastIndexOf(".") + 1);
	      try {
	         InputStream fileStream = multi.getInputStream();
	         FileUtils.copyInputStreamToFile(fileStream, targetFile);
	      } catch (IOException e) {
	         FileUtils.deleteQuietly(targetFile);
	         e.printStackTrace();
	      }


	      FeedVo voObj = new FeedVo();
	      
	      voObj.setBoard_code(vo.getBoard_code());
	      voObj.setBoard_content(vo.getBoard_content());
	      voObj.setBoard_file_original_name(board_file_original_name);
	      voObj.setBoard_file_path(savePath);
	      voObj.setBoard_file_server_name(board_file_server_name);
	      voObj.setBoard_file_ext(board_file_ext);

	      int res = biz.updateFeed(voObj);

	      

	      // 동영상 업로드시 썸네일 이미지 생성
	      String str = null;
	      int i = board_file_server_name.indexOf(".");
	      String noExtension = board_file_server_name.substring(0, i);
	      String[] cmd = new String[] {
	            "C:\\instaclone\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\INSTAGRAM_CLONE_temp\\resources\\images\\feedupload\\ffmpeg",
	            "-i",
	            "C:\\instaclone\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\INSTAGRAM_CLONE_temp\\resources\\images\\feedupload\\"
	                  + board_file_server_name,
	            "-an", "-ss", "00:00:01", "-r", "1", "-vframes", "1", "-y",
	            "C:\\instaclone\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\INSTAGRAM_CLONE_temp\\resources\\images\\feedupload\\"
	                  + noExtension + ".jpg" };
	      Process process = null;
	      try {
	         if (board_file_ext.equals("mp4") || board_file_ext.equals("avi")) {
	            // 프로세스 빌더를 통하여 외부 프로그램 실행
	            process = new ProcessBuilder(cmd).start();
	            // 외부 프로그램의 표준출력 상태 버퍼에 저장
	            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            // 표준출력 상태를 출력
	            while ((str = stdOut.readLine()) != null) {
	               System.out.println(str);
	            }
	         }
	      } catch (IOException e) {
	         e.printStackTrace();
	      }

	      Map<String, Object> map = new HashMap<String, Object>();
	      if(res>0) {
	    	  map.put("board_code", board_code);
	      }
	      return map;
	   

	}
	
	@PostMapping(value = "/updateHashtag")
	   public Map<String, Object> updateHashtag(@RequestBody List<Map<String, Object>> listMap) {

	      int board_code = 0;	      
	      List<String> tags = new ArrayList<String>();
	      
	      for (Map<String, Object> map : listMap) {
	         if (map.get("tag") != null) {
	            tags.add((String) map.get("tag"));
	         } else if (map.get("board_code") != null) {
	            board_code = (Integer) map.get("board_code");
	         }
	      }
	      
	      System.out.println("updateHashtag - board_code : " + board_code + " tags : " + tags);
	      
	      int deleteRes = biz.deleteHashtag(board_code);

	      List<TagVo> tagList = new ArrayList<TagVo>();
	      for (String tag : tags) {
	      
	         TagVo tagVo = new TagVo();
	         tagVo.setBoard_code(board_code);
	         tagVo.setTag_hash(tag);
	         
	         tagList.add(tagVo);
	      }
	      
	      System.out.println(tagList);
	      
	      //그럼 여기서 biz로 보내는거 만들어야겟네
	      
	      int insertRes = biz.insertHashtag(tagList);
	      Map<String, Object> resMap = new HashMap<String, Object>();
	      resMap.put("res", insertRes);
	      return resMap;
	   }
	
	
	
	// 랜덤 피드 목록
	@RequestMapping(value = "/randomFeed")
	public ModelAndView randomFeed(Model model, HttpSession session) {
      logger.info("FEED/RANDOMFEED");
      int member_code = ((MemberVo)session.getAttribute("user")).getMember_code();
      List<FeedVo> list = biz.randomFeedList(member_code);
      model.addAttribute("feedList", list);
      return new ModelAndView("/feed/randomFeed");
   }

	@RequestMapping(value = "/likeFeed")
	   public ModelAndView likeFeed() {
	      return new ModelAndView("/feed/likeFeed");
	   }	
	
	
	@RequestMapping(value = "likeFeedList", method = RequestMethod.GET)
	   @ResponseBody
	   public String likeFeedList(int member_code, int startNo) {
	      List<FeedVo> list = biz.likeFeedList(startNo, member_code);
	      Gson gson = new Gson();
	      String feedList = gson.toJson(list);
	      System.out.println("$%$%$%@^#%##^#^왜안나와" + feedList);
	      return feedList;
	   }
	
	

}
