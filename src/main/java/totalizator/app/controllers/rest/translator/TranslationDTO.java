package totalizator.app.controllers.rest.translator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class TranslationDTO {

	private String text;

	public String getText() {
		return text;
	}

	public void setText( final String text ) {
		this.text = text;
	}
}
