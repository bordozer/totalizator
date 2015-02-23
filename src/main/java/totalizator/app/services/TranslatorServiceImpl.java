package totalizator.app.services;

import org.springframework.stereotype.Service;

@Service
public class TranslatorServiceImpl implements TranslatorService {

	@Override
	public String translate( final String nerd ) {
		return String.format( "%s&#8482", nerd );
	}
}
