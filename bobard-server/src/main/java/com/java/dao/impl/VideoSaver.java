/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.dao.impl;

import com.java.dao.IVideoDao;
import com.java.entity.Video;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import javax.persistence.EntityManager;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Etienne
 */
@Repository
public class VideoSaver {

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private IVideoDao videoDao;

    public void saveVideo(byte[] videoByte) throws FileNotFoundException, IOException {
        
        File video = new File("C:\\Users\\Etienne\\Videos\\fichier_court.mp4");
        FileInputStream fis = new FileInputStream(video);
        
        //byte[] file = new byte[1024];
        //IOUtils.readFully(fis, file);
        byte[] file = Files.readAllBytes(video.toPath());
        Video videoa = new Video();
        videoa.setMot("chocolat");
        videoa.setFichier(file);
        videoDao.save(videoa);
        
        
    }

}
