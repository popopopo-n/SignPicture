package com.kamesuta.mc.signpic.shortening.net;

public class ShorteningGoogle extends ShorteningMain {

	public static final String API_PATH = "https://www.googleapis.com/urlshortener/v1/url";

	@Override
	public void run() {
		try {
			final String token = getShorteningKey().getPicture();

			final StringBuilder stb = new StringBuilder();
			stb.append(API_PATH);
			stb.append("?key=");
			stb.append(this.rawurl);

			final String path = new String(stb);

		} catch (final Throwable e) {
			this.callback.onShorteningError("Shortening Error", e);
		}
	}

	@Override
	public String getServicename() {
		return "goo.gl";
	}

}
