package totalizator.app.translator;

public class NerdKey {

	private final String nerd;

	public NerdKey( final String nerd ) {
		this.nerd = nerd;
	}

	public String getNerd() {
		return nerd;
	}

	@Override
	public String toString() {
		return nerd;
	}

	@Override
	public int hashCode() {
		return nerd.hashCode();
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof NerdKey ) ) {
			return false;
		}

		final NerdKey nerdKey = ( NerdKey ) obj;
		return nerd.equals( nerdKey.getNerd() );
	}
}
