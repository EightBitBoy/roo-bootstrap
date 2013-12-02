package com.roo.bootstrap;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.operations.AbstractOperations;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.FeatureNames;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Property;
import org.springframework.roo.support.osgi.OSGiUtils;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Implementation of {@link BootstrapOperations} interface.
 * 
 * @since 1.1.1
 */
@Component
@Service
public class BootstrapOperationsImpl extends AbstractOperations implements BootstrapOperations {

	/**
	 * Get hold of a JDK Logger
	 */
	private Logger log = Logger.getLogger(getClass().getName());

	private static final char SEPARATOR = File.separatorChar;

	/**
	 * Get a reference to the ProjectOperations from the underlying OSGi
	 * container. Make sure you are referencing the Roo bundle which contains
	 * this service in your add-on pom.xml.
	 */
	@Reference
	private ProjectOperations projectOperations;

	@Reference
	MetadataService metadataService;

	/** {@inheritDoc} */
	public String getProperty(String propertyName) {
		Validate.notBlank(propertyName, "Property name required");
		return System.getProperty(propertyName);
	}

	public boolean isInstallBootstrapAvailable() {
		return isProjectAvailable() && isControllerAvailable();
	}

	private boolean isProjectAvailable() {
		return projectOperations.isFocusedProjectAvailable();
	}

	private boolean isControllerAvailable() {
		PathResolver pathResolver = projectOperations.getPathResolver();
		return fileManager.exists(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF/views"))
				&& !projectOperations.isFeatureInstalledInFocusedModule(FeatureNames.JSF)
				&& fileManager.exists(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "spring" + SEPARATOR
						+ "webmvc-config.xml"))
				&& fileManager.exists(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "tags"));
	}

	/** {@inheritDoc} */
	public void installBootstrap() {
		// Use PathResolver to get canonical resource names for a given artifact
		PathResolver pathResolver = projectOperations.getPathResolver();
		Element configuration = XmlUtils.getConfiguration(getClass());
		// Install
		this.updatePomProperties(configuration);
		this.updateDependencies(configuration);

		copyDirectoryContents("images/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "images"), true);
		copyDirectoryContents("js/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "js"), true);
		copyDirectoryContents("fonts/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "fonts"), true);
		copyDirectoryContents("styles/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "styles"), true);
		copyDirectoryContents("WEB-INF/layouts/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "layouts"), true);
		copyDirectoryContents("WEB-INF/views/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "views"), true);
		copyDirectoryContents("WEB-INF/tags/form/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "tags" + SEPARATOR + "form"), true);
		copyDirectoryContents("WEB-INF/tags/form/fields/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "tags" + SEPARATOR + "form" + SEPARATOR + "fields"), true);
		copyDirectoryContents("WEB-INF/tags/menu/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "tags" + SEPARATOR + "menu"), true);
		copyDirectoryContents("WEB-INF/tags/util/*.*", pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "tags" + SEPARATOR + "util"), true);
	}

	private void updatePomProperties(Element configuration) {
		List<Element> properties = XmlUtils.findElements("/configuration/springjs/properties/*", configuration);
		for (Element property : properties) {
			projectOperations.addProperty(projectOperations.getFocusedModuleName(), new Property(property));
		}
	}

	private void updateDependencies(Element configuration) {

		List<Dependency> dependencies = new ArrayList<Dependency>();
		List<Element> springJsDependencies = XmlUtils.findElements("/configuration/springjs/dependencies/dependency", configuration);
		for (Element dependencyElement : springJsDependencies) {
			dependencies.add(new Dependency(dependencyElement));
		}
		projectOperations.addDependencies(projectOperations.getFocusedModuleName(), dependencies);
	}
}