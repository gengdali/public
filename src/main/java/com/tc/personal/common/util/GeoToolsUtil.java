package com.tc.personal.common.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;


/**
 * .
 *
 * @Author:wwwzhqwww
 * @Date: 9:54 2022/10/10/22.
 * @Description
 * @Modified By:
 */
@Slf4j
public class GeoToolsUtil {

    private static WKTReader reader = new WKTReader();

    private static final String GEO_JSON_TYPE = "GeometryCollection";

    private static final String WKT_TYPE = "GEOMETRYCOLLECTION";


    /**
     * @author ：wwwzhqwww
     * @date ：2022/10/24 15:45
     * @parms : [wkt]
     * @modfier :
     * @modifydate :
     * @description: TODO
     */
    public static JSONObject wktToJson(String wkt) {

        String json = null;
        JSONObject jsonObject = new JSONObject();
        try {
            Geometry geometry = reader.read(wkt);
            StringWriter writer = new StringWriter();
            GeometryJSON geometryJSON = new GeometryJSON(9);
            geometryJSON.write(geometry, writer);

            json = writer.toString();
            jsonObject = JSONUtil.parseObj(json);

        } catch (Exception e) {
            log.info("--WKT转GeoJson出现异常");
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * jsonToWkt.
     *
     * @return * @return : null
     * @Author wwwzhqwww
     * @Date 10:17 2022/10/10/27
     * @Description .
     * @Modified By:
     * @params * @param null :
     */
    public static String jsonToWkt(JSONObject jsonObject) {
        String wkt = null;
        String type = jsonObject.getStr("type");
        GeometryJSON gJson = new GeometryJSON();
        try {
            // {"geometries":[{"coordinates":[4,6],"type":"Point"},{"coordinates":[[4,6],[7,10]],"type":"LineString"}],"type":"GeometryCollection"}
            if (GeoToolsUtil.GEO_JSON_TYPE.equals(type)) {
                // 由于解析上面的json语句会出现这个geometries属性没有采用以下办法
                JSONArray geometriesArray = jsonObject.getJSONArray("geometries");
                // 定义一个数组装图形对象
                int size = geometriesArray.size();
                Geometry[] geometries = new Geometry[size];
                for (int i = 0; i < size; i++) {
                    String str = geometriesArray.get(i).toString();
                    // 使用GeoUtil去读取str
                    Reader reader = GeoJSONUtil.toReader(str);
                    Geometry geometry = gJson.read(reader);
                    geometries[i] = geometry;
                }
                GeometryCollection geometryCollection = new GeometryCollection(geometries, new GeometryFactory());
                wkt = geometryCollection.toText();
            } else {
                Reader reader = GeoJSONUtil.toReader(jsonObject.toString());
                Geometry read = gJson.read(reader);
                wkt = read.toText();
            }
        } catch (IOException e) {
            log.info("--GeoJson转WKT出现异常");
            e.printStackTrace();
        }
        return wkt;
    }

    public static Geometry wktToGeom(String geoJson) {
        Geometry geometry = null;
        try {
            geometry = reader.read(geoJson);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return geometry;
    }
}
