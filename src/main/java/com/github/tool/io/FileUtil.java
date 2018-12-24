package com.github.tool.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

/**
 * <p></p>
 *
 * @author PengCheng
 * @date 2018/12/24
 */
public class FileUtil {

    public static void writeLines(File file, String encoding, Collection lines, String lineEnding) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            IOUtil.writeLines(lines, lineEnding, out, encoding);
        } finally {
            IOUtil.close(out);
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && parent.exists() == false) {
                if (parent.mkdirs() == false) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }

    public static void copy(String source, String target) throws IOException {
        FileChannel input= null;
        FileChannel output=null;
        try {
            input = FileChannel.open(Paths.get(source), StandardOpenOption.READ);
            output = FileChannel.open(Paths.get(target),StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.READ);
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            while(input.read(buffer)!=-1){
                buffer.flip();
                output.write(buffer);
                buffer.clear();
            }
        } finally {
            IOUtil.close(output);
            IOUtil.close(input);
        }
    }

}
