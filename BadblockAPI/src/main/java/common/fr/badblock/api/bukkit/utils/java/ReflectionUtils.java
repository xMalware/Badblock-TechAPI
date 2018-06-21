package fr.badblock.api.bukkit.utils.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;

/**
 * Classe contenant plusieurs méthodes utiles pour l'utilisation de la
 * réflection. Pour la gestion des fields, préférez
 * {@link fr.badblock.gameapi.utils.reflection.Reflector}.
 * 
 * @author LeLanN
 */
public class ReflectionUtils {
	/**
	 * Récupčre la version Bukkit
	 * 
	 * @return La version
	 */
	public static String getBukkitVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		return name.substring(name.lastIndexOf('.') + 1) + ".";
	}

	/**
	 * Récupčre un constructeur
	 * 
	 * @param clazz
	 *            La classe contenenant le constructeur
	 * @param args
	 *            Les arguments du constructeur
	 * @return Le constructeur
	 */
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) {
		try {
			return clazz.getConstructor(args);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupčre et rend accessible un Field
	 * 
	 * @param clazz
	 *            Classe contenant le field
	 * @param name
	 *            Nom du field
	 * @return Le Field
	 */
	public static Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Récupčre la méthode getHandle présente sur beaucoup d'objets OBC
	 * 
	 * @param obj
	 *            L'object en question
	 * @return Le résultat du getHandle()s
	 */
	public static Object getHandle(Object obj) {
		try {
			return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Récupčre une méthode
	 * 
	 * @param clazz
	 *            La classe contenenant la méthode
	 * @param name
	 *            Le nom de la méthode
	 * @param args
	 *            Les arguments de la méthode
	 * @return La méthode
	 */
	public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
		try {
			return clazz.getMethod(name, args);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupčre une classe NMS
	 * 
	 * @param className
	 *            Le nom de la classe recherchée
	 * @return La classe trouvée
	 */
	public static Class<?> getNMSClass(String className) {
		try {
			return Class.forName("net.minecraft.server." + getBukkitVersion() + className);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupčre une classe OBC (org.bukkit.craftbukkit)
	 * 
	 * @param className
	 *            Le nom de la classe ŕ trouver
	 * @return La classe trouvée
	 */
	public static Class<?> getOBCClass(String className) {
		try {
			return Class.forName("org.bukkit.craftbukkit." + getBukkitVersion() + className);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupčre une classe appartenant ŕ une autre
	 * 
	 * @param clazz
	 *            La classe "mčre"
	 * @param name
	 *            Le nom de la sous classe
	 * @return La classe trouvée
	 */
	public static Class<?> getSubClass(Class<?> clazz, String name) {
		for (Class<?> c : clazz.getDeclaredClasses()) {
			if (c.getSimpleName().equals("EnumTitleAction")) {
				return c;
			}
		}

		return null;
	}

	/**
	 * Enlčve le flag 'final' ŕ un Field.
	 * 
	 * @param field
	 *            Le field auquel enlever le flag 'final'.
	 */
	public static void removeFinal(Field field) {
		if (Modifier.isFinal(field.getModifiers())) {
			try {
				Reflector reflector = new Reflector(field);
				reflector.setFieldValue("modifiers", field.getModifiers() & 0xffffffef);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
