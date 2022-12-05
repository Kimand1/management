package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gyeongbukSystem.management.mainSystem.dto.FireDepartment;
import com.gyeongbukSystem.management.mainSystem.dto.FireDepartmentVO;


@Mapper
public interface FireDepartmentDAO {
	public List<FireDepartment> listFireDepartment(FireDepartmentVO fireDepartmentVO);
	public FireDepartment selectFireDepartment(FireDepartmentVO fireDepartmentVO);	
}
