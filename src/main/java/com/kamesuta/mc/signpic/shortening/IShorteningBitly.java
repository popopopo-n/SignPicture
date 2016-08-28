package com.kamesuta.mc.signpic.shortening;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.shortening.exception.ShorteningException;

public class IShorteningBitly extends ShorteningMain {

	public static final String API_PATH = "https://api-ssl.bitly.com/v3/shorten";

	@Override
	public void run() {
		try {
			final ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(ShorteningReference.WASD));
			final ObjectInputStream ois = new ObjectInputStream(bais);
			final ShorteningKey iShorteningKey = (ShorteningKey)ois.readObject();
			bais.close();
			ois.close();

			final String token = iShorteningKey.getSign();

			final StringBuilder stb = new StringBuilder();
			stb.append(API_PATH);
			stb.append("?access_token=");
			stb.append(token);
			stb.append("&longUrl=");
			stb.append(this.rawurl);
			stb.append("&format=json");

			final String path = new String(stb);

			final HttpUriRequest req = new HttpGet(path);
			final HttpResponse response = downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();
			final InputStream is = entity.getContent();

			if (response.getStatusLine().getStatusCode() != 200)
				throw new ShorteningException("Transport Error: " + response.getStatusLine().getStatusCode());

			final InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			final BufferedReader reader = new BufferedReader(isr);

			final JsonReader jsonReader = new JsonReader(reader);
			final BitlyAPIJson apiJson = new Gson().fromJson(jsonReader, BitlyAPIJson.class);
			jsonReader.close();

			this.callback.onShorteningDone(apiJson.data.url);
		} catch (final Throwable e) {
			this.callback.onShorteningError("Shortening Error", e);
		}
	}

	@Override
	public String getServicename() {
		return "Bit.ly";
	}

}

class BitlyAPIJson {
	public String status_code;
	public String status_txt;
	public Data data;
	public static class Data {
		public String long_url;
		public String url;
		public String hash;
		public String global_hash;
		public String new_hash;
	}
}
