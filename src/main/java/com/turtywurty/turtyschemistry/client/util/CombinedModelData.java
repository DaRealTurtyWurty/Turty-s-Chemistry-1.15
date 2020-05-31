package com.turtywurty.turtyschemistry.client.util;

import javax.annotation.Nullable;

import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class CombinedModelData implements IModelData {
	private final IModelData[] subDatas;

	public CombinedModelData(IModelData... datas) {
		subDatas = datas;
	}

	@Override
	public boolean hasProperty(ModelProperty<?> prop) {
		for (IModelData d : subDatas)
			if (d.hasProperty(prop))
				return true;
		return false;
	}

	@Nullable
	@Override
	public <T2> T2 getData(ModelProperty<T2> prop) {
		for (IModelData d : subDatas)
			if (d.hasProperty(prop))
				return d.getData(prop);
		return null;
	}

	@Nullable
	@Override
	public <T2> T2 setData(ModelProperty<T2> prop, T2 data) {
		// TODO implement
		return null;
	}
}