package com.kamesuta.mc.signpic.shortening.net;

import com.kamesuta.mc.signpic.shortening.IShortening;
import com.kamesuta.mc.signpic.shortening.IShorteningCallback;
import com.kamesuta.mc.signpic.util.Downloader;

public abstract class ShorteningMain extends Thread implements IShortening {

	protected String rawurl;
	protected IShorteningCallback callback;

	public static Downloader downloader = new Downloader();

	@Override
	public void shortening(final String rawurl, final IShorteningCallback callback) {
		this.rawurl = rawurl;
		this.callback = callback;

		setName("Sign Picture Shortening URL Thread");
		start();
	}

	@Override
	public abstract void run();

	@Override
	public abstract String getServicename();
}
