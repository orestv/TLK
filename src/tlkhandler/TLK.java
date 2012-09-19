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
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author ovoloshchuk
 */
public class TLK {        
    //Methods
    public static int getDword(byte[] data, int offset) {
        ByteBuffer buf = ByteBuffer.wrap(data, offset, 4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt();
    }
    public static void load(DataStore ds, String filename) throws IOException {        
        File f = new File(filename);        
        try (RandomAccessFile raf = new RandomAccessFile(f, "r");) {
            byte[] headerBytes = new byte[Header.LENGTH];
            raf.read(headerBytes);
            Header header = new Header(headerBytes);
            byte[] stringDataBytes = new byte[Entry.LENGTH];
            int strref = 0;
            while (raf.getFilePointer() < header.getStringEntriesOffset()) {
                raf.read(stringDataBytes);
                Entry entry = new Entry(stringDataBytes);
                if (entry.getStringSize() > 0) {
                    long currentOffset = raf.getFilePointer();
                    raf.seek(header.getStringEntriesOffset() + entry.getEntryOffset());
                    byte[] entryBytes = new byte[entry.getStringSize()];
                    raf.read(entryBytes);
                    raf.seek(currentOffset);
                    String entryText = Charset.forName("Windows-1251").decode(ByteBuffer.wrap(entryBytes)).toString();
                    DataStore.Entry e = new DataStore.Entry(strref, entryText, entryBytes);
                    ds.add(e);
                }
                else {
                    System.out.println(String.format("Empty string found at strref %d", strref));
                }
                strref++;
            }
        }
    }
    //classes
    public static class Header {
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
    public static class Entry {
        public final static int LENGTH = 40;
        private final static int OFFSET_STRING_OFFSET = 28;
        private final static int OFFSET_STRING_SIZE = 32;
        private final byte[] _content;
        private String _value = "";
        private String _translation = "";
        private int _strref;
        
        public Entry(byte[] content) {
            _content = Arrays.copyOf(content, content.length);
        }
        
        public int getEntryOffset() {
            return getDword(_content, OFFSET_STRING_OFFSET);
        }
        public int getStringSize() {
            return getDword(_content, OFFSET_STRING_SIZE);
        }

        public String getValue() {
            return _value;
        }

        public void setValue(String value) {
            this._value = value;
        }

        public String getTranslation() {
            return _translation;
        }

        public void setTranslation(String translation) {
            this._translation = translation;
        }
        public int getStrRef() {
            return _strref;
        }
        public void setStrRef(int strref) {
            _strref = strref;
        }
        
    }
}
