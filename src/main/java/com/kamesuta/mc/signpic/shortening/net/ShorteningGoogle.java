package com.kamesuta.mc.signpic.shortening.net;

import java.io.BufferedReader;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ShorteningGoogle extends Shortening {

	public static final String API_PATH = "https://www.googleapis.com/urlshortener/v1/url";

	@Override
	public void run() {
		try {
			final String token = getShorteningKey().getPicture();

			final HttpPost post = new HttpPost(API_PATH + "?key=" + token);

			final StringBuilder stb = new StringBuilder();
			stb.append("{'longUrl': '");
			stb.append(this.rawurl);
			stb.append("'}");

			post.setEntity(new StringEntity(new String(stb), Consts.UTF_8));
			post.setHeader("Content-Type", "application/json");

			final HttpResponse response = downloader.client.execute(post);
			final BufferedReader reader = getReader(response);

			final JsonReader jsonReader = new JsonReader(reader);
			final GoogleJson apiJson = new Gson().fromJson(jsonReader, GoogleJson.class);
			reader.close();
			jsonReader.close();

			this.callback.onShorteningDone(apiJson.id);
		} catch (final Throwable e) {
			this.callback.onShorteningError("Shortening Error", e);
		}
	}

	@Override
	public String getServicename() {
		return "goo.gl";
	}

	class GoogleJson {
		public String kind;
		public String id;
		public String longUrl;
		public String status;
	}

}
