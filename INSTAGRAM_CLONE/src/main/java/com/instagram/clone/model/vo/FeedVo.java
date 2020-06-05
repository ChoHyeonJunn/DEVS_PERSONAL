package com.instagram.clone.model.vo;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class FeedVo {
   
   @NonNull
   private int board_code;
   
   private int member_code;
   private int channel_code;
   private String board_content;
   private String board_file_original_name;
   private String board_file_server_name;
   private String board_file_path;
   private Date board_regdate;
   private int board_like_count;
   private String board_file_ext;
   private String tag_hash;
   
   private MultipartFile mpfile;
   
   private int reply_code;
   private String reply_content;
   private int reply_like_count;
   private Date reply_regdate;
   
   private String member_id;
   private String member_img_server_name;
   
   private int rnum;
}