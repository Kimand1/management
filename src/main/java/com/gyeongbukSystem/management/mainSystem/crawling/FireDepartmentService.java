package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import com.gyeongbukSystem.management.mainSystem.dto.FireDepartment;
import com.gyeongbukSystem.management.mainSystem.dto.FireDepartmentVO;

public interface FireDepartmentService {
	public List<FireDepartment> listFireDepartment(FireDepartmentVO fireDepartmentVO);
	public FireDepartment selectFireDepartment(FireDepartmentVO fireDepartmentVO);	
}
