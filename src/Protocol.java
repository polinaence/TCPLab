import java.io.*;

public class Protocol {

    public static FileInfo readFileInfo(InputStream in) throws IOException {
        DataInputStream inputStream = new DataInputStream(in);
        String fileName = inputStream.readUTF();
        long fileSize = inputStream.readLong();
        byte[] fileHash = new byte[FileInfo.HASH_SIZE];
        inputStream.readNBytes(fileHash, 0, FileInfo.HASH_SIZE);
        return new FileInfo(fileName, fileSize, fileHash);
    }

    public static void writeFileInfo(OutputStream out, FileInfo fileInfo) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(out);
        outputStream.writeUTF(fileInfo.getName());
        outputStream.writeLong(fileInfo.getSize());
        outputStream.write(fileInfo.getHash());
    }

    public static boolean readACK(InputStream in) throws IOException {
        return new DataInputStream(in).readBoolean();
    }

    public static void writeACK(OutputStream out, boolean ack) throws IOException {
        new DataOutputStream(out).writeBoolean(ack);
    }

}