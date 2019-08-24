package com.zf.plugins.app.publish.xiaomi.api;

import com.google.gson.Gson;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Cipher;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author colin
 * @since 2013-5-9 上午11:47:03
 */
public class XiaoMiUploadAppApi {
    private static final String DOMAIN = "http://api.developer.xiaomi.com/devupload";

    private static final String PUSH = DOMAIN + "/dev/push";
    private static final String QUERY = DOMAIN + "/dev/query";
    private static final String CATEGORY = DOMAIN + "/dev/category";

    private static final int KEY_SIZE = 1024;
    private static final int GROUP_SIZE = KEY_SIZE / 8;
    private static final int ENCRYPT_GROUP_SIZE = GROUP_SIZE - 11;
    public static final String KEY_ALGORITHM = "RSA/NONE/PKCS1Padding";


    // 加载BC库
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 公钥加密
     *
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String str, PublicKey publicKey) throws Exception {
        byte[] data = str.getBytes();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] segment = new byte[ENCRYPT_GROUP_SIZE];
        int idx = 0;
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM, "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        while (idx < data.length) {
            int remain = data.length - idx;
            int segsize = remain > ENCRYPT_GROUP_SIZE ? ENCRYPT_GROUP_SIZE : remain;
            System.arraycopy(data, idx, segment, 0, segsize);
            baos.write(cipher.doFinal(segment, 0, segsize));
            idx += segsize;
        }
        return Hex.encodeHexString(baos.toByteArray());
    }

    /**
     * 读取公钥
     *
     * @param cerFilePath
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyByX509Cer(String cerFilePath) throws Exception {
        InputStream x509Is = new FileInputStream(cerFilePath);
        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(x509Is);
            return cert.getPublicKey();
        } finally {
            if (x509Is != null) {
                try {
                    x509Is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getFileMD5(File file) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return DigestUtils.md5Hex(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * 读取应答包
     *
     * @param responseEntity
     * @return
     * @throws IOException
     */
    private static String readResponse(HttpEntity responseEntity) throws IOException {
        if (responseEntity == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        InputStream instream = responseEntity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            reader.close();
        }
        return sb.toString().trim();
    }

    /**
     * 查询分类
     *
     * @return
     */
    public static CategoryResult getCatetories() {
        HttpPost request = new HttpPost(CATEGORY);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            HttpResponse response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();
            String result = readResponse(responseEntity);
            return new Gson().fromJson(result, CategoryResult.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.abort();
        } finally {
            try {
                httpClient.close();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 查询应用信息
     *
     * @param packageName
     * @param userName
     * @param password
     * @return
     */
    public static AppInfoResult query(String cerFilePath, String userName, String password, String packageName) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            PublicKey publicKeyByX509Cer = getPublicKeyByX509Cer(cerFilePath);

            JSONObject json = new JSONObject();
            json.put("packageName", packageName);
            json.put("userName", userName);
            List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
            formParams.add(new BasicNameValuePair("RequestData", json.toString()));

            JSONObject sig = new JSONObject();
            JSONArray sigs = new JSONArray();

            JSONObject sigItem = new JSONObject();
            sigItem.put("name", "RequestData");
            sigItem.put("hash", DigestUtils.md5Hex(json.toString()));

            sigs.put(sigItem);
            sig.put("sig", sigs);
            sig.put("password", password);
            formParams.add(new BasicNameValuePair("SIG", encryptByPublicKey(sig.toString(), publicKeyByX509Cer)));
            HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");

            HttpPost request = new HttpPost(QUERY);
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();

            String result = readResponse(responseEntity);

            return new Gson().fromJson(result, AppInfoResult.class);
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            try {
                httpClient.close();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 推送应用
     *
     * @param email
     * @param password
     * @param synchroType 0：新增;1：更新;2：信息修改
     * @param apkFile
     * @param iconFile
     * @param appDetail
     * @param screenshots
     * @return
     * @throws Exception
     */
    public static Result push(String cerFilePath, String email, String password, int synchroType, File apkFile, File iconFile, AppDetailBean appDetail,
                              List<File> screenshots) {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            PublicKey publicKeyByX509Cer = getPublicKeyByX509Cer(cerFilePath);

            net.sf.json.JSONObject json = new net.sf.json.JSONObject();
            json.put("userName", email);
            json.put("appInfo", appDetail);
            json.put("synchroType", synchroType);

            // 计算数字签名
            net.sf.json.JSONObject sigJSON = new net.sf.json.JSONObject();
            net.sf.json.JSONArray paramMd5Array = new net.sf.json.JSONArray();
            net.sf.json.JSONObject sigItem = new net.sf.json.JSONObject();
            sigItem.put("name", "RequestData");
            sigItem.put("hash", DigestUtils.md5Hex(json.toString()));

            paramMd5Array.add(sigItem);

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();

            if (apkFile != null) {
                net.sf.json.JSONObject apk = new net.sf.json.JSONObject();
                apk.put("name", "apk");
                apk.put("hash", getFileMD5(apkFile));
                paramMd5Array.add(apk);
                entity.addPart("apk", new FileBody(apkFile));
            }

            if (iconFile != null) {
                net.sf.json.JSONObject icon = new net.sf.json.JSONObject();
                icon.put("name", "icon");
                icon.put("hash", getFileMD5(iconFile));
                paramMd5Array.add(icon);
                entity.addPart("icon", new FileBody(iconFile));
            }

            if (screenshots != null && screenshots.size() > 0) {
                for (int i = 0; i < screenshots.size(); i++) {
                    String screenshotName = "screenshot_" + (i + 1);
                    net.sf.json.JSONObject screenshot = new net.sf.json.JSONObject();
                    screenshot.put("name", screenshotName);
                    screenshot.put("hash", getFileMD5(screenshots.get(i)));
                    paramMd5Array.add(screenshot);

                    entity.addPart(screenshotName, new FileBody(screenshots.get(i)));
                }
            }
            sigJSON.put("sig", paramMd5Array);
            sigJSON.put("password", password);

            entity.addPart("RequestData", new StringBody(json.toString(), ContentType.APPLICATION_JSON));
            entity.addPart("SIG", new StringBody(encryptByPublicKey(sigJSON.toString(), publicKeyByX509Cer), ContentType.APPLICATION_JSON));

            HttpPost request = new HttpPost(PUSH);
            request.setEntity(entity.build());
            HttpResponse response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();
            String result = readResponse(responseEntity);
            return new Gson().fromJson(result, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }
}
