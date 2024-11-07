package com.my.resumeManager.epilogue.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.epilogue.model.dao.EpilogueMapper;

@Service
public class EpilogueServiceImpl implements EpilogueService{
	
	@Autowired
	private EpilogueMapper eMapper;
	
	
	
}
