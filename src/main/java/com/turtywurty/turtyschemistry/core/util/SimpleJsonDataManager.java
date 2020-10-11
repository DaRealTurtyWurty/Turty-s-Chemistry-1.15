package com.turtywurty.turtyschemistry.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

//Credits to Commoble for this code
public class SimpleJsonDataManager<T> extends JsonReloadListener {
	
	public static final Gson GSON = new GsonBuilder().create();

	/** The raw data that we parsed from json last time resources were reloaded **/
	protected Map<ResourceLocation, T> data = new HashMap<>();

	private final Class<T> dataClass;

	/**
	 * @param folder This is the name of the folders that the resource loader looks
	 *               in, e.g. assets/modid/FOLDER
	 */
	public SimpleJsonDataManager(String folder, Class<T> dataClass) {
		super(GSON, folder);
		this.dataClass = dataClass;
	}

	/**
	 * Get the data object represented by the json at the given resource location
	 **/
	public Map<ResourceLocation, T> getData() {
		return this.data;
	}

	/**
	 * Called on resource reload, the jsons have already been found for us and we
	 * just need to parse them in here
	 **/
	@Override
	protected void apply(Map<ResourceLocation, JsonObject> jsons, IResourceManager manager, IProfiler profiler) {
		this.data = mapValues(jsons, this::getJsonAsData);
	}

	/**
	 * Converts all the values in a map to new values; the new map uses the same
	 * keys as the old map
	 **/
	public static <Key, In, Out> Map<Key, Out> mapValues(Map<Key, In> inputs, Function<In, Out> mapper) {
		Map<Key, Out> newMap = new HashMap<>();

		inputs.forEach((key, input) -> newMap.put(key, mapper.apply(input)));

		return newMap;
	}

	/**
	 * Use a json object (presumably one from an assets/turtychemistry/agitator folder) to
	 * generate a data object
	 **/
	protected T getJsonAsData(JsonObject json) {
		return GSON.fromJson(json, this.dataClass);
	}
}