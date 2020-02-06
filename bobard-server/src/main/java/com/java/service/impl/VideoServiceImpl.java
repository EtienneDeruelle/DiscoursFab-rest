/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.service.impl;

import com.java.dao.IVideoDao;
import com.java.dao.impl.VideoSaver;
import com.java.entity.Video;
import com.java.service.IVideoService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Etienne
 */
@Service
public class VideoServiceImpl implements IVideoService {

    byte[] userimage=null;
    
    @Autowired
    private VideoSaver videoSaver;
    
    @Autowired
    private IVideoDao videoDao;

    @Override
    public List<Video> findAll() {
        return (ArrayList<Video>) videoDao.findAll();
    }

    @Override
    public Video save(Video video) {
        return videoDao.save(video);
    }

    @Override
    public byte[] findFichierVideoByIdVideo(Long id) {
        return videoDao.findById(id).get().getFichier();
    }

}
