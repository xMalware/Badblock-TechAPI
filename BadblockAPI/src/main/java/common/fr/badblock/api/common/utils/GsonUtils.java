package fr.badblock.api.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;

public class GsonUtils
{
	@Getter@Setter
	private static Gson	gson		= new Gson();
	@Getter@Setter
	private static Gson	prettyGson	= new GsonBuilder().setPrettyPrinting().create();
}