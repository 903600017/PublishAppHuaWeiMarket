package com.zf.plugins.app.publish.huawei

public class Utils {

    public static String getContent(File file) throws IOException {

        FileReader reader = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new FileReader(file);
            bufferedReader = new BufferedReader(reader);
            char buf = new char[1024]
            int length = 0;
            while ((length = bufferedReader.read(buf)) != -1) {
                stringBuilder.append(buf, 0, length)
            }

        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return stringBuilder.toString();
    }

    public static String getResourceContent(String filePath){
        URL resource = Utils.class.getClassLoader().getResource(filePath);
        return resource.getText("utf-8")
    }
}
