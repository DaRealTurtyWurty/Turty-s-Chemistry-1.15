package com.turtywurty.turtyschemistry.core.util;

public abstract class FluidHandlerReferenceHolder {
	private FluidStackHandler lastKnownValue;

	public static FluidHandlerReferenceHolder create(final IFluidHandlerArray data, final int index) {
		return new FluidHandlerReferenceHolder() {
			public FluidStackHandler get() {
				return data.get(index);
			}

			public void set(FluidStackHandler value) {
				data.set(index, value);
			}
		};
	}

	public static FluidHandlerReferenceHolder create(final FluidStackHandler[] data, final int index) {
		return new FluidHandlerReferenceHolder() {
			public FluidStackHandler get() {
				return data[index];
			}

			public void set(FluidStackHandler value) {
				data[index] = value;
			}
		};
	}

	public static FluidHandlerReferenceHolder single() {
		return new FluidHandlerReferenceHolder() {
			private FluidStackHandler value;

			public FluidStackHandler get() {
				return this.value;
			}

			public void set(FluidStackHandler value) {
				this.value = value;
			}
		};
	}

	public abstract FluidStackHandler get();

	public abstract void set(FluidStackHandler value);

	public boolean isDirty() {
		FluidStackHandler handler = this.get();
		boolean flag = handler != this.lastKnownValue;
		this.lastKnownValue = handler;
		return flag;
	}
}