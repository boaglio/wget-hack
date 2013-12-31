package com.boaglio.wgethack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Gerar scripts de wget para baixar os podcasts
 * 
 * @author Fernando Boaglio
 */

@Deprecated
public class GenerateWgetForCBN {

	// url/ano/colunas/nome_YYMMDD
	// static String URL_CBN = "http://download3.globo.com/sgr-mp3/cbn/";
	// http://download.sgr.globo.com/sgr-mp3/cbn/2013/colunas/atalla_130610.mp3
	static String URL_CBN = "http://download.sgr.globo.com/sgr-mp3/cbn/";
	static SimpleDateFormat dateFormatForDay = new SimpleDateFormat("yyMMdd");
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
	static SimpleDateFormat dateFormatForYear = new SimpleDateFormat("yyyy");
	static SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("yyyy-MM");
	// static String podcastType [] = {"max"};
	static String podcastType[] = {"atalla","max","halfeld","jabor","lucia2","lucia"};

	public static void main(String[] args) {

		for (String podcast : podcastType) {

			// data inicial
			GregorianCalendar dia = new GregorianCalendar();
			dia.set(Calendar.YEAR,2004);
			dia.set(Calendar.MONTH,Calendar.JANUARY);
			dia.set(Calendar.DAY_OF_MONTH,1);

			GregorianCalendar ultimoDia = new GregorianCalendar();
			ultimoDia.set(Calendar.YEAR,2013);
			ultimoDia.set(Calendar.MONTH,Calendar.MAY);
			ultimoDia.set(Calendar.DAY_OF_MONTH,30);

			System.out.println("De:" + dateFormat.format(dia.getTime()) + " ate " + dateFormat.format(ultimoDia.getTime()));

			// gera urls
			GregorianCalendar ateEsseDia = new GregorianCalendar();
			ateEsseDia.setTime(ultimoDia.getTime());
			ultimoDia.setTime(dia.getTime());
			StringBuilder builder = new StringBuilder();
			do {

				builder.append(buildCommand(dia,podcast));

				dia.add(Calendar.DAY_OF_MONTH,1);

				if (dia.get(Calendar.MONTH) != ultimoDia.get(Calendar.MONTH)) {

					// grava o arquivo
					writeFile(builder,podcast,dateFormatForMonth.format(ultimoDia.getTime()));

					ultimoDia.add(Calendar.MONTH,1);

					builder = new StringBuilder();
				}

			} while (dia.before(ateEsseDia));

			// grava o arquivo
			writeFile(builder,podcast,dateFormatForMonth.format(ultimoDia.getTime()));
		}

	}

	private static void writeFile(StringBuilder content,String podcast,String monthAndYear) {
		Writer output = null;
		File file = new File(podcast + "-" + monthAndYear + ".sh");

		try {
			output = new BufferedWriter(new FileWriter(file));
			// init
			output.write("#!/bin/sh \n");
			output.write("echo ......................................\n");
			output.write("echo Baixando " + podcast + "-" + monthAndYear + "\n");
			output.write("echo ......................................\n");
			output.write("mkdir  " + podcast + "-" + monthAndYear + "\n");
			output.write("cd " + podcast + "-" + monthAndYear + "\n");
			// wget
			output.write(content.toString());
			// end
			output.write("cd ..\n");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Arquivo " + monthAndYear + "-" + podcast + " escrito!");
	}

	private static String buildCommand(GregorianCalendar day,String podcast) {

		return "wget -c " + URL_CBN + dateFormatForYear.format(day.getTime()) + "/colunas/" + podcast + "_" + dateFormatForDay.format(day.getTime()) + ".mp3\n";
	}

}
