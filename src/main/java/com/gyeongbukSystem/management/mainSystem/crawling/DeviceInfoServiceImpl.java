package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfo;
import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfoVO;

@Service
public class DeviceInfoServiceImpl implements DeviceInfoService{

	@Autowired
	private DeviceInfoDAO deviceInfoDAO;
	
	@Override
	public List<DeviceInfo> listDeviceInfo(DeviceInfoVO deviceInfoVO) {
		// TODO Auto-generated method stub
		return deviceInfoDAO.listDeviceInfo(deviceInfoVO);
	}

	@Override
	public DeviceInfo selectDeviceInfo(DeviceInfoVO deviceInfoVO) {
		// TODO Auto-generated method stub
		return deviceInfoDAO.selectDeviceInfo(deviceInfoVO);
	}

	
}
