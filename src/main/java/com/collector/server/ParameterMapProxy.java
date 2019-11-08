package com.collector.server;

import java.util.List;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParameterMapProxy<K, V> implements InvocationHandler {
	private Map<K, V> map;

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("put")) {
//			if (sysArgs.contains(args[0]) && map.containsKey(args[0]))
//				throw new Exception("ApplicationContext,RootPath,ArgCfg is systemPara ,can not modify");
			
			//一定要转成字符串,不能直接调用map.get("ArgCfg")作为参数
			Logger logger = LogManager.getLogger((String) map.get("ArgCfg"));

			K key = (K) args[0];
			V object = (V) args[1];
			StringBuilder sb = new StringBuilder(32);
			sb.append(System.lineSeparator()).append("pararms:");
			if (object.getClass().isArray()) {
				sb.append("(k,v)").append(key.toString()).append("=").append(Arrays.toString((Object[]) object));
			} else if (object instanceof List) {
				sb.append("(k,v)").append(key.toString()).append("=");
				for (Object obj : ((List<?>) object)) {
					sb.append(System.lineSeparator()).append("  ").append(obj);
				}
			} else {
				sb.append("(k,v)").append(key.toString()).append("=").append(object.toString());
			}
			logger.debug(sb.toString());
		}
		return method.invoke(map, args);
	}

	public ParameterMapProxy(Map<K, V> map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public Map<K, V> ParameterMapProxyInstance() {
		return (Map<K, V>) Proxy.newProxyInstance(map.getClass().getClassLoader(), map.getClass().getInterfaces(),
				this);
	};

	public void putSysArgs(K key, V value) {
		if (map != null) {
			map.put(key, value);
		}
	}
}
