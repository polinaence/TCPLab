public class FileInfo {
    public final static int HASH_SIZE = 16;

    public FileInfo(String name, long size, byte[] hash) {
        this.name = name;
        this.size = size;
        this.hash = hash;
    }

    private final String name;
    // в байтах
    private final long size;

    private final byte[] hash;

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public byte[] getHash() {
        return hash;
    }
}