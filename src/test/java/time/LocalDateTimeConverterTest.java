package time;

public class LocalDateTimeConverterTest {

	/*@Test
	public void convertToDatabaseColumn() {

		final LocalDateTimeConverter converter = new LocalDateTimeConverter();

		final LocalDateTime localDateTime = LocalDateTime.of( 2015, 10, 5, 3, 0 );
		final Date date = converter.convertToDatabaseColumn( localDateTime );

		assertEquals( "Wrong date", "Mon Oct 05 03:00:00 EEST 2015", date.toString() );
	}

	@Test
	public void convertToEntityAttribute() {

		final LocalDateTimeConverter converter = new LocalDateTimeConverter();

		final Date time = new Date( 1444003200000L ); // 2015-10-05 03:00:00
		final LocalDateTime localDateTime = converter.convertToEntityAttribute( time );

		assertEquals( "Wrong local date time", "2015-10-05T03:00", localDateTime.toString() );
	}*/
}
