package com.instagram.clone.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagVo {
   
   private int tag_code;
   private int board_code;
   private String tag_map;
   private String tag_people;
   private String tag_hash;
}