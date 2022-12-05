package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gyeongbukSystem.management.mainSystem.dto.Broadlog;
import com.gyeongbukSystem.management.mainSystem.dto.BroadlogVO;
import com.gyeongbukSystem.management.mainSystem.util.DateUtil;

@Service
public class BroadlogServiceImpl implements BroadlogService{

	@Autowired
	private BroadlogDAO broadlogDAO;
	
	@Override
	public void insertBroadlog(BroadlogVO broadlogVO) throws Exception {
		// TODO Auto-generated method stub
		broadlogDAO.insertBroadlog(broadlogVO);
	}

	@Override
	public void updateBroadlog(BroadlogVO broadlogVO) throws Exception {
		// TODO Auto-generated method stub
		broadlogDAO.updateBroadlog(broadlogVO);
	}

	@Override
	public List<Broadlog> listBroadlog(BroadlogVO broadlogVO) throws Exception {
		// TODO Auto-generated method stub
		return broadlogDAO.listBroadlog(broadlogVO);
	}

	@Override
	public Broadlog selectBroadlog(BroadlogVO broadlogVO) throws Exception {
		// TODO Auto-generated method stub
		return broadlogDAO.selectBroadlog(broadlogVO);
	}

	@Override
	public int selectLastLineNum(BroadlogVO broadlogVO) throws Exception {
		// TODO Auto-generated method stub
		return broadlogDAO.selectLastLineNum(broadlogVO);
	}

	@Override
	public void deleteBroadlog(BroadlogVO broadlogVO) throws Exception {
		// TODO Auto-generated method stub
		broadlogDAO.deleteBroadlog(broadlogVO);
	}

	@Override
	public List<Broadlog> listBroadlogDevSttus() throws Exception {
		// TODO Auto-generated method stub
		return broadlogDAO.listBroadlogDevSttus();
	}

	@Override
	public int listBroadlogCnt(BroadlogVO broadlogVO) throws Exception {
		// TODO Auto-generated method stub
		return broadlogDAO.listBroadlogCnt(broadlogVO);
	}

}
