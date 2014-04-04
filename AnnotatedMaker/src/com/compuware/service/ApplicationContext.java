package com.compuware.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.compuware.service.util.ReflectionUtil;
import com.compuware.service.util.TypeFilter;

public class ApplicationContext {
	private String [] contextPaths;
    private Map<Class<?>, Object> servicesByClass = new HashMap<>();
    private Map<String, Object> servicesById = new HashMap<>();
	
	
	public ApplicationContext (String ... contextPaths) {
		final TypeFilter filter = new TypeFilter () {

			@Override
			public boolean accept(Class<?> c) {
				Service component = c.getAnnotation(Service.class);
				if (component != null) {
					return true;
				}
				return false;
			}
			
		};

		this.contextPaths = Arrays.copyOf(contextPaths, contextPaths.length);
		for (String path : contextPaths) {
			try {
				for (Class<?> c : ReflectionUtil.getClasses(path, filter)) {
					Object o = c.newInstance();
					Service component = c.getAnnotation(Service.class);
					if (!"".equals(component.id())) {
						servicesById.put(component.id(), o);
					}
					for (Class<?> type: ReflectionUtil.getSuperTypes(c)) {
						servicesByClass.put(type, o);
					}
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}
	
	public String [] getContextPaths () {
		return Arrays.copyOf(contextPaths, contextPaths.length);
	}

	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> type) {
		return (T) servicesByClass.get(type);
	}

	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> type, String id) {
		return (T) servicesById.get(id);
	}

	public Object getService(String id) {
		return servicesById.get(id);
	}
}
