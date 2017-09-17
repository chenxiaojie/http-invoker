package com.chenxiaojie.http.invoker.interceptor;

import com.chenxiaojie.http.invoker.HttpInvoker;
import com.chenxiaojie.http.invoker.exception.HttpRequestException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.Args;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by chenxiaojie on 2017/9/9.
 */
@ThreadSafe
public class BasicAuthInterceptor implements HttpInvoker.Interceptor {

    private static final FastDateFormat fastDateFormat = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss zzz", TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
    private static final String authMethod = "MWS";
    private static final String HmacSHA1 = "HmacSHA1";

    private final String appKey;
    private final String appSecret;
    private final Charset charset;

    public BasicAuthInterceptor(String appKey, String appSecret) {
        this(appKey, appSecret, "UTF-8");
    }

    public BasicAuthInterceptor(String appKey, String appSecret, String charset) {
        Args.notBlank(appKey, "appKey");
        Args.notBlank(appSecret, "appSecret");
        Args.notNull(charset, "charset");
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.charset = Charset.forName(charset);
    }

    @Override
    public boolean intercept(HttpRequestBase httpRequestBase) throws HttpRequestException {
        try {
            String date = fastDateFormat.format(new Date());
            String encryptStr = String.format("%s %s\n%s", httpRequestBase.getMethod(), httpRequestBase.getURI().getPath(), date);
            SecretKeySpec signingKey = new SecretKeySpec(appSecret.getBytes(), HmacSHA1);
            Mac mac = Mac.getInstance(HmacSHA1);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(encryptStr.getBytes(charset));
            String sign = Base64.encodeBase64String(rawHmac);
            httpRequestBase.addHeader("Date", date);
            httpRequestBase.addHeader("Authorization", String.format("%s %s:%s", authMethod, appKey, sign));
        } catch (Exception e) {
            throw new HttpRequestException("addAuth error", e);
        }
        return true;
    }
}
