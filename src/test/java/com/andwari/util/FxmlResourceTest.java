package com.andwari.util;

import java.net.URL;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class FxmlResourceTest {

	@Test
	public void testAllResourcesArePresent() {
		for (FxmlResource res : FxmlResource.values()) {
			URL url = getClass().getClassLoader().getResource(res.getPath());
			if (url == null)
				System.out.println(res);
			Assertions.assertThat(url).isNotNull();
		}
	}

}
