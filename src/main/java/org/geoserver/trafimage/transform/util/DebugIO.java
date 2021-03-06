/* Copyright (c) 2014 geOps - www.geops.de. All rights reserved.
 *
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.trafimage.transform.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.process.ProcessException;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * For easier debugging, ...
 * 
 * @author nico
 *
 */
public class DebugIO {
	
	/**
	 * write the feature collection to a sql file. 
	 * 
	 * this takes a few shortcuts, for example all attributes are omitted
	 * 
	 * SQL dialect of the file is PostgreSQL/PostGIS.
	 * 
	 * @param collection
	 * @param fileName
	 * @param tableName
	 */
	public static void dumpCollectionToSQLFile(SimpleFeatureCollection collection, String fileName, String tableName) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
		} catch (FileNotFoundException e) {
			throw new ProcessException(e);
		} catch (UnsupportedEncodingException e) {
			throw new ProcessException(e);
		}
		
		try {
			writer.println("begin;");
			writer.println("drop table if exists "+tableName+";");	
			writer.println("create table "+tableName+" (id integer, geom geometry);");
			
			final SimpleFeatureIterator cIt = collection.features();
			try {
				int counter=0; 
				while (cIt.hasNext()) {
					final SimpleFeature feature = cIt.next();
					final Geometry geom = (Geometry) feature.getDefaultGeometry();
					if (geom != null) {
						writer.println("insert into "+tableName+" (id, geom) select "+counter+", geomfromtext('"+geom.toText()+"');");
					}
					counter++;
				}
			} finally {
				cIt.close();
			}
			writer.println("commit;");
			
		} finally {
			writer.close();
		}
	}
}
