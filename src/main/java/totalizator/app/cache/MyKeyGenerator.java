package totalizator.app.cache;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MyKeyGenerator implements KeyGenerator {

	@Override
	public Object generate( final Object target, final Method method, final Object... params ) {
		return generateMD5Hash( params );
	}

	private String generateMD5Hash( final Object... params ) {
		return DigestUtils.md5Hex( Arrays.toString( params ) );
	}
}
