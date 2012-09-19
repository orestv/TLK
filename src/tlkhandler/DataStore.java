/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tlkhandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author ovoloshchuk
 */
public class DataStore {
    private final ArrayList<Entry> _entries = new ArrayList<>();
    private final ArrayList<Entry> _entriesInternal = new ArrayList<>();
    private int _formalSize;
    private final static String separator = " / ";
    
    public void add(Entry entry) {
        _entriesInternal.add(entry);
        if (!entry.isEmpty())
            _entries.add(entry);
    }
    public void clear() {
        _entries.clear();
        _entriesInternal.clear();
        _formalSize = 0;
    }
    public Entry get(int index) {
        if (index >= _entries.size())
            return null;
        return _entries.get(index);
    }
    public Entry getInternal(int index) {
        if (index >= _entriesInternal.size())
            return null;
        return _entriesInternal.get(index);
    }
    public Entry getByRef(int strref) {
        for (Entry e : _entries)
            if (e.getStrref() == strref)
                return e;
        return null;
    }
    public int size() {
        return _entries.size();
    }
    public int formalSize() {
        return _formalSize;
    }
    public void setFormalSize(int formalSize) {
        _formalSize = formalSize;
    }

    public void save(File file) throws IOException {
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(file))) {
            for (Entry entry : _entriesInternal) {
                wr.write(entry.toString());
                wr.newLine();
            }
        }
    }
    public void load(File file) {
        clear();
    }

    public static class Entry {
        int _strref;
        String _original;
        String _translation;
        byte[] _headerBytes;

        public Entry(int strref, String original, String translation, byte[] headerBytes) {
            this._strref = strref;
            this._original = original;
            this._translation = translation;
            this._headerBytes = Arrays.copyOf(headerBytes, headerBytes.length);
        }
        public Entry (int strref, String original, byte[] headerBytes) {
            this(strref, original, original, headerBytes);
        }

        public int getStrref() {
            return _strref;
        }
        public void setStrref(int strref) {
            this._strref = strref;
        }
        public String getOriginal() {
            return _original;
        }
        public void setOriginal(String original) {
            this._original = original;
        }
        public String getTranslation() {
            return _translation;
        }
        public void setTranslation(String translation) {
            this._translation = translation;
        }
        public byte[] getHeaderBytes() {
            return _headerBytes;
        }
        public void setHeaderBytes(byte[] headerBytes) {
            this._headerBytes = headerBytes;
        }
        public boolean isEmpty() {
            return _original == null || _original.isEmpty();
        }

        public String toString() {
            return String.format("%d / %s / %s", _strref, DatatypeConverter.printBase64Binary(_headerBytes), _translation);
        }
    }
}
