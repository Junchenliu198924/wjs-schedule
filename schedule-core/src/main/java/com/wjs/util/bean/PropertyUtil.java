package com.wjs.util.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertyUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);

	public static void copyProperties(Object dest, Object orig) {
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			LOGGER.error("bean copy error:", e);
		} 
		
	}
	
	/**
	 * 从集合元素中获取对应字段的值作为一个list返回，集合元素必须是JavaBean
	 *
	 * @param collection
	 * @param fieldName JavaBean对象的filed name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T,S> List<S> fetchFieldList(Collection<T> collection, String fieldName) {

		List<S> fields = new ArrayList<S>();

		if (CollectionUtils.isEmpty(collection) || StringUtils.isEmpty(fieldName)) {
			return fields;
		}

		Object fieldValue;
		try {
			for (T t : collection) {
				if (t == null) {
					continue;
				}
				fieldValue = BeanUtils.getProperty(t, fieldName);
				fields.add(fieldValue == null ? null : (S)fieldValue);
			}
		} catch (Exception e) {
			LOGGER.warn("Get property '" + fieldName + "' from  collection [" + collection + "] error", e);
		}

		return fields;
	}

}
