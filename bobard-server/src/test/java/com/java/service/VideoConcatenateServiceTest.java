package com.java.service;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.java.entity.Video;
import com.java.service.impl.VideoConcatenateServiceImpl;
import java.awt.AWTException;
import java.io.IOException;


@RunWith(MockitoJUnitRunner.class)
public class VideoConcatenateServiceTest {

	@InjectMocks
	private VideoConcatenateServiceImpl videoConcatenateService;
	
	@Test
	public void itShouldConcatenateTwoVideo() throws IOException, InterruptedException, AWTException {
		videoConcatenateService.concateneTwoVideo(new Video(), new Video());
		fail();
	}
}
