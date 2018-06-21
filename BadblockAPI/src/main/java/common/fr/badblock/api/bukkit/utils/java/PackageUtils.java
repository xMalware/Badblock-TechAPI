
package fr.badblock.api.bukkit.utils.java;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.plugin.Plugin;

import fr.badblock.api.bukkit.listeners.BadListener;
import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitRequestListener;

public class PackageUtils {

	private static boolean inheritFrom(Class<?> clazz, Class<?> from) {
		if (Modifier.isAbstract(clazz.getModifiers()))
		{
			return false;
		}
		while (clazz != Object.class) {
			if (clazz == from)
				return true;

			clazz = clazz.getSuperclass();
		}

		return false;
	}

	private static Object instanciate(Class<?> clazz) throws Exception {
		try {
			return clazz.getConstructor().newInstance();
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	public static void instanciateListeners(Plugin plugin, String... paths) throws IOException {
		URL url = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();

		ZipInputStream zip = new ZipInputStream(url.openStream());
		ZipEntry entry = null;

		while ((entry = zip.getNextEntry()) != null) {
			String finded = null;

			for (String path : paths) {
				if (entry.getName().startsWith(path.replace(".", "/"))) {
					finded = entry.getName().replace("/", ".");
					break;
				}
			}

			if (finded != null && entry.getName().endsWith(".class")) {
				try {
					String className = finded.substring(0, finded.length() - 6);

					Class<?> clazz = plugin.getClass().getClassLoader().loadClass(className);

					if (inheritFrom(clazz, BadListener.class) || inheritFrom(clazz, RabbitListener.class)|| inheritFrom(clazz, RabbitRequestListener.class)
							|| inheritFrom(clazz, _BungeeProcessing.class)) {
						instanciate(clazz);
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		}
	}

}
