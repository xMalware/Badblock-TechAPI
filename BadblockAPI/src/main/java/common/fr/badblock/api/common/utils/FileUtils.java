package fr.badblock.api.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class FileUtils
{
	public static final String EOL = System.getProperty("line.separator");

	public static String readFile(File file) throws Exception
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

		String str;
		StringBuilder stringBuilder = new StringBuilder();
		
		while ((str = in.readLine()) != null)
		{
			stringBuilder.append(str);
			stringBuilder.append(EOL);
		}

		in.close();
		return stringBuilder.toString();
	}

	public static void writeFile(File file, String json)
	{
		try
		{
			PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
			writer.println(json);
			writer.close();
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}
	}

	public static boolean contentEquals(File file1, File file2) throws IOException
	{
		if(!file1.exists() || !file2.exists())
		{
			return false;
		}

		InputStream is1 = new FileInputStream(file1);
		InputStream is2 = new FileInputStream(file2);

		int v1 = 0, v2 = 0;

		while( (v1 = is1.read()) != -1 && (v2 = is2.read()) != -1 && v1 == v2 );

		is1.close();
		is2.close();

		return v1 == v2;
	}

	public static void copyFile(File source, File dest) throws IOException
	{
		InputStream is = null;
		OutputStream os = null;
		
		try
		{
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			
			byte[] buffer = new byte[1024];
			int length;

			while ((length = is.read(buffer)) > 0)
			{
				os.write(buffer, 0, length);
			}
		}
		finally
		{
			is.close();
			os.close();
		}
	}
	
	public static void copyDirectory(File src, File dst) throws IOException
	{
		if(!src.isDirectory())
			throw new IOException("Source isn't a directory");
		
		if(dst.exists() && dst.isFile())
			dst.delete();
				
		if(!dst.exists())
			dst.mkdirs();
		
		for(File file : src.listFiles())
		{
			if(file.isDirectory())
			{
				copyDirectory(file, new File(dst, file.getName()));
			}
			else
			{
				copyFile(file, new File(dst, file.getName()));
			}
		}
	}
	
	public static void delete(File folder)
	{
		if(!folder.exists())
			return;

		if(folder.isDirectory())
			for(File f : folder.listFiles())
				if(f.isDirectory())
					delete(f);
				else f.delete();

		folder.delete();
	}
}