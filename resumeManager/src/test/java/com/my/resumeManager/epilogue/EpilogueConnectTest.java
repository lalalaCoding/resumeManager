package com.my.resumeManager.epilogue;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.resumeManager.epilogue.model.dao.EpilogueMapper;

@SpringBootTest
public class EpilogueConnectTest {
	
	@Autowired
	private EpilogueMapper eMapper;
	
	@Test
	public void dbConnectTest() {
		Integer count = eMapper.connectTest();
		Assertions.assertThat(count).isNotNull();
	}
	
	
	
	
}
