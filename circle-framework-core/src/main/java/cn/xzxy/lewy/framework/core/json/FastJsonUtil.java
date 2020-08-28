package cn.xzxy.lewy.framework.core.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lewy95
 **/
@Slf4j
public class FastJsonUtil {
    /**
     * 验证json 格式,json值为空或json格式错误,返回false
     *
     * @param json json字符串
     * @return boolean
     */
    public static boolean valid(String json) {
        return JSON.isValid(json);
    }

    /**
     * JSON反序列化
     */
    public static <V> List<V> fromJsonArray(String json, Class<V> c) {
        return JSONObject.parseArray(json, c);
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(Object jsonObj, Class<V> c) {
        return JSON.parseObject(jsonObj.toString(), c);
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, Class<V> c) {
        return JSON.parseObject(json, c);
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, TypeReference<V> typeReference) {
        return JSON.parseObject(json, typeReference.getType());
    }

    /**
     * 序列化为JSON
     */
    public static <V> String to(List<V> list) {
        if (Objects.isNull(list)) {
            return "[]";
        }
        return JSON.toJSONString(list);
    }

    /**
     * 序列化为JSON
     */
    public static <V> String to(V v) {
        if (Objects.isNull(v)) {
            return "{}";
        }
        return JSON.toJSONString(v);
    }

    /**
     * 格式化数据对象转为json的数据
     *
     * @param v
     * @param <V>
     * @return
     */
    public static <V> String toFormatJson(V v) {
        return format(to(v));
    }

    /**
     * 格式化数据对象转为json的数据
     *
     * @param list
     * @param <V>
     * @return
     */
    public static <V> String toFormatJson(List<V> list) {
        return format(to(list));
    }

    /**
     * 从json串中获取某个字段
     *
     * @return String
     */
    public static String getString(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject == null) {
            return null;
        }
        try {
            return jsonObject.getString(key);
        } catch (Exception e) {
            log.error("fastjson get string error, json: " + json + ", key: " + key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return int
     */
    public static Integer getInt(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject == null) {
            return null;
        }
        try {
            return jsonObject.getInteger(key);
        } catch (Exception e) {
            log.error("fastjson get string error, json: " + json + ", key: " + key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return long
     */
    public static Long getLong(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject == null) {
            return null;
        }
        try {
            return jsonObject.getLong(key);
        } catch (Exception e) {
            log.error("fastjson get string error, json: " + json + ", key: " + key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return double
     */
    public static Double getDouble(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject == null) {
            return null;
        }
        try {
            return jsonObject.getDouble(key);
        } catch (Exception e) {
            log.error("fastjson get string error, json: " + json + ", key: " + key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return double
     */
    public static BigInteger getBigInteger(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return new BigInteger(String.valueOf(0.00));
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject == null) {
            return new BigInteger(String.valueOf(0.00));
        }
        try {
            return jsonObject.getBigInteger(key);
        } catch (Exception e) {
            log.error("fastjson get string error, json: " + json + ", key: " + key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return double
     */
    public static BigDecimal getBigDecimal(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject == null) {
            return null;
        }
        try {
            return jsonObject.getBigDecimal(key);
        } catch (Exception e) {
            log.error("fastjson get string error, json: " + json + ", key: " + key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return boolean, 默认为false
     */
    public static boolean getBoolean(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject == null) {
            return false;
        }
        try {
            return jsonObject.getBooleanValue(key);
        } catch (Exception e) {
            log.error("fastjson get string error, json: " + json + ", key: " + key, e);
            return false;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return boolean, 默认为false
     */
    public static Byte getByte(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject == null) {
            return null;
        }
        return jsonObject.getByteValue(key);
    }

    /**
     * 从json串中获取某个字段
     *
     * @return boolean, 默认为false
     */
    public static <T> List<T> getList(String json, String key, Class<T> c) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        List<T> ts = null;
        if (jsonObject != null) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                ts = jsonArray.toJavaList(c);
            } catch (Exception e) {
                log.error("fastjson get string error, json: " + json + ", key: " + key, e);
            }
        }
        return ts;
    }

    public static Map<String, Object> getMap(String json) {
        if (StringUtils.isBlank(json)) {
            return new HashMap<String, Object>(16);
        }
        return JSONObject.parseObject(json);
    }

    /**
     * 向json中添加属性
     *
     * @return json
     */
    public static <T> String add(String json, String key, T value) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        add(jsonObject, key, value);
        return jsonObject.toString();
    }

    /**
     * 向json中添加属性
     */
    private static <T> void add(JSONObject jsonObject, String key, T value) {
        if (value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Byte[]) {
            jsonObject.put(key, value);
        } else {
            jsonObject.put(key, to(value));
        }
    }

    /**
     * 除去json中的某个属性
     *
     * @return json
     */
    public static String remove(String json, String key) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        jsonObject.remove(key);
        return jsonObject.toString();
    }

    /**
     * 修改json中的属性
     */
    public static <T> String update(String json, String key, T value) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        add(jsonObject, key, value);
        return jsonObject.toString();
    }

    /**
     * 格式化Json(美化)
     *
     * @return json
     */
    public static String format(String json) {

        try {
            JSONObject jsonObject = JSONObject.parseObject(json);
            return JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat);

        } catch (Exception e) {
            log.warn("格式化失败,即将转为json集合format");
        }
        return formatArray(json);
    }

    /**
     * 格式化Json数组(美化)
     *
     * @return json
     */
    public static String formatArray(String json) {
        JSONArray jsonArray = JSONObject.parseArray(json);
        return JSON.toJSONString(jsonArray, SerializerFeature.PrettyFormat);
    }

    /**
     * 判断字符串是否是json
     *
     * @return json
     */
    public static boolean isJson(String json) {
        return JSON.isValid(json);
    }
}
