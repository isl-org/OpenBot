package org.openbot.env;

public class Control {
	private final float left;
	private final float right;
	private final float speedMultiplier;

	public Control(float left, float right, float speedMultiplier) {
		this.left = Math.max(-1.f, Math.min(1.f, left));
		this.right = Math.max(-1.f, Math.min(1.f, right));
		this.speedMultiplier = speedMultiplier;
	}

	public float getLeft() {
		return left * speedMultiplier;
	}

	public float getRight() {
		return right * speedMultiplier;
	}
}
