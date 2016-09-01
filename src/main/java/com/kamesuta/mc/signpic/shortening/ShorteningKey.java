package com.kamesuta.mc.signpic.shortening;

import java.io.Serializable;

public class ShorteningKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String sign;
	private final String picture;

	public ShorteningKey(final String sign, final String picture) {
		this.sign = sign;
		this.picture = picture;
	}

	public String getSign() {
		return this.sign;
	}

	public String getPicture() {
		return this.picture;
	}
}