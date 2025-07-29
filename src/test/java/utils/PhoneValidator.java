package utils;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class PhoneValidator {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String VALIDATE_URL = "https://beta-api-gateway.fcam.vn/brm-mngt/api/auth/validate/phone-number/";

    public static boolean isPhoneNumberRegistered(String phoneNumber) throws IOException {
        // Tạo body JSON
        JSONObject json = new JSONObject();
        json.put("country_code", "84");
        json.put("phone_number", phoneNumber);
        json.put("email", "");

        RequestBody requestBody = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(VALIDATE_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("gis", "b841ed7258384318b24cc92a7160ab8d")  // thay bằng giá trị thực nếu cần
                .addHeader("package", "iot.lab.camera")
                .addHeader("language", "vi")
                .addHeader("version", "2.1.0")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseBody = response.body().string();
        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray dataArray = responseJson.getJSONArray("data");

        if (dataArray.length() > 0) {
            JSONObject dataObj = dataArray.getJSONObject(0);
            return dataObj.getBoolean("is_exist");
        }

        return false;
    }
}

