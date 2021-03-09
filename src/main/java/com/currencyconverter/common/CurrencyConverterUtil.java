package com.currencyconverter.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class CurrencyConverterUtil {

	public static String getFormattedDate(Date dateToFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return Optional.ofNullable(dateToFormat).map(sdf::format).orElse(null);
	}

	public static String getFormattedAmount(BigDecimal amountToFormat) {
		return Optional.ofNullable(amountToFormat).map((p) -> {
			p = p.setScale(4, RoundingMode.CEILING);
			return String.format("%1$17s", new DecimalFormat("0.0000").format(p));
		}).orElse(null);
	}

	public static String getFormattedDateTime(Date dateToFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return Optional.ofNullable(dateToFormat).map(sdf::format).orElse(null);
	}

}
