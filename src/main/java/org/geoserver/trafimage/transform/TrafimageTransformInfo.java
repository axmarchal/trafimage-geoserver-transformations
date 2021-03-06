/* Copyright (c) 2014 geOps - www.geops.de. All rights reserved.
 *
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.trafimage.transform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;
import org.markdownj.MarkdownProcessor;

public class TrafimageTransformInfo {

	private static final Logger LOGGER = Logging.getLogger(TrafimageTransformInfo.class);
	
	public TrafimageTransformInfo() {
	}
	
    public String getGitVersion() {
        String gitVersion = getResourceContents("/trafimage-geoserver-transformations.gitversion");
        if ((gitVersion==null) || (gitVersion=="")) {
            gitVersion = "<No git version information available>";
        }
        return gitVersion;
	}

    
	private String getReadme() {
        String readmeText = getResourceContents("/README.trafimage-geoserver-transformations.md");
        if ((readmeText==null) || (readmeText=="")) {
        	readmeText = "Readme is not available";
        }
        return readmeText;
	}
	
	public String getReadmeHtml() {
		MarkdownProcessor processor = new MarkdownProcessor();
		return processor.markdown(this.getReadme());
	}
	
	private String getResourceContents(String resourceName) {
        InputStream rs = getClass().getResourceAsStream(resourceName);
        if (rs==null) {
            return "";
        }
        try {
	        Scanner scanner = new Scanner(rs,"UTF-8");
	        try {
	        	return scanner.useDelimiter("\\A").next().trim();
	        } finally {
	        	scanner.close();
	        }
        } finally {
        	try {
				rs.close();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Could not close inputstream to "+resourceName, e);
			}
        }
    }
	
	public String getVersion() {
        String version = getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = "unknown - not packaged";
        }
        return version;
	}
}
