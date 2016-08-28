package com.kamesuta.mc.signpic;

import com.kamesuta.mc.signpic.shortening.IShorteningBitly;
import com.kamesuta.mc.signpic.shortening.IShorteningCallback;

public class Debug {

	public static void main(final String[] args) {
		//		Reference.logger.info(ImageSizes.LIMIT.size(32, 24, 10, 12));

		final IShorteningCallback callback = new IShorteningCallback() {

			@Override
			public void onShorteningError(final String message, final Throwable e) {
				Reference.logger.error(message, e);;
			}

			@Override
			public void onShorteningDone(final String shorturl) {
				Reference.logger.info(shorturl);
			}
		};
		IShorteningBitly.INSTANCE.shortening("https://github.com/Team-Fruit", callback);
	}

}
