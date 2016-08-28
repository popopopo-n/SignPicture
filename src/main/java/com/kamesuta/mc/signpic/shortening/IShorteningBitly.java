package com.kamesuta.mc.signpic.shortening;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.shortening.exception.ShorteningException;

public class IShorteningBitly extends IShorteningMain {

	public static final IShorteningBitly INSTANCE = new IShorteningBitly();

	public static final String API_PATH = "https://api-ssl.bitly.com/v3/shorten";

	@Override
	public void run() {
		try {
			final ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(IShorteningReference.WASD));
			final ObjectInputStream ois = new ObjectInputStream(bais);
			final IShorteningKey iShorteningKey = (IShorteningKey)ois.readObject();
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

			//			final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
			//
			//			final List<Header> headers = new ArrayList<Header>();
			//			headers.add(new BasicHeader("Accept-Charset", "UTF-8"));
			//			headers.add(new BasicHeader("Accept-Language", "en-US,en;q=0.5"));
			//			headers.add(new BasicHeader("User-Agent", Reference.MODID));
			//
			//			final HttpClient client =  HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setDefaultHeaders(headers).build();

			final HttpGet httpGet = new HttpGet(path);
			final HttpResponse response = downloader.client.execute(httpGet);
			final InputStream is = response.getEntity().getContent();
			//			final String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

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
