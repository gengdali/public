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
import org.postgis.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.SQLException;


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

    /**
     * 将空间坐标类型数据转为几何类对象
     *
     * @param geometryString
     * @return
     * @throws SQLException
     */
    public static org.postgis.Geometry dataToGeometry(String geometryString) throws SQLException {
        // 假设从数据库中获取到的Geometry对象
        PGgeometry pgGeometry = new PGgeometry(geometryString);
        org.postgis.Geometry geometry = pgGeometry.getGeometry();
        // 根据 Geometry 类型进行相应的转换
        if (geometry instanceof Point) {
            // 将Geometry对象转换为Java中的Point对象
            Point point = (Point) pgGeometry.getGeometry();
            // 输出Point对象的坐标
            log.info("Point: " + point.getX() + ", " + point.getY());
            return point;
        } else if (geometry instanceof MultiPoint) {
            MultiPoint multiPoint = (MultiPoint) geometry;
            // 处理多点
            Point[] points = new Point[multiPoint.numPoints()];
            /*for (int i = 0; i < multiPoint.numPoints(); i++) {
                Point point = multiPoint.getPoint(i);
                Coordinate coordinate = new Coordinate(point.getX(), point.getY());
                points[i] = new GeometryFactory().createPoint(coordinate);
            }
            MultiPoint jtsMultiPoint = new GeometryFactory().createMultiPoint(points);*/
            log.info("MultiPoint: " + points.toString());
            return multiPoint;
        } else if (geometry instanceof LineString) {
            // 将Geometry对象转换为Java中的LineString对象
            LineString lineString = (LineString) pgGeometry.getGeometry();
            // 输出LineString对象的坐标
            log.info("LineString: " + lineString.getPoint(0).getX() + ", " + lineString.getPoint(0).getY());
            return lineString;
        } else if (geometry instanceof MultiLineString) {
            MultiLineString multiLineString = (MultiLineString) geometry;
            // 处理多线
            for (int i = 0; i < multiLineString.numLines(); i++) {
                org.postgis.Geometry line = multiLineString.getLine(i);
                // 在这里进行多线的处理
                log.info("Line " + (i + 1) + ": " + line.toString());
            }
            return multiLineString;
        } else if (geometry instanceof Polygon) {
            // 将Geometry对象转换为Java中的Polygon对象
            Polygon polygon = (Polygon) pgGeometry.getGeometry();
            // 输出Polygon对象的坐标
            for (int i = 0; i < polygon.numRings(); i++) {
                log.info("Polygon Ring " + i + ":");
                for (int j = 0; j < polygon.getRing(i).numPoints(); j++) {
                    log.info(polygon.getRing(i).getPoint(j).getX() + ", " + polygon.getRing(i).getPoint(j).getY());
                }
            }
            return polygon;
        } else if (geometry instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) geometry;
            // 处理多面
            for (int i = 0; i < multiPolygon.numPolygons(); i++) {
                Polygon polygon = multiPolygon.getPolygon(i);
                // 在这里进行多面的处理
                log.info("Polygon " + (i + 1) + ": " + polygon.toString());

                // 处理多面的外环和内环
                LinearRing outerRing = polygon.getRing(0);
                log.info("Outer Ring: " + outerRing.toString());

                for (int j = 1; j < polygon.numRings(); j++) {
                    LinearRing innerRing = polygon.getRing(j);
                    log.info("Inner Ring " + j + ": " + innerRing.toString());
                }
            }
            return multiPolygon;
        }
        return geometry;
    }
}
