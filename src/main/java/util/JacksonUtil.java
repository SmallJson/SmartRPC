package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

public class JacksonUtil {

    // DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES可避免接收对象缺少对应字段时抛异常
    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public static <T> T readValue(String content, Class<T> valueType) {
        T res = null;
        try {
            if (!StringUtils.isBlank(content)) {
                res = objectMapper.readValue(content, valueType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    //字符串反序列化成自定义类
    public static <T> T readValue(String content, TypeReference valueTypeRef) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T res = null;
        try {
            if (content != null && !content.equals("")) {
                res = mapper.readValue(content, valueTypeRef);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return res;
    }

    //反序列化成JsonNode
    public static JsonNode readTree(String content) {
        JsonNode node = null;
        try {
            if (!StringUtils.isBlank(content)) {
                node = objectMapper.readTree(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return node;
    }

    public static String writeValueAsString(Object value) {
        String json = "";
        try {
            json = objectMapper.writeValueAsString(value);
        } catch (Exception e) {
           e.printStackTrace();
        }

        return json;
    }
}
