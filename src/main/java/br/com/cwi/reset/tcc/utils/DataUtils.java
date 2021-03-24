package br.com.cwi.reset.tcc.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DataUtils {

	public static LocalDateTime horaEDataAgora() {
		return LocalDateTime.now();
	}

	public static DayOfWeek diaDaSemanaHoje() {
		return horaEDataAgora().getDayOfWeek();
	}

	public static LocalTime horaAgora() {
		return LocalTime.now();
	}

	public static String dataDeHojeFormatada() {
		DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/uuuu");
		return formatterData.format(horaEDataAgora());
	}

	public static String horaDeAgoraFormatada() {
		DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");
		return formatterHora.format(horaEDataAgora());
	}

}
