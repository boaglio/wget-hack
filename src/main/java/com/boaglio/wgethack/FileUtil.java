package com.boaglio.wgethack;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.log4j.Logger;

public class FileUtil {

	private static final Logger logger = Logger.getLogger(FileUtil.class);

	public static void downloadArquivo(String linkDoArquivo,String destino) {

		FileOutputStream fos = null;
		try {
			logger.info(" >>> " + linkDoArquivo + " >>> " + destino);
			URL url = new URL(linkDoArquivo);
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());

			fos = new FileOutputStream(destino);
			fos.getChannel().transferFrom(rbc,0,1 << 24);

			logger.info(" >>> download terminado!");

		} catch (FileNotFoundException fnf) {
			logger.info("Arquivo " + linkDoArquivo + " nao existe para baixar!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
