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
import io.humble.video.Decoder;
import io.humble.video.Coder;

import io.humble.video.MediaDescriptor.Type;
import io.humble.video.Demuxer;
import io.humble.video.DemuxerFormat;
import io.humble.video.DemuxerStream;
import io.humble.video.Global;
import io.humble.video.KeyValueBag;
import io.humble.video.MediaPacket;
import io.humble.video.Muxer;
import io.humble.video.MuxerFormat;

@Service
public class VideoConcatenateServiceImpl implements IVideoConcatenateService {

	//args="-vf h264_mp4toannexb inputfile.mp4 output.m3u8"
	/*String input, String output, int hls_start,
	int hls_time, int hls_list_size, int hls_wrap, String hls_base_url,
    String vFilter,
    String aFilter*/
	
	@Override
	public Video concateneTwoVideo(final Video video1, final Video video2) {
		Video video3 = new Video();
		
		getInformationVideo();

		final Demuxer demuxer = Demuxer.make();
	    try {
			demuxer.open("/home/etienne/Vidéos/video2.mp4", null, false, true, null, null);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    final Muxer muxer = Muxer.make("/home/etienne/Vidéos/videoResult.m3u8", null, "hls");
	    /*muxer.setProperty("start_number", hls_start);
	    muxer.setProperty("hls_time", hls_time);
	    muxer.setProperty("hls_list_size", hls_list_size);
	    muxer.setProperty("hls_wrap", hls_wrap);
	    if (hls_base_url != null && hls_base_url.length() > 0)
	      muxer.setProperty("hls_base_url", hls_base_url);*/
	    
	    final MuxerFormat format = MuxerFormat.guessFormat("mp4", null, null);
	    
	    final BitStreamFilter vf = BitStreamFilter.make("-bsf:v h264_mp4toannexb"); //BitStreamFilter.make("-bsf:v h264_mp4toannexb");
	    final BitStreamFilter af = null; //BitStreamFilter.make("-bsf:v h264_mp4toannexb");
	    
	    int n = 0;
	    try {
			n = demuxer.getNumStreams();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    final Decoder[] decoders = new Decoder[n];
	    
	    for(int i = 0; i < n; i++) {
	        DemuxerStream ds = null;
			try {
				ds = demuxer.getStream(i);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        decoders[i] = ds.getDecoder();
	        final Decoder d = decoders[i];
	        
	        if (d != null) {
	          // neat; we can decode. Now let's see if this decoder can fit into the mp4 format.
	          if (!format.getSupportedCodecs().contains(d.getCodecID())) {
	            throw new RuntimeException("Input filename contains at least one stream with a codec not supported in the output format: " + d.toString());
	          }
	          if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER))
	            d.setFlag(Coder.Flag.FLAG_GLOBAL_HEADER, true);
	          d.open(null, null);
	          muxer.addNewStream(d);
	        }
	      }
	    
	    try {
			muxer.open(null, null);
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    final MediaPacket packet = MediaPacket.make();
	    try {
			while(demuxer.read(packet) >= 0) {
			  /**
			   * Now we have a packet, but we can only write packets that had decoders we knew what to do with.
			   */
			  final Decoder d = decoders[packet.getStreamIndex()];
			  if (packet.isComplete() && d != null) {
			    // check to see if we are using bit stream filters, and if so, filter the audio
			    // or video.
			    if (vf != null && d.getCodecType() == Type.MEDIA_VIDEO)
			      vf.filter(packet, null);
			    else if (af != null && d.getCodecType() == Type.MEDIA_AUDIO)
			      af.filter(packet, null);
			    muxer.write(packet, true);
			  }
			}
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    muxer.close();
	    try {
			demuxer.close();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	public void getInformationVideo() {
		Demuxer demuxer = Demuxer.make();
		try {
			demuxer.open("/home/etienne/Vidéos/video1.mp4", null, false, true, null, null);
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
		for (String key : metadata.getKeys())
			System.out.printf("  %s: %s\n", key, metadata.getValue(key));

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
				for (String key : metadata.getKeys())
					System.out.printf("    %s: %s\n", key, metadata.getValue(key));
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
