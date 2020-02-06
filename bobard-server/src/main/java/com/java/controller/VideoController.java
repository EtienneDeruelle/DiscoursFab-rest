/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.controller;


import com.java.entity.Video;
import com.java.service.IVideoService;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.io.InputStream;
import javax.websocket.server.PathParam;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



/**
 *
 * @author Etienne
 */
@RestController
@RequestMapping("/public/videos")
@Setter
@Getter
public class VideoController {

    private final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private IVideoService videoService;

    @GetMapping(path = "/all")
    public List<Video> getVideo() throws IOException {
        return videoService.findAll();
    }

    @GetMapping(value = "/{idVideo}/blob", produces = "video/mp4")
    public @ResponseBody byte[] getByte(@PathParam("idVideo") Long idVideo) throws IOException {
        return videoService.findFichierVideoByIdVideo(idVideo);
    }
    
    @PostMapping(consumes = "multipart/form-data")
    public Video saveVideo(@RequestParam("fichier") MultipartFile fichier, @RequestParam("mot") String mot) throws IOException{
        final Video video = new Video();
        video.setFichier(fichier.getBytes());
        video.setMot(mot);
        return videoService.save(video);
    }

}
