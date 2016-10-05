package com.sedmelluq.discord.lavaplayer.source.local;

import com.sedmelluq.discord.lavaplayer.container.matroska.MatroskaAudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioTrackExecutor;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.DelegatedAudioTrack;
import com.sedmelluq.discord.lavaplayer.container.mpeg.MpegAudioTrack;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity.COMMON;

/**
 * Audio track that handles processing local files as audio tracks.
 */
public class LocalAudioTrack extends DelegatedAudioTrack {
  private final File file;

  /**
   * @param executor Track executor
   * @param trackInfo Track info
   */
  public LocalAudioTrack(AudioTrackExecutor executor, AudioTrackInfo trackInfo) {
    super(executor, trackInfo);

    this.file = new File(executor.getIdentifier());
  }

  @Override
  public void process(AtomicInteger volumeLevel) throws Exception {
    if (executor.getIdentifier().endsWith(".mp4") || executor.getIdentifier().endsWith(".m4a")) {
      processDelegate(new MpegAudioTrack(executor, trackInfo, new LocalSeekableInputStream(file)), volumeLevel);
    } else if (executor.getIdentifier().endsWith(".webm") || executor.getIdentifier().endsWith(".mkv")) {
      processDelegate(new MatroskaAudioTrack(executor, trackInfo, new LocalSeekableInputStream(file)), volumeLevel);
    } else {
      throw new FriendlyException("Unknown file extension.", COMMON, null);
    }
  }
}