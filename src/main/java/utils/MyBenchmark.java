/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package utils;

import org.openjdk.jmh.annotations.Benchmark;

import java.io.*;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class MyBenchmark {

    private static final byte[] BYTE100;
    private static final byte[] BYTE200;
    private static final byte[] BYTE2KB;
    private static final byte[] BYTE5KB;
    private static final byte[] BYTE50KB;
    private static final byte[] BYTE500KB;
    static {
        try {
            BYTE100 = Files.readAllBytes(new File("src/main/java/famo/utils/testfile-100B.bin").toPath());
            BYTE200 = Files.readAllBytes(new File("src/main/java/famo/utils/testfile-200B.bin").toPath());
            BYTE2KB = Files.readAllBytes(new File("src/main/java/famo/utils/testfile-2kB.bin").toPath());
            BYTE5KB = Files.readAllBytes(new File("src/main/java/famo/utils/testfile-5kB.bin").toPath());
            BYTE50KB = Files.readAllBytes(new File("src/main/java/famo/utils/testfile-50kB.bin").toPath());
            BYTE500KB = Files.readAllBytes(new File("src/main/java/famo/utils/testfile-500kB.bin").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @Benchmark
    public void testCreateSha1Git() throws IOException {
        createSha1Git(new File("src/main/java/famo/utils/testfile-5kB.bin"));
    }

//    @Benchmark
    public void testCreateSha1Git2() throws IOException {
        createSha1Git2(new File("src/main/java/famo/utils/testfile-5kB.bin"));
    }

//    @Benchmark
    public void testCreateSha1Git3() throws IOException {
        createSha1Git3(new File("src/main/java/famo/utils/testfile-5kB.bin"));
    }

//    @Benchmark
    public void testCreateSha1Git_50k() throws IOException {
        createSha1Git(new File("src/main/java/famo/utils/testfile-50kB.bin"));
    }

//    @Benchmark
    public void testCreateSha1Git2_50k() throws IOException {
        createSha1Git2(new File("src/main/java/famo/utils/testfile-50kB.bin"));
    }

//    @Benchmark
    public void testCreateSha1Git3_50k() throws IOException {
        createSha1Git3(new File("src/main/java/famo/utils/testfile-50kB.bin"));
    }

//    @Benchmark
    public void testCreateSha1Git_500k() throws IOException {
        createSha1Git(new File("src/main/java/famo/utils/testfile-500kB.bin"));
    }

//    @Benchmark
    public void testCreateSha1Git2_500k() throws IOException {
        createSha1Git2(new File("src/main/java/famo/utils/testfile-500kB.bin"));
    }

//    @Benchmark
    public void testCreateSha1Git3_500k() throws IOException {
        createSha1Git3(new File("src/main/java/famo/utils/testfile-500kB.bin"));
    }

//    @Benchmark
    public int testCreateStringWithRefLength() {
        String s = new String("");
        return s.length();
    }

//    @Benchmark
    public String testCreateStringWithRef() {
        String s = new String("");
        return s;
    }

//    @Benchmark
    public String testCreateString() {
        return new String("");
    }

//    @Benchmark
    public void testReadFile() throws IOException {
        File file = new File("src/main/java/famo/utils/MyBenchmark.java");
        try (InputStream fis = new FileInputStream(file)) {
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
            }
        }
    }

//    @Benchmark
    public void testDigestByteArray() throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        try (InputStream fis = new ByteArrayInputStream(BYTE5KB)) {
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }
        }
    }
//    @Benchmark
    public void testDigestInputStream() throws NoSuchAlgorithmException, IOException {
        String algorithm = "SHA-1";
        byte[] bytes = BYTE5KB;
        hashBytes(algorithm, bytes);
    }

    private void hashBytes(String algorithm, byte[] bytes) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (InputStream fis = new ByteArrayInputStream(bytes)) {
            DigestInputStream digestInputStream = new DigestInputStream(fis, digest);
            byte[] buffer = new byte[8192];
            while (digestInputStream.read(buffer) > 0) ;
        }
    }

    //    @Benchmark
    public void testDigestByteArray50k() throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        try (InputStream fis = new ByteArrayInputStream(BYTE50KB)) {
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }
        }
    }
//    @Benchmark
    public void testDigestInputStream50k() throws NoSuchAlgorithmException, IOException {
        hashBytes("SHA-1", BYTE50KB);
    }

//    @Benchmark
    public void testDigestByteArray500k() throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        try (InputStream fis = new ByteArrayInputStream(BYTE500KB)) {
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }
        }
    }
//    @Benchmark
    public void testDigestInputStream500k() throws NoSuchAlgorithmException, IOException {
        hashBytes("SHA-1", BYTE500KB);
    }

    @Benchmark
    public void test_100B_Sha1() throws NoSuchAlgorithmException, IOException {
        hashBytes("SHA-1", BYTE100);
    }
    @Benchmark
    public void test_100B_Sha256() throws NoSuchAlgorithmException, IOException {
        hashBytes("SHA-256", BYTE100);
    }
    @Benchmark
    public void test_200B_Sha1() throws NoSuchAlgorithmException, IOException {
        hashBytes("SHA-1", BYTE200);
    }
    @Benchmark
    public void test_200B_Sha256() throws NoSuchAlgorithmException, IOException {
        hashBytes("SHA-256", BYTE200);
    }
    @Benchmark
    public void test_2000B_Sha1() throws NoSuchAlgorithmException, IOException {
        hashBytes("SHA-1", BYTE2KB);
    }
    @Benchmark
    public void test_2000B_Sha256() throws NoSuchAlgorithmException, IOException {
        hashBytes("SHA-256", BYTE2KB);
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     * @see https://stackoverflow.com/a/6293816/3629114
     * @see https://stackoverflow.com/a/552725/3629114
     * @see https://stackoverflow.com/a/19789797/3629114
     */
    public static byte[] createSha1Git(File file) throws IOException {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            return new byte[20];
        }
        digest.update(("blob " + file.length() + "\0").getBytes());
        try (InputStream fis = new FileInputStream(file)) {
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }
            return digest.digest();
        }
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     * @see https://gist.github.com/zeroleaf/6809843
     * @see https://stackoverflow.com/a/552725/3629114
     * @see https://stackoverflow.com/a/19789797/3629114
     */
    public static byte[] createSha1Git2(File file) throws IOException {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            return new byte[20];
        }
        try (InputStream fis = new FileInputStream(file)) {
            DigestInputStream digestInputStream = new DigestInputStream(fis, digest);
            digest.update(("blob " + file.length() + "\0").getBytes());
            byte[] buffer = new byte[8192];
            while (digestInputStream.read(buffer) > 0);
            return digest.digest();
        }
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     * @see https://gist.github.com/oyyq99999/359a73f68ae18a716b5d
     * @see https://stackoverflow.com/a/552725/3629114
     * @see https://stackoverflow.com/a/19789797/3629114
     */
    public static String createSha1Git3(File file) throws IOException {
        Sha1Hash sha1 = new Sha1Hash();
        sha1.update(("blob " + file.length() + "\0").getBytes());
        try (InputStream fis = new FileInputStream(file)) {
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    if (n < 8192) {
                        buffer = Arrays.copyOf(buffer, n);
                    }
                    sha1.update(buffer);
                }
            }
            return sha1.digest();
        }
    }

}
