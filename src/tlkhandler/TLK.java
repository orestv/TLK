/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tlkhandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    public void load() throws IOException {        
        File f = new File(_filename);
        try (FileInputStream strm = new FileInputStream(f)) {
            byte[] headerBytes = new byte[Header.LENGTH_HEADER];
            strm.read(headerBytes);
            _header = new Header(headerBytes);
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
        public final static int LENGTH_HEADER = 20;
        public final static int OFFSET_LANGUAGE_ID = 8;
        public final static int OFFSET_STRING_COUNT = 12;
        public final static int OFFSET_STRING_DATA_OFFSET = 16;
                
        private byte[] _content;
        
        private int getLittleEndianDwordAt(int nOffset) {
            ByteBuffer buf = ByteBuffer.wrap(_content, nOffset, 4);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            return buf.getInt();
        }
        
        public Header(byte[] content) {
            _content = Arrays.copyOf(content, content.length);
        }
        
        public int getLanguageID() {
            return getLittleEndianDwordAt(OFFSET_LANGUAGE_ID);
        }
        public int getStringCount() {
            return getLittleEndianDwordAt(OFFSET_STRING_COUNT);
        }
        public int getStringDataOffset() {
            return getLittleEndianDwordAt(OFFSET_STRING_DATA_OFFSET);
        }
    }
}
