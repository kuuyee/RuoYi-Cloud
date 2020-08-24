package com.ruoyi.system.utils.tree;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TreeDto extends TreeBaseDto {

	private List<TreeDto> children = new ArrayList<TreeDto>();

	
}
