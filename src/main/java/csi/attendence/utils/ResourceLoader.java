package csi.attendence.utils;

import org.springframework.core.io.Resource;

public interface ResourceLoader {

	Resource getResource(String location);

	ClassLoader getClassLoader();
}