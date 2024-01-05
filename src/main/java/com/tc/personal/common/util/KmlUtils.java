package com.tc.personal.common.util;

import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.kml.KMLConfiguration;
import org.geotools.xsd.PullParser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Objects;

/**
 * @PROJECT_NAME: public
 * @DESCRIPTION:kml文件工具
 * @AUTHOR: 12615
 * @DATE: 2024/1/5 11:23
 */
@Slf4j
public class KmlUtils {


    /**
     * kml转换为geojson
     *
     * @param input 输入
     * @return {@link StringWriter}
     * @throws IOException ioexception
     */
    public static String kml2Geojson(InputStream input) throws IOException {
        try {
            PullParser parser = new PullParser(new KMLConfiguration(), input, SimpleFeature.class);
            FeatureJSON fjson = new FeatureJSON();
            StringWriter sw = new StringWriter();
            SimpleFeature simpleFeature = (SimpleFeature) parser.parse();
            sw.append("{    \"type\": \"FeatureCollection\",    \"features\": [");
            while (simpleFeature != null) {
                SimpleFeatureType featureType = simpleFeature.getFeatureType();
                fjson.setFeatureType(featureType);
                fjson.writeFeature(simpleFeature, sw);
                simpleFeature = (SimpleFeature) parser.parse();
                if (simpleFeature != null) {
                    sw.append(",");
                }
            }
            sw.append("]}");
            sw.close();
            return sw.toString();
        } catch (Exception e) {
            log.error("KML 文件解析报错：{}", e.getMessage());
            return null;
        } finally {
            Objects.requireNonNull(input, "KML 文件输入流为空").close();
        }
    }
}
