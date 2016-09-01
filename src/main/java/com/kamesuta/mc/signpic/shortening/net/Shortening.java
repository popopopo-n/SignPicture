package com.kamesuta.mc.signpic.shortening.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.kamesuta.mc.signpic.shortening.IShortening;
import com.kamesuta.mc.signpic.shortening.IShorteningCallback;
import com.kamesuta.mc.signpic.shortening.ShorteningException;
import com.kamesuta.mc.signpic.shortening.ShorteningKey;
import com.kamesuta.mc.signpic.shortening.ShorteningReference;
import com.kamesuta.mc.signpic.util.Downloader;

public abstract class Shortening extends Thread implements IShortening {

	protected String rawurl;
	protected IShorteningCallback callback;
	protected static ShorteningKey shorteningKey;

	public static Downloader downloader = new Downloader();

	@Override
	public void shortening(final String rawurl, final IShorteningCallback callback) {
		this.rawurl = rawurl;
		this.callback = callback;

		setName("Sign Picture Shortening Thread");
		start();
	}

	public ShorteningKey getShorteningKey() throws ShorteningException {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			final byte[] inByte = Base64.decodeBase64(ShorteningReference.WASD);
			bais = new ByteArrayInputStream(inByte);
			ois = new ObjectInputStream(bais);
			return (ShorteningKey)ois.readObject();
		} catch (final Exception e) {
			throw new ShorteningException("Decode Error");
		} finally {
			IOUtils.closeQuietly(bais);
			IOUtils.closeQuietly(ois);
		}
	}

	public BufferedReader getReader(final HttpResponse response) throws ShorteningException {
		try {
			if (response.getStatusLine().getStatusCode() != 200)
				throw new ShorteningException("Transport Error " + response.getStatusLine().getStatusCode());

			final HttpEntity entity = response.getEntity();
			final InputStream is = entity.getContent();
			final InputStreamReader isr = new InputStreamReader(is, Consts.UTF_8);
			final BufferedReader reader = new BufferedReader(isr);
			return reader;
		} catch (final IOException e) {
			throw new ShorteningException("Read Error");
		}
	}

	@Override
	public abstract void run();

	@Override
	public abstract String getServicename();
}
