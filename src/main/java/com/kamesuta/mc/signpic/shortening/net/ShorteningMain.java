package com.kamesuta.mc.signpic.shortening.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.codec.binary.Base64;

import com.kamesuta.mc.signpic.shortening.IShortening;
import com.kamesuta.mc.signpic.shortening.IShorteningCallback;
import com.kamesuta.mc.signpic.shortening.ShorteningKey;
import com.kamesuta.mc.signpic.shortening.ShorteningReference;
import com.kamesuta.mc.signpic.util.Downloader;

public abstract class ShorteningMain extends Thread implements IShortening {

	protected String rawurl;
	protected IShorteningCallback callback;
	protected static ShorteningKey shorteningKey;

	public static Downloader downloader = new Downloader();

	@Override
	public void shortening(final String rawurl, final IShorteningCallback callback) {
		this.rawurl = rawurl;
		this.callback = callback;

		setName("Sign Picture Shortening URL Thread");
		start();
	}

	public ShorteningKey getShorteningKey() throws IOException, ClassNotFoundException {
		final byte[] inByte = Base64.decodeBase64(ShorteningReference.WASD);
		final ByteArrayInputStream bais = new ByteArrayInputStream(inByte);
		final ObjectInputStream ois = new ObjectInputStream(bais);
		final ShorteningKey shorteningKey = (ShorteningKey)ois.readObject();
		bais.close();
		ois.close();
		return shorteningKey;
	}

	@Override
	public abstract void run();

	@Override
	public abstract String getServicename();
}
