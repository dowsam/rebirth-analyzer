/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer PaodingMaker.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.knife;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import net.paoding.analysis.Constants;
import net.paoding.analysis.analyzer.impl.MostWordsModeDictionariesCompiler;
import net.paoding.analysis.analyzer.impl.SortingDictionariesCompiler;
import net.paoding.analysis.dictionary.support.detection.Difference;
import net.paoding.analysis.dictionary.support.detection.DifferenceListener;
import net.paoding.analysis.exception.PaodingAnalysisException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.store.FSLockFactory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NativeFSLockFactory;

/**
 * The Class PaodingMaker.
 *
 * @author l.xue.nong
 */
public class PaodingMaker {

	/** The Constant DEFAULT_PROPERTIES_PATH. */
	public static final String DEFAULT_PROPERTIES_PATH = "classpath:paoding-analysis.properties";

	/**
	 * Instantiates a new paoding maker.
	 */
	private PaodingMaker() {
	}

	/** The log. */
	private static Log log = LogFactory.getLog(PaodingMaker.class);

	/** The properties holder. */
	private static ObjectHolder/* <Properties> */propertiesHolder = new ObjectHolder/* <Properties> */();

	/** The paoding holder. */
	private static ObjectHolder/* <Paoding> */paodingHolder = new ObjectHolder/* <Paoding> */();

	// ----------------获取Paoding对象的方法-----------------------

	/**
	 * Make.
	 *
	 * @return the paoding
	 */
	public static Paoding make() {
		return make(DEFAULT_PROPERTIES_PATH);
	}

	/**
	 * Make.
	 *
	 * @param propertiesPath the properties path
	 * @return the paoding
	 */
	public static Paoding make(String propertiesPath) {
		return make(getProperties(propertiesPath));
	}

	/**
	 * Make.
	 *
	 * @param p the p
	 * @return the paoding
	 */
	public static Paoding make(Properties p) {
		postPropertiesLoaded(p);
		return implMake(p);
	}

	// --------------------------------------------------

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	public static Properties getProperties() {
		return getProperties(DEFAULT_PROPERTIES_PATH);
	}

	/**
	 * Gets the properties.
	 *
	 * @param path the path
	 * @return the properties
	 */
	public static Properties getProperties(String path) {
		if (path == null) {
			throw new NullPointerException("path should not be null!");
		}
		try {
			//
			Properties p = (Properties) propertiesHolder.get(path);
			if (p == null || modified(p)) {
				p = loadProperties(new Properties(), path);
				propertiesHolder.set(path, p);
				paodingHolder.remove(path);
				postPropertiesLoaded(p);
				String absolutePaths = p.getProperty("paoding.analysis.properties.files.absolutepaths");
				log.info("config paoding analysis from: " + absolutePaths);
			}
			return p;
		} catch (IOException e) {
			throw new PaodingAnalysisException(e);
		}
	}

	// -------------------私有 或 辅助方法----------------------------------

	/**
	 * Modified.
	 *
	 * @param p the p
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static boolean modified(Properties p) throws IOException {
		String lastModifieds = p.getProperty("paoding.analysis.properties.lastModifieds");
		String[] lastModifedsArray = lastModifieds.split(";");
		String files = p.getProperty("paoding.analysis.properties.files");
		String[] filesArray = files.split(";");
		for (int i = 0; i < filesArray.length; i++) {
			File file = getFile(filesArray[i]);
			if (file.exists() && !String.valueOf(getFileLastModified(file)).equals(lastModifedsArray[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Load properties.
	 *
	 * @param p the p
	 * @param path the path
	 * @return the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static Properties loadProperties(Properties p, String path) throws IOException {
		URL url;
		File file;
		String absolutePath;
		InputStream in;
		// 若ifexists为真表示如果该文件存在则读取他的内容，不存在则忽略它
		boolean skipWhenNotExists = false;
		if (path.startsWith("ifexists:")) {
			skipWhenNotExists = true;
			path = path.substring("ifexists:".length());
		}
		if (path.startsWith("classpath:")) {
			path = path.substring("classpath:".length());
			url = getClassLoader().getResource(path);
			if (url == null) {
				if (skipWhenNotExists) {
					return p;
				}
				throw new FileNotFoundException("Not found " + path + " in classpath.");
			}

			/*
			 * Fix issue 42 : 读取配置文件的一个Bug
			 */
			file = new File(getUrlPath(url));
			in = url.openStream();
		} else {
			if (path.startsWith("dic-home:")) {
				File dicHome = new File(getDicHome(p));
				path = path.substring("dic-home:".length());
				file = new File(dicHome, path);
			} else {
				file = new File(path);
			}
			if (skipWhenNotExists && !file.exists()) {
				return p;
			}
			in = new FileInputStream(file);
		}
		absolutePath = file.getAbsolutePath();
		p.load(in);
		in.close();
		String lastModifieds = p.getProperty("paoding.analysis.properties.lastModifieds");
		String files = p.getProperty("paoding.analysis.properties.files");
		String absolutePaths = p.getProperty("paoding.analysis.properties.files.absolutepaths");
		if (lastModifieds == null) {
			p.setProperty("paoding.dic.properties.path", path);
			lastModifieds = String.valueOf(getFileLastModified(file));
			files = path;
			absolutePaths = absolutePath;
		} else {
			lastModifieds = lastModifieds + ";" + getFileLastModified(file);
			files = files + ";" + path;
			absolutePaths = absolutePaths + ";" + absolutePath;
		}
		p.setProperty("paoding.analysis.properties.lastModifieds", lastModifieds);
		p.setProperty("paoding.analysis.properties.files", files);
		p.setProperty("paoding.analysis.properties.files.absolutepaths", absolutePaths);
		String importsValue = p.getProperty("paoding.imports");
		if (importsValue != null) {
			p.remove("paoding.imports");
			String[] imports = importsValue.split(";");
			for (int i = 0; i < imports.length; i++) {
				loadProperties(p, imports[i]);
			}
		}
		return p;
	}

	/**
	 * Gets the file last modified.
	 *
	 * @param file the file
	 * @return the file last modified
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static long getFileLastModified(File file) throws IOException {
		String path = file.getPath();
		int jarIndex = path.indexOf(".jar!");
		if (jarIndex == -1) {
			return file.lastModified();
		} else {
			path = path.replaceAll("%20", " ").replaceAll("\\\\", "/");
			jarIndex = path.indexOf(".jar!");
			int protocalIndex = path.indexOf(":");
			String jarPath = path.substring(protocalIndex + ":".length(), jarIndex + ".jar".length());
			File jarPathFile = new File(jarPath);
			JarFile jarFile;
			try {
				jarFile = new JarFile(jarPathFile);
				String entryPath = path.substring(jarIndex + ".jar!/".length());
				JarEntry entry = jarFile.getJarEntry(entryPath);
				return entry.getTime();
			} catch (IOException e) {
				System.err.println("error in handler path=" + path);
				System.err.println("error in handler jarPath=" + jarPath);
				throw e;
			}
		}
	}

	/**
	 * Gets the dic home.
	 *
	 * @param p the p
	 * @return the dic home
	 */
	private static String getDicHome(Properties p) {
		setDicHomeProperties(p);
		return p.getProperty("paoding.dic.home.absolute.path");
	}

	/**
	 * Post properties loaded.
	 *
	 * @param p the p
	 */
	private static void postPropertiesLoaded(Properties p) {
		if ("done".equals(p.getProperty("paoding.analysis.postPropertiesLoaded"))) {
			return;
		}
		setDicHomeProperties(p);
		p.setProperty("paoding.analysis.postPropertiesLoaded", "done");
	}

	/**
	 * Sets the dic home properties.
	 *
	 * @param p the new dic home properties
	 */
	private static void setDicHomeProperties(Properties p) {
		String dicHomeAbsultePath = p.getProperty("paoding.dic.home.absolute.path");
		if (dicHomeAbsultePath != null) {
			return;
		}
		// 获取词典安装目录配置：
		// 如配置了PAODING_DIC_HOME环境变量，则将其作为字典的安装主目录
		// 否则使用属性文件的paoding.dic.home配置
		// 但是如果属性文件中强制配置paoding.dic.home.config-first=this，
		// 则优先考虑属性文件的paoding.dic.home配置，
		// 此时只有当属性文件没有配置paoding.dic.home时才会采用环境变量的配置
		String dicHomeBySystemEnv = null;
		try {
			dicHomeBySystemEnv = getSystemEnv(Constants.ENV_PAODING_DIC_HOME);
		} catch (Error e) {
			log.warn("System.getenv() is not supported in JDK1.4. ");
		}
		String dicHome = getProperty(p, Constants.DIC_HOME);
		if (dicHomeBySystemEnv != null) {
			String first = getProperty(p, Constants.DIC_HOME_CONFIG_FIRST);
			if (first != null && first.equalsIgnoreCase("this")) {
				if (dicHome == null) {
					dicHome = dicHomeBySystemEnv;
				}
			} else {
				dicHome = dicHomeBySystemEnv;
			}
		}
		// 如果环境变量和属性文件都没有配置词典安转目录
		// 则尝试在当前目录和类路径下寻找是否有dic目录，
		// 若有，则采纳他为paoding.dic.home
		// 如果尝试后均失败，则抛出PaodingAnalysisException异常
		if (dicHome == null) {
			File f = new File("dic");
			if (f.exists()) {
				dicHome = "dic/";
			} else {
				URL url = PaodingMaker.class.getClassLoader().getResource("dic");
				if (url != null) {
					dicHome = "classpath:dic/";
				}
			}
		}
		if (dicHome == null) {
			throw new PaodingAnalysisException(
					"please set a system env PAODING_DIC_HOME or Config paoding.dic.home in paoding-dic-home.properties point to the dictionaries!");
		}
		// 规范化dicHome，并设置到属性文件对象中
		dicHome = dicHome.replace('\\', '/');
		if (!dicHome.endsWith("/")) {
			dicHome = dicHome + "/";
		}
		p.setProperty(Constants.DIC_HOME, dicHome);// writer to the properites
		// object
		// 将dicHome转化为一个系统唯一的绝对路径，记录在属性对象中
		File dicHomeFile = getFile(dicHome);
		if (!dicHomeFile.exists()) {
			throw new PaodingAnalysisException("not found the dic home dirctory! " + dicHomeFile.getAbsolutePath());
		}
		if (!dicHomeFile.isDirectory()) {
			if (dicHomeFile.getPath().indexOf('!') > 0 && dicHomeFile.getPath().startsWith("file:")) {
				String path = dicHomeFile.getPath().substring(0, dicHomeFile.getPath().indexOf('!'));
				path = path.substring(5);
				path = path.replace('\\', '/');
				try {
					JarEntry entry;
					JarInputStream jarStream = new JarInputStream(new FileInputStream(path));
					while ((entry = jarStream.getNextJarEntry()) != null) {
						String name = entry.getName();
						if (entry.isDirectory() && name.startsWith("dic")) {
							FileUtils.forceMkdir(new File(System.getProperty("user.dir") + "/" + name));
						}
						if (!entry.isDirectory() && name.startsWith("dic")) {
							OutputStream out = new FileOutputStream(new File(System.getProperty("user.dir") + "/"
									+ name));
							InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
							try {
								org.apache.commons.io.IOUtils.copy(in, out);
								out.flush();
							} finally {
								IOUtils.closeQuietly(in);
								IOUtils.closeQuietly(out);
							}
						}
					}
					dicHomeFile = new File(System.getProperty("user.dir") + "/dic");
				} catch (Exception e) {
					throw new PaodingAnalysisException(e);
				}
			} else {
				throw new PaodingAnalysisException("dic home should not be a file, but a directory!");
			}
		}
		p.setProperty("paoding.dic.home.absolute.path", dicHomeFile.getAbsolutePath());
	}

	/**
	 * Impl make.
	 *
	 * @param p the p
	 * @return the paoding
	 */
	private static Paoding implMake(final Properties p) {
		// 将要返回的Paoding对象，它可能是新创建的，也可能使用paodingHolder中已有的Paoding对象
		Paoding paoding;
		// 作为本次返回的Paoding对象在paodingHolder中的key，使之后同样的key不会重复创建Paoding对象
		final Object paodingKey;
		// 如果该属性对象是通过PaodingMaker由文件读入的，则必然存在paoding.dic.properties.path属性
		// 详细请参考loadProperties方法)
		String path = p.getProperty("paoding.dic.properties.path");
		// 如果该属性由文件读入，则文件地址作为Paoding对象在paodingHolder中的key
		if (path != null) {
			paodingKey = path;
			// 否则以属性文件作为其key，之后只要进来的是同一个属性对象，都返回同一个Paoding对象
		} else {
			paodingKey = p;
		}
		paoding = (Paoding) paodingHolder.get(paodingKey);
		if (paoding != null) {
			return paoding;
		}
		try {
			paoding = createPaodingWithKnives(p);
			final Paoding finalPaoding = paoding;
			//
			String compilerClassName = getProperty(p, Constants.ANALYZER_DICTIONARIES_COMPILER);
			Class compilerClass = null;
			if (compilerClassName != null) {
				compilerClass = Class.forName(compilerClassName);
			}
			if (compilerClass == null) {
				String analyzerMode = getProperty(p, Constants.ANALYZER_MODE);
				if ("most-words".equalsIgnoreCase(analyzerMode) || "default".equalsIgnoreCase(analyzerMode)) {
					compilerClass = MostWordsModeDictionariesCompiler.class;
				} else {
					compilerClass = SortingDictionariesCompiler.class;
				}
			}
			final DictionariesCompiler compiler = (DictionariesCompiler) compilerClass.newInstance();
			new Function() {
				public void run() throws Exception {
					String LOCK_FILE = "write.lock";
					String dicHome = p.getProperty("paoding.dic.home.absolute.path");
					FSLockFactory FileLockFactory = new NativeFSLockFactory(dicHome);
					Lock lock = FileLockFactory.makeLock(LOCK_FILE);

					boolean obtained = false;
					try {
						obtained = lock.obtain(90000);
						if (obtained) {
							// 编译词典-对词典进行可能的处理，以符合分词器的要求
							if (compiler.shouldCompile(p)) {
								Dictionaries dictionaries = readUnCompiledDictionaries(p);
								Paoding tempPaoding = createPaodingWithKnives(p);
								setDictionaries(tempPaoding, dictionaries);
								compiler.compile(dictionaries, tempPaoding, p);
							}

							// 使用编译后的词典
							final Dictionaries dictionaries = compiler.readCompliedDictionaries(p);
							setDictionaries(finalPaoding, dictionaries);

							// 启动字典动态转载/卸载检测器
							// 侦测时间间隔(秒)。默认为60秒。如果设置为０或负数则表示不需要进行检测
							String intervalStr = getProperty(p, Constants.DIC_DETECTOR_INTERVAL);
							int interval = Integer.parseInt(intervalStr);
							if (interval > 0) {
								dictionaries.startDetecting(interval, new DifferenceListener() {
									public void on(Difference diff) throws Exception {
										dictionaries.stopDetecting();

										// 此处调用run方法，以当检测到**编译后**的词典变更/删除/增加时，
										// 重新编译源词典、重新创建并启动dictionaries自检测
										run();
									}
								});
							}
						}
					} catch (LockObtainFailedException ex) {
						log.error("Obtain " + LOCK_FILE + " in " + dicHome + " failed:" + ex.getMessage());
						throw ex;
					} catch (IOException ex) {
						log.error("Obtain " + LOCK_FILE + " in " + dicHome + " failed:" + ex.getMessage());
						throw ex;
					} finally {
						if (obtained) {
							try {
								lock.release();
							} catch (Exception ex) {

							}
						}
					}
				}
			}.run();
			// Paoding对象创建成功！此时可以将它寄放到paodingHolder中，给下次重复利用
			paodingHolder.set(paodingKey, paoding);
			return paoding;
		} catch (Exception e) {
			throw new PaodingAnalysisException("", e);
		}
	}

	/**
	 * Creates the paoding with knives.
	 *
	 * @param p the p
	 * @return the paoding
	 * @throws Exception the exception
	 */
	private static Paoding createPaodingWithKnives(Properties p) throws Exception {
		// 如果PaodingHolder中并没有缓存该属性文件或对象对应的Paoding对象，
		// 则根据给定的属性创建一个新的Paoding对象，并在返回之前存入paodingHolder
		Paoding paoding = new Paoding();

		// 寻找传说中的Knife。。。。
		final Map /* <String, Knife> */knifeMap = new HashMap /*
																 * <String,
																 * Knife>
																 */();
		final List /* <Knife> */knifeList = new LinkedList/* <Knife> */();
		final List /* <Function> */functions = new LinkedList/* <Function> */();
		Iterator iter = p.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry e = (Map.Entry) iter.next();
			final String key = (String) e.getKey();
			final String value = (String) e.getValue();
			int index = key.indexOf(Constants.KNIFE_CLASS);
			if (index == 0 && key.length() > Constants.KNIFE_CLASS.length()) {
				final int end = key.indexOf('.', Constants.KNIFE_CLASS.length());
				if (end == -1) {
					Class clazz = Class.forName(value);
					Knife knife = (Knife) clazz.newInstance();
					knifeList.add(knife);
					knifeMap.put(key, knife);
					log.info("add knike: " + value);
				} else {
					// 由于属性对象属于hash表，key的读取顺序不和文件的顺序一致，不能保证属性设置时，knife对象已经创建
					// 所以这里只定义函数放到functions中，待到所有的knife都创建之后，在执行该程序
					functions.add(new Function() {
						public void run() throws Exception {
							String knifeName = key.substring(0, end);
							Object obj = knifeMap.get(knifeName);
							if (!obj.getClass().getName().equals("org.springframework.beans.BeanWrapperImpl")) {
								Class beanWrapperImplClass = Class.forName("org.springframework.beans.BeanWrapperImpl");
								Method setWrappedInstance = beanWrapperImplClass.getMethod("setWrappedInstance",
										new Class[] { Object.class });
								Object beanWrapperImpl = beanWrapperImplClass.newInstance();
								setWrappedInstance.invoke(beanWrapperImpl, new Object[] { obj });
								knifeMap.put(knifeName, beanWrapperImpl);
								obj = beanWrapperImpl;
							}
							String propertyName = key.substring(end + 1);
							Method setPropertyValue = obj.getClass().getMethod("setPropertyValue",
									new Class[] { String.class, Object.class });
							setPropertyValue.invoke(obj, new Object[] { propertyName, value });
						}
					});
				}
			}
		}
		// 完成所有留后执行的程序
		for (Iterator iterator = functions.iterator(); iterator.hasNext();) {
			Function function = (Function) iterator.next();
			function.run();
		}
		// 把刀交给庖丁
		paoding.setKnives(knifeList);
		return paoding;
	}

	/**
	 * Read un compiled dictionaries.
	 *
	 * @param p the p
	 * @return the dictionaries
	 */
	private static Dictionaries readUnCompiledDictionaries(Properties p) {
		String skipPrefix = getProperty(p, Constants.DIC_SKIP_PREFIX);
		String noiseCharactor = getProperty(p, Constants.DIC_NOISE_CHARACTOR);
		String noiseWord = getProperty(p, Constants.DIC_NOISE_WORD);
		String unit = getProperty(p, Constants.DIC_UNIT);
		String confucianFamilyName = getProperty(p, Constants.DIC_CONFUCIAN_FAMILY_NAME);
		String combinatorics = getProperty(p, Constants.DIC_FOR_COMBINATORICS);
		String charsetName = getProperty(p, Constants.DIC_CHARSET);
		int maxWordLen = Integer.valueOf(getProperty(p, Constants.DIC_MAXWORDLEN));
		Dictionaries dictionaries = new FileDictionaries(getDicHome(p), skipPrefix, noiseCharactor, noiseWord, unit,
				confucianFamilyName, combinatorics, charsetName, maxWordLen);
		return dictionaries;
	}

	/**
	 * Sets the dictionaries.
	 *
	 * @param paoding the paoding
	 * @param dictionaries the dictionaries
	 */
	private static void setDictionaries(Paoding paoding, Dictionaries dictionaries) {
		Knife[] knives = paoding.getKnives();
		for (int i = 0; i < knives.length; i++) {
			Knife knife = (Knife) knives[i];
			if (knife instanceof DictionariesWare) {
				((DictionariesWare) knife).setDictionaries(dictionaries);
			}
		}
	}

	/**
	 * Gets the url path.
	 *
	 * @param url the url
	 * @return the url path
	 */
	private static String getUrlPath(URL url) {
		if (url == null)
			return null;
		String urlPath = null;
		try {
			urlPath = url.toURI().getPath();
		} catch (URISyntaxException e) {
		}
		if (urlPath == null) {
			urlPath = url.getFile();
		}
		return urlPath;
	}

	/**
	 * Gets the file.
	 *
	 * @param path the path
	 * @return the file
	 */
	private static File getFile(String path) {
		File file;
		URL url;
		if (path.startsWith("classpath:")) {
			path = path.substring("classpath:".length());
			url = getClassLoader().getResource(path);

			/*
			 * Fix issue 42 : 读取配置文件的一个Bug
			 */
			if (url != null) {
				path = getUrlPath(url);
			}
			final boolean fileExist = url != null;
			file = new File(path) {
				private static final long serialVersionUID = 4009013298629147887L;

				public boolean exists() {
					return fileExist;
				}
			};
		} else {
			file = new File(path);
		}
		return file;
	}

	/**
	 * Gets the class loader.
	 *
	 * @return the class loader
	 */
	private static ClassLoader getClassLoader() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = PaodingMaker.class.getClassLoader();
		}
		return loader;
	}

	/**
	 * Gets the property.
	 *
	 * @param p the p
	 * @param name the name
	 * @return the property
	 */
	private static String getProperty(Properties p, String name) {
		return Constants.getProperty(p, name);
	}

	// --------------------------------------------------------------------

	/**
	 * The Class ObjectHolder.
	 *
	 * @author l.xue.nong
	 */
	private static class ObjectHolder/* <T> */{

		/**
		 * Instantiates a new object holder.
		 */
		private ObjectHolder() {
		}

		/** The objects. */
		private Map/* <Object, T> */objects = new HashMap/* <Object, T> */();

		/**
		 * Gets the.
		 *
		 * @param name the name
		 * @return the object
		 */
		public Object/* T */get(Object name) {
			return objects.get(name);
		}

		/**
		 * Sets the.
		 *
		 * @param name the name
		 * @param object the object
		 */
		public void set(Object name, Object/* T */object) {
			objects.put(name, object);
		}

		/**
		 * Removes the.
		 *
		 * @param name the name
		 */
		public void remove(Object name) {
			objects.remove(name);
		}
	}

	/**
	 * The Interface Function.
	 *
	 * @author l.xue.nong
	 */
	private static interface Function {

		/**
		 * Run.
		 *
		 * @throws Exception the exception
		 */
		public void run() throws Exception;
	}

	/**
	 * Gets the system env.
	 *
	 * @param name the name
	 * @return the system env
	 */
	private static String getSystemEnv(String name) {
		try {
			String path = System.getenv(name);
			if (path == null)
				path = System.getProperty(name);
			return path;
		} catch (Error error) {
			String path = System.getProperty(name);
			if (path != null)
				return path;
			String osName = System.getProperty("os.name").toLowerCase();
			try {
				String cmd;
				if (osName.indexOf("win") != -1) {
					cmd = "cmd /c SET";
				} else {
					cmd = "/usr/bin/printenv";
				}
				Process process = Runtime.getRuntime().exec(cmd);
				InputStreamReader isr = new InputStreamReader(process.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null && line.startsWith(name)) {
					int index = line.indexOf(name + "=");
					if (index != -1) {
						return line.substring(index + name.length() + 1);
					}
				}
			} catch (Exception e) {
				log.warn("unable to read env from os．" + e.getMessage(), e);
			}
		}
		return null;
	}

}
