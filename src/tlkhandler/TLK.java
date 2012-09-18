/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tlkhandler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 *
 * @author ovoloshchuk
 */
public class TLK {
    private String _filename;
    private Header _header;
    
    public TLK(String sFilename) {
        _filename = sFilename;
    }
    
    //Methods
    public static int getDword(byte[] data, int offset) {
        ByteBuffer buf = ByteBuffer.wrap(data, offset, 4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt();
    }
    public void load() throws IOException {        
        File f = new File(_filename);        
        try (RandomAccessFile raf = new RandomAccessFile(f, "r");) {
            byte[] headerBytes = new byte[Header.LENGTH];
            raf.read(headerBytes);
            _header = new Header(headerBytes);
            byte[] stringDataBytes = new byte[Data.LENGTH];
            while (raf.getFilePointer() < _header.getStringEntriesOffset()) {
                raf.read(stringDataBytes);
                Data data = new Data(stringDataBytes);
                long currentOffset = raf.getFilePointer();
                raf.seek(_header.getStringEntriesOffset() + data.getEntryOffset());
                byte[] entryBytes = new byte[data.getStringSize()];
                raf.read(entryBytes);
                raf.seek(currentOffset);
                String entryText = Charset.forName("Windows-1251").decode(ByteBuffer.wrap(entryBytes)).toString();
                System.out.println(String.format("String offset is %d, length is %d, data is %s.", 
                        data.getEntryOffset(), data.getStringSize(), entryText));
            }
        }
    }
    
    //Getters/Setters
    public String getFilename() {
        return _filename;
    }
    public Header getHeader() {
        return _header;
    }
    
    //classes
    public class Header {
        public final static int LENGTH = 20;
        public final static int OFFSET_LANGUAGE_ID = 8;
        public final static int OFFSET_STRING_COUNT = 12;
        public final static int OFFSET_STRING_ENTRIES_OFFSET = 16;
                
        private final byte[] _content;
        
        public Header(byte[] content) {
            _content = Arrays.copyOf(content, content.length);
        }
        
        public int getLanguageID() {
            return getDword(_content, OFFSET_LANGUAGE_ID);
        }
        public int getStringCount() {
            return getDword(_content, OFFSET_STRING_COUNT);
        }
        public int getStringEntriesOffset() {
            return getDword(_content, OFFSET_STRING_ENTRIES_OFFSET);
        }
    }
    public class Data {
        public final static int LENGTH = 40;
        private final static int OFFSET_STRING_OFFSET = 28;
        private final static int OFFSET_STRING_SIZE = 32;
        private final byte[] _content;
        
        public Data(byte[] content) {
            _content = Arrays.copyOf(content, content.length);
        }
        
        public int getEntryOffset() {
            return getDword(_content, OFFSET_STRING_OFFSET);
        }
        public int getStringSize() {
            return getDword(_content, OFFSET_STRING_SIZE);
        }
    }
}
