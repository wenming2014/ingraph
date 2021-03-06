package ingraph.bulkloader.csv.loader.cellprocessor;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CsvContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

abstract public class AbstractParseEpoch extends CellProcessorAdaptor implements StringCellProcessor {

	protected final DateFormat formatter;

	public AbstractParseEpoch(final String dateTimeFormat) {
		super();
		formatter = new SimpleDateFormat(dateTimeFormat);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);

		long epoch = Long.parseLong(value.toString());
		long result = Long.valueOf(formatter.format(new java.util.Date(epoch)));

		return next.execute(result, context);
	}

}
