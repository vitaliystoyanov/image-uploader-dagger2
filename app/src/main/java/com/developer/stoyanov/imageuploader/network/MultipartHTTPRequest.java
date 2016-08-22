package com.developer.stoyanov.imageuploader.network;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.System.currentTimeMillis;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.URLConnection.guessContentTypeFromName;

public class MultipartHTTPRequest {
    private static final String TAG = "MultipartHTTPRequest";

    private static final String CRLF = "\r\n";
    private static final String CHARSET = "UTF-8";

    private HttpURLConnection connection = null;
    private OutputStream outputStream = null;
    private PrintWriter writer = null;
    private final String boundary;

    public MultipartHTTPRequest(final URL url) {
        boundary = "---------------------------" + currentTimeMillis();
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Charset", CHARSET);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);
        } catch (Exception e) {
            Log.e(TAG, "MultipartHTTPRequest: ", e);
        }
    }

    public void addFormField(final String name, final String value) {
        if (writer == null) return;
        writer.append("--").append(boundary).append(CRLF)
                .append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(CRLF)
                .append("Content-Type: text/plain; charset=").append(CHARSET)
                .append(CRLF).append(CRLF).append(value).append(CRLF);
    }

    public void addFilePart(final String fieldName, final InputStream uploadFile)
            throws IOException {
        if (writer == null) return;
        final String fileName = "image";
        writer.append("--").append(boundary).append(CRLF)
                .append("Content-Disposition: form-data; name=\"")
                .append(fieldName).append("\"; filename=\"").append(fileName)
                .append("\"").append(CRLF).append("Content-Type: ")
                .append(guessContentTypeFromName(fileName)).append(CRLF)
                .append(CRLF);

        writer.flush();
        outputStream.flush();
        try (final InputStream inputStream = uploadFile) {
            final byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }

        writer.append(CRLF);
    }

    public void addHeaderField(String name, String value) {
        if (writer == null) return;
        writer.append(name).append(": ").append(value).append(CRLF);
    }

    public byte[] finish() throws IOException {
        if (writer == null) return null;
        writer.append(CRLF).append("--").append(boundary).append("--")
                .append(CRLF);
        writer.close();

        final int status = connection.getResponseCode();
        if (status != HTTP_OK) {
            try (final InputStream is = connection.getErrorStream()) {
                final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                final byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    bytes.write(buffer, 0, bytesRead);
                }
                return bytes.toByteArray();
            } finally {
                connection.disconnect();
            }
        }

        try (final InputStream is = connection.getInputStream()) {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            final byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bytes.write(buffer, 0, bytesRead);
            }
            return bytes.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
}
