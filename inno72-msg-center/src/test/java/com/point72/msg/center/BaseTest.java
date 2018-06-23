package com.inno72.msg.center;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=MsgCenterApplication.class)
@ActiveProfiles(profiles={"dev"})
public class BaseTest {

	public BaseTest() {
		super();
	}

}
