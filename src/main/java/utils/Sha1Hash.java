package utils;

import java.util.Arrays;

import static java.lang.String.format;

public class Sha1Hash {

    private static final class Sha1KGenerator {

        static int getK(int x) {
            return (int) ((long) (Math.sqrt(x) / 4L * Math.pow(2, 32)));
        }
    }

    private static final int k[]            = {Sha1KGenerator.getK(2), Sha1KGenerator.getK(3),
            Sha1KGenerator.getK(5), Sha1KGenerator.getK(10)};
    private int              h0             = 0x67452301;
    private int              h1             = 0xefcdab89;
    private int              h2             = 0x98badcfe;
    private int              h3             = 0x10325476;
    private int              h4             = 0xc3d2e1f0;
    private long             bytesProcessed = 0L;
    private int              offset         = 0;
    private byte[]           remaining      = new byte[64];

    public static void main(String[] args) {
        Sha1Hash sha1 = new Sha1Hash();
        sha1.update("The quick brown fox jumps over the lazy cog".getBytes());
        System.out.println(sha1.digest());
    }

    public void reset() {
        h0 = 0x67452301;
        h1 = 0xefcdab89;
        h2 = 0x98badcfe;
        h3 = 0x10325476;
        h4 = 0xc3d2e1f0;
        offset = 0;
        bytesProcessed = 0;
    }

    private int f(int t, int b, int c, int d) {
        switch (t) {
            case 0:
                return (b & c) | ((~b) & d);
            case 2:
                return (b & c) | (b & d) | (c & d);
            case 1:
            case 3:
            default:
                return b ^ c ^ d;
        }
    }

    public void update(byte[] data) {
        int off = 0;
        byte[] byteBuf = new byte[64];
        int[] w = new int[80];
        while (data.length >= off + 64 - offset) {
            System.arraycopy(remaining, 0, byteBuf, 0, offset);
            System.arraycopy(data, off, byteBuf, offset, 64 - offset);

            off += 64 - offset;
            offset = 0;

            for (int i = 0; i < 16; i++) {
                int j = i << 2;
                w[i] = (byteBuf[j + 3] & 0xff) | ((byteBuf[j + 2] & 0xff) << 8)
                        | ((byteBuf[j + 1] & 0xff) << 16) | ((byteBuf[j] & 0xff) << 24);
            }

            for (int i = 16; i < 80; i++) {
                w[i] = rol(w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16], 1);
            }

            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;

            for (int i = 0; i < 80; i++) {
                int temp = rol(a, 5) + f(i / 20, b, c, d) + e + w[i] + k[i / 20];
                e = d;
                d = c;
                c = rol(b, 30);
                b = a;
                a = temp;
            }

            h0 += a;
            h1 += b;
            h2 += c;
            h3 += d;
            h4 += e;
        }
        if (data.length > off) {
            System.arraycopy(data, off, remaining, offset, data.length - off);
            offset += data.length - off;
        }
        bytesProcessed += data.length;
    }

    private static int rol(int x, int n) {
        return (x << n) | (x >>> (32 - n));
    }

    private void padding() {
        int padLen = (int) (64 - bytesProcessed & 0x3f);
        if (padLen <= 8) {
            padLen += 64;
        }
        byte[] pad = new byte[padLen];
        Arrays.fill(pad, (byte) 0);
        pad[0] = (byte) 0x80;
        long bitLength = bytesProcessed << 3;
        int i = padLen - 8;
        pad[i++] = (byte) ((bitLength >>> 56) & 0xff);
        pad[i++] = (byte) ((bitLength >>> 48) & 0xff);
        pad[i++] = (byte) ((bitLength >>> 40) & 0xff);
        pad[i++] = (byte) ((bitLength >>> 32) & 0xff);
        pad[i++] = (byte) ((bitLength >>> 24) & 0xff);
        pad[i++] = (byte) ((bitLength >>> 16) & 0xff);
        pad[i++] = (byte) ((bitLength >>> 8) & 0xff);
        pad[i++] = (byte) (bitLength & 0xff);
        update(pad);
    }

    private static String toHexString(int x) {
        return format("%08x", x);
    }

    public String digest() {
        padding();
        return toHexString(h0) + toHexString(h1) + toHexString(h2) + toHexString(h3)
                + toHexString(h4);
    }
}
