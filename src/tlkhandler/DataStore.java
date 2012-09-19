/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tlkhandler;

import java.util.ArrayList;

/**
 *
 * @author ovoloshchuk
 */
public class DataStore {
    private ArrayList<Entry> _entries = new ArrayList<>();
    
    public void add(Entry entry) {
        _entries.add(entry);
    }
    public Entry get(int index) {
        return _entries.get(index);
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
    
    public static class Entry {
        int _strref;
        String _original;
        String _translation;
        byte[] _headerBytes;

        public Entry(int strref, String original, String translation, byte[] headerBytes) {
            this._strref = strref;
            this._original = original;
            this._translation = translation;
            this._headerBytes = headerBytes;
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
        
        
    }
}
