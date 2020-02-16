package com.java.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BitField;
import org.springframework.stereotype.Service;

import com.java.entity.Video;
import com.java.service.IVideoConcatenateService;

import io.humble.video.BitStreamFilter;
import io.humble.video.BitStreamFilterType;
import io.humble.video.Codec;
import io.humble.video.Decoder;

import io.humble.video.MediaDescriptor.Type;
import io.humble.video.Demuxer;
import io.humble.video.DemuxerFormat;
import io.humble.video.DemuxerStream;
import io.humble.video.Encoder;
import io.humble.video.Global;
import io.humble.video.KeyValueBag;
import io.humble.video.MediaAudio;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.MediaSampled;
import io.humble.video.Muxer;
import io.humble.video.MuxerFormat;
import io.humble.video.Rational;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VideoConcatenateServiceImpl implements IVideoConcatenateService {

    //args="-vf h264_mp4toannexb inputfile.mp4 output.m3u8"
    /*String input, String output, int hls_start,
	int hls_time, int hls_list_size, int hls_wrap, String hls_base_url,
    String vFilter,
    String aFilter*/
    /**
     *
     * @param video1
     * @param video2
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws java.awt.AWTException
     */
    @Override
    public Video concateneTwoVideo(final Video video1, final Video video2) throws IOException, InterruptedException, AWTException {
        Video video3 = new Video();

        getInformationVideo();

        final String pathfile = new File("C:\\Users\\Etienne\\Vidéos\\video10.mp4").getPath();
        final MuxerFormat format = MuxerFormat.guessFormat("mp4", null, null);
        final Muxer muxer = Muxer.make("video10.mp4", format, null);
        final Codec codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());
        //Encoder encoder = Encoder.make(codec);
        //encoder.open(null, null);

        //encoder.setWidth(screenbounds.width);
        //encoder.setHeight(screenbounds.height);
        // We are going to use 420P as the format because that's what most video formats these days use
        /*final PixelFormat.Type pixelformat = PixelFormat.Type.PIX_FMT_YUV420P;
        encoder.setPixelFormat(pixelformat);
        encoder.setTimeBase(framerate);

        if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER)) {
            encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);
        }

        encoder.open(null, null);
        muxer.addNewStream(encoder);
        muxer.open(null, null);*/

 /*MediaPictureConverter converter = null;
        final MediaPicture picture = MediaPicture.make(
                encoder.getWidth(),
                encoder.getHeight(),
                pixelformat);
        picture.setTimeBase(framerate);*/
        final Demuxer demuxer = Demuxer.make();
        demuxer.open("C:/Users/Etienne/Videos/video1.mp4", null, false, true, null, null);
        //final Codec codec2 = Codec.findDecodingCodec(Codec.findDecodingCodecByName("mp4").getID());
        //Decoder decoder = Decoder.make(codec2);

        MediaAudio audio = null;
        MediaPicture picture = null;
        final MediaPacket packet = MediaPacket.make();
        final MediaPacket packet2 = MediaPacket.make();
        for (int i = 0; i < demuxer.getNumStreams(); i++) {
            DemuxerStream demuxerStream = demuxer.getStream(i);
            Decoder decoder = demuxerStream.getDecoder();
            Encoder encoder = Encoder.make(codec);
            if (decoder != null && decoder.getCodecType() == Type.MEDIA_VIDEO) {
                encoder.setPixelFormat(decoder.getPixelFormat());
                encoder.setTimeBase(decoder.getTimeBase());
                encoder.setWidth(decoder.getWidth());
                encoder.setHeight(decoder.getHeight());
                encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);
            } else if (decoder != null && decoder.getCodecType() == Type.MEDIA_AUDIO) {
                encoder.setSampleFormat(decoder.getSampleFormat());
            }
            encoder.open(null, null);
            muxer.addNewStream(encoder);
            muxer.open(null, null);

            while (demuxer.read(packet) >= 0) {
                int offset = 0;
                int bytesRead = 0;
                do {
                    if (decoder != null && decoder.getCodecType() == Type.MEDIA_VIDEO) {
                        picture = MediaPicture.make(decoder.getWidth(), decoder.getHeight(), decoder.getPixelFormat());
                        decoder.open(null, null);
                        bytesRead = decoder.decodeVideo(picture, packet, offset);

                        //encoder.encode(packet, picture);
                    } else if (decoder != null && decoder.getCodecType() == Type.MEDIA_AUDIO) {
                        audio = MediaAudio.make(decoder.getNumProperties(), decoder.getSampleRate(), decoder.getChannels(), decoder.getChannelLayout(), decoder.getSampleFormat());
                        decoder.open(null, null);
                        bytesRead = decoder.decodeAudio(audio, packet, offset);
                        //encoder.encode(packet, audio);
                    }

                    //muxer.write(packet, true);
                    offset += bytesRead;
                } while (offset < packet.getSize());

                if ((picture != null && picture.isComplete()) || (audio != null && audio.isComplete())) {
                    do {
                        if (decoder != null && decoder.getCodecType() == Type.MEDIA_VIDEO) {
                            encoder.encode(packet2, picture);
                        } else if (decoder != null && decoder.getCodecType() == Type.MEDIA_AUDIO) {
                            encoder.encode(packet2, audio);
                        }
                        if (packet2.isComplete()) {
                            muxer.write(packet2, true);
                        }
                    } while (packet2.isComplete());
                }
            }
            /*
            if (decoder != null && decoder.getCodecType() == Type.MEDIA_VIDEO) {
                encoder.encode(packet, picture);
            } else if (decoder != null && decoder.getCodecType() == Type.MEDIA_AUDIO) {
                encoder.encode(packet, audio);
            }
            muxer.write(packet, true);*/

        }

        /*
        
        int offset = 0;
                int bytesRead = 0;
                do {
                    if (decoder != null && decoder.getCodecType() == Type.MEDIA_VIDEO) {
                        picture = MediaPicture.make(decoder.getWidth(), decoder.getHeight(), decoder.getPixelFormat());
                        decoder.open(null, null);
                        bytesRead = decoder.decodeVideo(picture, packet, offset);
                        //encoder.encode(packet, picture);
                    } else if (decoder != null && decoder.getCodecType() == Type.MEDIA_AUDIO) {
                        audio = MediaAudio.make(decoder.getNumProperties(), decoder.getSampleRate(), decoder.getChannels(), decoder.getChannelLayout(), decoder.getSampleFormat());
                        decoder.open(null, null);
                        bytesRead = decoder.decodeAudio(audio, packet, offset);
                        //encoder.encode(packet, audio);
                    }
                    picture.setComplete(true);
                    picture.setTimeStamp(System.currentTimeMillis());
                    //muxer.write(packet, true);
                    offset += bytesRead;
                } while (((audio != null && !audio.isComplete()) || picture != null) 
                        && ((picture != null && !picture.isComplete()) || audio != null));

                if (decoder != null && decoder.getCodecType() == Type.MEDIA_VIDEO) {
                    encoder.encode(packet, picture);
                } else if (decoder != null && decoder.getCodecType() == Type.MEDIA_AUDIO) {
                    encoder.encode(packet, audio);
                }
                muxer.write(packet, true);
        
         */
        //// PARTIE DELICATE debut
        //final MediaPacket packet = MediaPacket.make();
        /*
        for (int i = 0; i < 10 / framerate.getDouble(); i++) {

            final BufferedImage screen = convertToType(robot.createScreenCapture(screenbounds), BufferedImage.TYPE_3BYTE_BGR);

            if (converter == null) {
                converter = MediaPictureConverterFactory.createConverter(screen, picture);
            }
            converter.toPicture(picture, screen, i);

            do {
                encoder.encode(packet, picture);
                if (packet.isComplete()) {
                    muxer.write(packet, false);
                }
            } while (packet.isComplete());

            Thread.sleep((long) (1000 * framerate.getDouble()));
        }*/
        /**
         * Encoders, like decoders, sometimes cache pictures so it can do the
         * right key-frame optimizations. So, they need to be flushed as well.
         * As with the decoders, the convention is to pass in a null input until
         * the output is not complete.
         */
        muxer.close();
        demuxer.close();

        return video3;
    }

    private static String formatTimeStamp(long duration) {
        if (duration == Global.NO_PTS) {
            return "00:00:00.00";
        }
        double d = 1.0 * duration / Global.DEFAULT_PTS_PER_SECOND;
        int hours = (int) (d / (60 * 60));
        int mins = (int) ((d - hours * 60 * 60) / 60);
        int secs = (int) (d - hours * 60 * 60 - mins * 60);
        int subsecs = (int) ((d - (hours * 60 * 60.0 + mins * 60.0 + secs)) * 100.0);
        return String.format("%1$02d:%2$02d:%3$02d.%4$02d", hours, mins, secs, subsecs);
    }

    public static BufferedImage convertToType(BufferedImage sourceImage,
            int targetType) {
        BufferedImage image;

        // if the source image is already the target type, return the source image
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        } // otherwise create a new image of the target type and draw the new
        // image
        else {
            image = new BufferedImage(sourceImage.getWidth(),
                    sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }

        return image;
    }

    public void getInformationVideo() {
        Demuxer demuxer = Demuxer.make();
        try {
            demuxer.open("C:\\Users\\Etienne\\Videos\\video1.mp4", null, false, true, null, null);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final DemuxerFormat format = demuxer.getFormat();
        System.out.printf("URL: '%s' (%s: %s)\n", demuxer.getURL(), format.getLongName(), format.getName());

        KeyValueBag metadata = demuxer.getMetaData();
        System.out.println("MetaData:");
        for (String key : metadata.getKeys()) {
            System.out.printf("  %s: %s\n", key, metadata.getValue(key));
        }

        final String formattedDuration = formatTimeStamp(demuxer.getDuration());
        System.out.printf("Duration: %s, start: %f, bitrate: %d kb/s\n", formattedDuration,
                demuxer.getStartTime() == Global.NO_PTS ? 0 : demuxer.getStartTime() / 1000000.0,
                demuxer.getBitRate() / 1000);

        int ns;
        try {
            ns = demuxer.getNumStreams();

            // Now, let's iterate through each of them.
            for (int i = 0; i < ns; i++) {
                DemuxerStream stream = demuxer.getStream(i);

                metadata = stream.getMetaData();
                // Language is usually embedded as metadata in a stream.
                final String language = metadata.getValue("language");

                // We will only be able to make a decoder for streams we can actually
                // decode, so the caller should check for null.
                Decoder d = stream.getDecoder();

                System.out.printf(" Stream #0.%1$d (%2$s): %3$s\n", i, language,
                        d != null ? d.toString() : "unknown coder");
                System.out.println("  Metadata:");
                for (String key : metadata.getKeys()) {
                    System.out.printf("    %s: %s\n", key, metadata.getValue(key));
                }
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
