/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.service;

import com.java.entity.Video;
import java.util.List;

/**
 *
 * @author Etienne
 */
public interface IVideoService {
    
    public List<Video> findAll();
    
    public byte[] findFichierVideoByIdVideo(Long id);
    
    public Video save(Video video);
    
}
