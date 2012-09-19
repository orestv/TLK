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
    //Methods
    public static int getDword(byte[] data, int offset) {
        ByteBuffer buf = ByteBuffer.wrap(data, offset, 4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt();
    }
    public static void putDword(byte[] data, int offset, int value) {
        ByteBuffer buf = ByteBuffer.wrap(data, offset, 4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(value);
    }
    public static void load(DataStore ds, String filename) throws IOException {
        File f = new File(filename);
        try (RandomAccessFile raf = new RandomAccessFile(f, "r");) {
            byte[] headerBytes = new byte[Header.LENGTH];
            raf.read(headerBytes);
            Header header = new Header(headerBytes);
            ds.setFormalSize(header.getStringCount());
            byte[] entryHeaderBytes = new byte[Entry.LENGTH];
            int strref = 0;
            while (raf.getFilePointer() < header.getStringEntriesOffset()) {
                try {
                    raf.read(entryHeaderBytes);
                    Entry entry = new Entry(entryHeaderBytes);
                    DataStore.Entry e;
                    if (entry.getStringSize() > 0) {
                        int currentOffset = (int)raf.getFilePointer();
                        raf.seek(header.getStringEntriesOffset() + entry.getEntryOffset());
                        byte[] entryDataBytes = new byte[entry.getStringSize()];
                        raf.read(entryDataBytes);
                        raf.seek(currentOffset);
                        String entryText = Charset.forName("Windows-1251").decode(ByteBuffer.wrap(entryDataBytes)).toString();
                        e = new DataStore.Entry(strref, entryText, entryHeaderBytes);
                    }
                    else {
                        e = new DataStore.Entry(strref, null, entryHeaderBytes);
                    }
                    if (e == null) {
                        System.out.println("e == null");
                    }
                    ds.add(e);
                    strref++;
                }
                catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
    }
    public static void save(DataStore ds, String filename) throws IOException {
        int dataOffset = ds.formalSize() * Entry.LENGTH + Header.LENGTH;
        Header header = new Header(0, ds.formalSize(), dataOffset);
        Charset charset = Charset.forName("Windows-1251");
        File f = new File(filename);
        try (RandomAccessFile raf = new RandomAccessFile(f, "rw")) {
            raf.write(header.getBytes());
            int currentStringOffset = 0;            
            for (int i = 0; i < ds.formalSize(); i++) {
                DataStore.Entry entry = ds.getInternal(i);
                Entry tlkEntry = new Entry(entry.getHeaderBytes(), entry.isEmpty() ? 0 : currentStringOffset, entry.getTranslation(), charset);
                raf.write(tlkEntry.getBytes());
                if (!entry.isEmpty()) {
                    int currentHeaderEntryOffset = (int)raf.getFilePointer();
                    raf.seek(dataOffset + currentStringOffset);
                    raf.write(tlkEntry.getDataBytes(charset));
                    currentStringOffset = (int)raf.getFilePointer() - dataOffset;
                    raf.seek(currentHeaderEntryOffset);
                }
            }
        }
    }
    //classes
    public static class Header {
        public final static int LENGTH = 20;
        public final static int OFFSET_LANGUAGE_ID = 8;
        public final static int OFFSET_STRING_COUNT = 12;
        public final static int OFFSET_STRING_ENTRIES_OFFSET = 16;
        public final static byte[] TLK_MARKER_BYTES = new byte[] {
            84, 76, 75, 32, 86, 51, 46, 48
        };

        private final byte[] _content;
        
        public Header(byte[] content) {
            _content = Arrays.copyOf(content, content.length);
        }
        public Header(int languageID, int entriesCount, int entriesOffset) {
            _content = new byte[LENGTH];
            ByteBuffer bb = ByteBuffer.wrap(_content);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put(TLK_MARKER_BYTES);

            bb.position(OFFSET_LANGUAGE_ID);
            bb.putInt(languageID);

            bb.position(OFFSET_STRING_COUNT);
            bb.putInt(entriesCount);

            bb.position(OFFSET_STRING_ENTRIES_OFFSET);
            bb.putInt(entriesOffset);
        }

        public byte[] getBytes() {
            return _content;
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
        private final byte[] _header;
        private String _value = "";
        private int _strref;
        public final static byte[] ZERO_ENTRY_BYTES = new byte[LENGTH];

        public Entry(byte[] header) {
            _header = Arrays.copyOf(header, header.length);
        }
        public Entry(byte[] header, int entryOffset, String value, Charset charset) {
            _header = Arrays.copyOf(header, header.length);
            _value = value;
            putDword(_header, OFFSET_STRING_OFFSET, entryOffset);
            if (value != null)
                putDword(_header, OFFSET_STRING_SIZE, value.getBytes(charset).length);
            else
                Arrays.fill(_header, OFFSET_STRING_SIZE, OFFSET_STRING_SIZE + 4, (byte)0);
        }

        public byte[] getBytes() {
            return _header;
        }
        public byte[] getDataBytes(Charset charset) {
            return _value.getBytes(charset);
        }
        public int getEntryOffset() {
            return getDword(_header, OFFSET_STRING_OFFSET);
        }
        public int getStringSize() {
            return getDword(_header, OFFSET_STRING_SIZE);
        }
        public String getValue() {
            return _value;
        }
        public void setValue(String value) {
            this._value = value;
        }
        public int getStrRef() {
            return _strref;
        }
        public void setStrRef(int strref) {
            _strref = strref;
        }

    }
}
