package com.fivesysdev.Fiveogram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// TODO: 9/3/23 make sure if you need constructors in dtos and remove it if its not used anywhere
public class MarkDTO {
    public int height;
    public int width;
    public String username;
    public long photoId;
}
