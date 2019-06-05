package cn.linj2n.melody.security;

import cn.linj2n.melody.config.MelodyProperties;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Component
public class QiniuAuthenticationFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(QiniuAuthenticationFilter.class);

    public static final String QINIU_AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private Auth qiniuAuth;

    @Autowired
    private MelodyProperties melodyProperties;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        QiniuAuthenticationRequestWrapper requestWrapper = new QiniuAuthenticationRequestWrapper((HttpServletRequest) request);
        final String callbackAuthHeader = requestWrapper.getHeader(QINIU_AUTHORIZATION_HEADER);
        byte[] callbackBody = IOUtils.toByteArray(requestWrapper.getInputStream());
        boolean validCallback =
                qiniuAuth.isValidCallback(
                        callbackAuthHeader,
                        melodyProperties.getQiniu().getCallBackHandlingUrl(),
                        callbackBody,
                        "application/json"
                );
        if (!validCallback) {

            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The Authorization token is not valid.");
        } else {
            logger.info("Qiniu callback request authentication succeeded.");
            chain.doFilter(requestWrapper, response);
        }
    }
//    static class QiniuAuthenticationRequestWrapper extends HttpServletRequestWrapper {
//
//        private final byte[] body;
//
//        public QiniuAuthenticationRequestWrapper(HttpServletRequest request) throws IOException {
//            super(request);
//            body = toByteArray(request.getInputStream());
//        }
//
//        private byte[] toByteArray(InputStream in) throws IOException {
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024 * 4];
//            int n = 0;
//            while ((n = in.read(buffer)) != -1) {
//                out.write(buffer, 0, n);
//            }
//            return out.toByteArray();
//        }
//
//        @Override
//        public BufferedReader getReader() throws IOException {
//            return new BufferedReader(new InputStreamReader(getInputStream()));
//        }
//
//
//        @Override
//        public ServletInputStream getInputStream() throws IOException {
//            final ByteArrayInputStream bais = new ByteArrayInputStream(body);
//            return new ServletInputStream() {
//
//                @Override
//                public boolean isFinished() {
//                    return bais.read() == -1;
//                }
//
//                @Override
//                public boolean isReady() {
//                    return true;
//                }
//
//                @Override
//                public void setReadListener(ReadListener listener) {
//
//                }
//
//                @Override
//                public int read() throws IOException {
//                    return bais.read();
//                }
//            };
//        }
//    }


    private class QiniuAuthenticationRequestWrapper extends HttpServletRequestWrapper {

        private byte[] body;

        public QiniuAuthenticationRequestWrapper(HttpServletRequest request) {
            super(request);
            try {
                body = IOUtils.toByteArray(request.getInputStream());
            } catch (IOException ex) {
                body = new byte[0];
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new DelegatingServletInputStream(new ByteArrayInputStream(body));
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }
    }

    private class DelegatingServletInputStream extends ServletInputStream {

        private final InputStream sourceStream;

        private boolean finished = false;


        /**
         * Create a DelegatingServletInputStream for the given source stream.
         *
         * @param sourceStream the source stream (never {@code null})
         */
        public DelegatingServletInputStream(InputStream sourceStream) {
            this.sourceStream = sourceStream;
        }

        /**
         * Return the underlying source stream (never {@code null}).
         */
        public final InputStream getSourceStream() {
            return this.sourceStream;
        }


        @Override
        public int read() throws IOException {
            int data = this.sourceStream.read();
            if (data == -1) {
                this.finished = true;
            }
            return data;
        }

        @Override
        public int available() throws IOException {
            return this.sourceStream.available();
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.sourceStream.close();
        }

        @Override
        public boolean isFinished() {
            return this.finished;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

    }

//    private static class QiniuAuthenticationRequestWrapper extends
//            HttpServletRequestWrapper {
//
//    private static final Logger logger = LoggerFactory.getLogger(QiniuAuthenticationRequestWrapper.class);
//    private final String payload;
//
//    public QiniuAuthenticationRequestWrapper(HttpServletRequest request) {
//        super(request);
//        // read the original payload into the payload variable
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader bufferedReader = null;
//        try {
//            // read the payload into the StringBuilder
//            InputStream inputStream = request.getInputStream();
//            if (inputStream != null) {
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                char[] charBuffer = new char[128];
//                int bytesRead = -1;
//                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                    stringBuilder.append(charBuffer, 0, bytesRead);
//                }
//            } else {
//                // make an empty string since there is no payload
//                stringBuilder.append("");
//            }
//        } catch (IOException ex) {
//            logger.error("Error reading the request payload", ex);
//            throw new RuntimeException("Error reading the request payload", ex);
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException iox) {
//                    // ignore
//                }
//            }
//        }
//        payload = stringBuilder.toString();
//    }
//
//    @Override
//    public ServletInputStream getInputStream() throws IOException {
//        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload.getBytes());
//        ServletInputStream inputStream = new ServletInputStream() {
//            @Override
//            public int read()
//                    throws IOException {
//                return byteArrayInputStream.read();
//            }
//
//            @Override
//            public boolean isFinished() {
//                return byteArrayInputStream.read() == -1;
//            }
//
//            @Override
//            public boolean isReady() {
//                return true;
//            }
//
//            @Override
//            public void setReadListener(ReadListener readListener) {
//            }
//        };
//        return inputStream;
//    }
//
//    @Override
//    public BufferedReader getReader() throws IOException {
//        return new BufferedReader(new InputStreamReader(getInputStream()));
//    }
//
//    public String getPayload() {
//        return payload;
//    }
//}
}
