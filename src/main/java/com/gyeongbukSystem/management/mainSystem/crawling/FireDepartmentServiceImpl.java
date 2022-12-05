package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gyeongbukSystem.management.mainSystem.dto.FireDepartment;
import com.gyeongbukSystem.management.mainSystem.dto.FireDepartmentVO;

@Service
public class FireDepartmentServiceImpl implements FireDepartmentService{

	@Autowired
	private FireDepartmentDAO fireDepartmentDAO;
	
	@Override
	public List<FireDepartment> listFireDepartment(FireDepartmentVO fireDepartmentVO) {
		// TODO Auto-generated method stub
		return fireDepartmentDAO.listFireDepartment(fireDepartmentVO);	
	}

	@Override
	public FireDepartment selectFireDepartment(FireDepartmentVO fireDepartmentVO) {
		// TODO Auto-generated method stub
		return fireDepartmentDAO.selectFireDepartment(fireDepartmentVO);
	}
	
}
