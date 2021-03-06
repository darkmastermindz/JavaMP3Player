package dev.hanselwei.Sound;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MP3Player {

    /**
     * MP3Player decodes MP3 and plays it
     * @param filePath MP3 File String
     * @throws IOException File exception
     * @throws UnsupportedAudioFileException Unsupported audio exception
     */
    public MP3Player(String filePath) throws IOException, UnsupportedAudioFileException {
        try {
            File file = new File(filePath);
            AudioFileFormat baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
            Map properties = baseFileFormat.properties();
            Long duration = (Long) properties.get("duration");

            System.out.println("** Running dev.hanselwei.Sound : MP3Player : 1.0.0.SNAPSHOT **");

            // sourceStream – the stream to be converted
            AudioInputStream sourceStream = AudioSystem.getAudioInputStream(file);
            AudioInputStream convertedInput = null;

            AudioFormat baseFormat = sourceStream.getFormat();

            // targetFormat – the desired audio format after conversion
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);


            convertedInput = AudioSystem.getAudioInputStream(targetFormat, sourceStream);

            // Play now
            System.out.println("\n** Now Playing... " + filePath + " **\n");
            rawPlay(targetFormat, convertedInput);
            sourceStream.close();
        } catch (Exception e)
        {
            //Handle exception.
            System.err.println(String.format("Error {}", e));

        }

        // if this makes it into production, please laugh
    }

    /**
     * RawPlay plays the decoded audio
     * @param targetFormat targetFormat
     * @param convertedInput convertedInput
     * @throws IOException File exception
     * @throws LineUnavailableException Line not available
     */
    private void rawPlay(AudioFormat targetFormat, AudioInputStream convertedInput) throws IOException, LineUnavailableException
    {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        if (line != null)
        {
            // Start
            line.start();
            int nBytesRead = 0, nBytesWritten = 0;
            while (nBytesRead != -1)
            {
                nBytesRead = convertedInput.read(data, 0, data.length);
                if (nBytesRead != -1) nBytesWritten = line.write(data,0, nBytesRead);
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            convertedInput.close();
        }
    }

    /**
     * Gets a SourceDataLine from audio format
     * A source data line is a data line to which data may be written. It acts as a source to its mixer. An application writes audio bytes to a source data line, which handles the buffering of the bytes and delivers them to the mixer. The mixer may mix the samples with those from other sources and then deliver the mix to a target such as an output port (which may represent an audio output device on a sound card).
     * Note that the naming convention for this interface reflects the relationship between the line and its mixer. From the perspective of an application, a source data line may act as a target for audio data.
     * @param audioFormat
     * @return SourceDataLine
     * @throws LineUnavailableException
     */
    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
    {
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(audioFormat);
        return line;
    }

}