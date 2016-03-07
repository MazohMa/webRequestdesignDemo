package com.modelweb.maxiaokun.modelaboutwebrequestdemo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @Description
 * @author Mazoh
 * @createDate 2015年7月18日
 * @version 1.0.0
 */

public class GsonUtil {
	/**
	 * 构建一个安全的Gson
	 * 
	 * 当解析遇到错误时忽略改字段，返回null
	 * 
	 * @return
	 */
	public static Gson createSecurityGson() {
		return new GsonBuilder()
				.registerTypeAdapter(String.class,
						new JsonDeserializer<String>() {
							@Override
							public String deserialize(JsonElement je,
									Type type,
									JsonDeserializationContext context)
									throws JsonParseException {
								try {
									return je.getAsString();
								} catch (Exception e) {
									return null;
								}
							}
						})
				.registerTypeAdapter(Integer.class,
						new JsonDeserializer<Integer>() {
							@Override
							public Integer deserialize(JsonElement je,
									Type type,
									JsonDeserializationContext context)
									throws JsonParseException {
								try {
									return je.getAsInt();
								} catch (Exception e) {
									return null;
								}
							}
						})
				.registerTypeAdapter(Long.class, new JsonDeserializer<Long>() {
					@Override
					public Long deserialize(JsonElement je, Type type,
							JsonDeserializationContext context)
							throws JsonParseException {
						try {
							return je.getAsLong();
						} catch (Exception e) {
							return null;
						}
					}
				})
				.registerTypeAdapter(Float.class,
						new JsonDeserializer<Float>() {
							@Override
							public Float deserialize(JsonElement je, Type type,
									JsonDeserializationContext context)
									throws JsonParseException {
								try {
									return je.getAsFloat();
								} catch (Exception e) {
									return null;
								}
							}
						})
				.registerTypeAdapter(Double.class,
						new JsonDeserializer<Double>() {
							@Override
							public Double deserialize(JsonElement je,
									Type type,
									JsonDeserializationContext context)
									throws JsonParseException {
								try {
									return je.getAsDouble();
								} catch (Exception e) {
									return null;
								}
							}
						}).create();
	}
}
