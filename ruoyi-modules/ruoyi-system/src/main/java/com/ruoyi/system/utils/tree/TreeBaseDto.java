package com.ruoyi.system.utils.tree;

import java.util.List;

import com.ruoyi.system.domain.vo.SimpleUser;

import lombok.Data;

@Data
public class TreeBaseDto implements Comparable<TreeBaseDto> {
	/**
	 * 当前节点id
	 */
	private Long id;

	/**
	 * 父节点id
	 */
	private Long parentId;

	private String parentName;

	/**
	 * 名称
	 */
	private String title;

	private List<SimpleUser> users;

	private Integer sort;

	@Override
	public int compareTo(TreeBaseDto o) {
		// TODO Auto-generated method stub
		return this.sort - o.getSort();
	}

}
