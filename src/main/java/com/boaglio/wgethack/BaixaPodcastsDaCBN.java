package com.boaglio.wgethack;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.boaglio.wgethack.domain.Podcast;
import com.boaglio.wgethack.domain.Tipo;
import com.boaglio.wgethack.repository.WgetDAO;
import com.boaglio.wgethack.repository.WgetRepository;
import com.boaglio.wgethack.type.Status;

/**
 * Baixar os podcasts da CBN
 * 
 * @author Fernando Boaglio
 */

public class BaixaPodcastsDaCBN {

	// url/ano/colunas/nome_YYMMDD
	// static String URL_CBN = "http://download3.globo.com/sgr-mp3/cbn/";
	// http://download.sgr.globo.com/sgr-mp3/cbn/2013/colunas/atalla_130610.mp3
	static String URL_CBN = "http://download.sgr.globo.com/sgr-mp3/cbn/";
	static SimpleDateFormat dateFormatForDay = new SimpleDateFormat("yyMMdd");
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	static SimpleDateFormat dateFormatForYear = new SimpleDateFormat("yyyy");
	static SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("yyyy-MM");

	private static final Logger logger = Logger.getLogger(BaixaPodcastsDaCBN.class);

	public static void main(String[] args) {

		WgetRepository repositorio = new WgetDAO();
		List<Tipo> todosOsTipos = repositorio.getTodosTipos();

		for (Tipo tipo : todosOsTipos) {

			logger.info("Tipo......." + tipo.getNome());

			// data inicial
			GregorianCalendar dia = new GregorianCalendar();
			dia.set(Calendar.YEAR,2004);
			dia.set(Calendar.MONTH,Calendar.JANUARY);
			dia.set(Calendar.DAY_OF_MONTH,1);

			// data final
			GregorianCalendar ultimoDia = new GregorianCalendar();
			ultimoDia.set(Calendar.YEAR,2013);
			ultimoDia.set(Calendar.MONTH,Calendar.DECEMBER);
			ultimoDia.set(Calendar.DAY_OF_MONTH,31);

			int diasDiff = daysBetween(dia,ultimoDia);

			logger.info("De:" + dateFormat.format(dia.getTime()) + " ate " + dateFormat.format(ultimoDia.getTime()));
			logger.info("Dias: " + diasDiff);

			// gera urls
			GregorianCalendar ateEsseDia = new GregorianCalendar();
			ateEsseDia.setTime(ultimoDia.getTime());
			ultimoDia.setTime(dia.getTime());

			int contador = 1;
			do {

				logger.info("Etapa: >>>>> " + contador + " de " + diasDiff);

				dia.add(Calendar.DAY_OF_MONTH,1);

				Podcast podcast = new Podcast();
				podcast.setTipo(tipo);
				podcast.setDia(dateFormat.format(dia.getTime()));

				Podcast p = repositorio.buscaPodcastById(podcast);

				if (p != null) {

					logger.info("Podcast armazenado: " + p.getDia() + " status:" + p.getStatus());

				} else {

					// verifica se o arquivo existe
					String nomeDoArquivo = montaNomeDoArquivo(podcast);
					logger.info("Nome do arquivo: " + nomeDoArquivo);
					File arqBaixado = new File(nomeDoArquivo);
					if (arqBaixado.exists() && arqBaixado.length() > 1024) {

						logger.info("Arquivo ja baixado: " + arqBaixado.length() / 1024 + "Kb");
						podcast.setStatus(Status.baixado.valor());

					} else {

						logger.info("Baixando arquivo: " + podcast);

						File diretorioDoArquivo = new File(montaDiretorioDoArquivo(podcast));

						if (!diretorioDoArquivo.exists()) {
							diretorioDoArquivo.mkdirs();
							logger.info("Diretorio criado: " + diretorioDoArquivo.getAbsolutePath());
						} else {
							logger.info("Diretorio ja existe: " + diretorioDoArquivo.getAbsolutePath());
						}

						FileUtil.downloadArquivo(buildURLparaDownload(dia,tipo.getNome()),nomeDoArquivo);

						try {
							Thread.sleep(1000l);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						if (arqBaixado.exists() && arqBaixado.length() > 1024) {

							logger.info(">>> Arquivo: " + podcast + " baixado com sucesso!");
							podcast.setStatus(Status.baixado.valor());

						} else {
							logger.info(">>> Arquivo NAO baixado : " + podcast + " !");
							podcast.setStatus(Status.naoEncontrado.valor());

						}

					}

					repositorio.gravaStatus(podcast);

				}

				contador++;

			} while (dia.before(ateEsseDia));
		}

	}

	// static String podcastType [] = {"max"};
	static String podcastType[] = {"atalla","max","halfeld","jabor","lucia2","lucia"};

	private static String montaNomeDoArquivo(Podcast podcast) {

		Date diaDt;
		String dir = "/tmp/teste";
		try {
			diaDt = dateFormat.parse(podcast.getDia());
			GregorianCalendar day = new GregorianCalendar();
			day.setTime(diaDt);
			dir = podcast.getTipo().getDiretorioRaiz() + File.separator + dateFormatForYear.format(day.getTime()) + File.separator + podcast.getTipo().getNome() + "-" + dateFormatForMonth.format(day.getTime()) + File.separator + podcast.getTipo().getNome() + "_" + dateFormatForDay.format(day.getTime()) + ".mp3";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dir;
	}

	private static String montaDiretorioDoArquivo(Podcast podcast) {

		Date diaDt;
		String dir = "/tmp/teste";
		try {
			diaDt = dateFormat.parse(podcast.getDia());
			GregorianCalendar day = new GregorianCalendar();
			day.setTime(diaDt);
			dir = podcast.getTipo().getDiretorioRaiz() + File.separator + dateFormatForYear.format(day.getTime()) + File.separator + podcast.getTipo().getNome() + "-" + dateFormatForMonth.format(day.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dir;
	}

	private static String buildURLparaDownload(GregorianCalendar day,String podcast) {

		return URL_CBN + dateFormatForYear.format(day.getTime()) + "/colunas/" + podcast + "_" + dateFormatForDay.format(day.getTime()) + ".mp3";
	}

	public static int daysBetween(GregorianCalendar d1,GregorianCalendar d2) {
		return (int) ( (d2.getTimeInMillis() - d1.getTimeInMillis()) / (1000 * 60 * 60 * 24));
	}

}
