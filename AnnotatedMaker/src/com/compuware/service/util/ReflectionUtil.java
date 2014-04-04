package com.compuware.service.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectionUtil {

	
	public static Iterable<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException
	{
		return getClasses(packageName, null);
	}

	public static Iterable<Class<?>> getClasses(String packageName, TypeFilter filter) throws ClassNotFoundException, IOException
	{
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    String path = packageName.replace('.', '/');
	    Enumeration<URL> resources = classLoader.getResources(path);
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements())
	    {
	        URL resource = resources.nextElement();
	        dirs.add(new File(resource.getFile()));
	    }
	    List<Class<?>> classes = new ArrayList<>();
	    for (File directory : dirs)
	    {
	        classes.addAll(findClasses(directory, packageName, filter));
	    }

	    return classes;
	}

	private static List<Class<?>> findClasses(File directory, String packageName, TypeFilter filter) throws ClassNotFoundException
	{
	    List<Class<?>> classes = new ArrayList<Class<?>>();
	    if (!directory.exists())
	    {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files)
	    {
	        if (file.isDirectory())
	        {
	            classes.addAll(findClasses(file, packageName + "." + file.getName(), filter));
	        }
	        else if (file.getName().endsWith(".class"))
	        {
	        	Class<?> c = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
	        	if (filter == null || filter.accept(c)) {
	                classes.add(c);
	        	}
	        }
	    }
	    return classes;
	}	
	
	public static Set<Class<?>> getSuperTypes (Class<?> c) {
	    Set<Class<?>> classes = new HashSet<Class<?>>();
	    if (!classes.contains(c) && !Object.class.equals(c)) {
	    	classes.add(c);
	    	Class<?> superClass = c.getSuperclass();
	    	if (superClass != null) {
	    		classes.addAll(getSuperTypes(superClass));
	    	}
	    	for (Class<?> inter : c.getInterfaces()) {
	    		classes.addAll(getSuperTypes(inter));
	    	}
	    }
	    return classes;
	}
	
	private ReflectionUtil () {}
	
}
