package com.java.service;

import com.java.entity.Video;
import java.awt.AWTException;
import java.io.IOException;

public interface IVideoConcatenateService {
	
	Video concateneTwoVideo(Video video1, Video video2)throws IOException, InterruptedException, AWTException;

}
