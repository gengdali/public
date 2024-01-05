package com.tc.personal.common.util;


import cn.hutool.core.util.ZipUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tc.personal.common.exception.ApiException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;


public class ShapeUtils {

    public static final String DEF_GEOM_KEY = "the_geom";

    public static final String DEF_ENCODE = "utf-8";

    /**
     * 图形信息写入shp文件。shape文件中的geometry附带属性类型仅支持String（最大255）、Integer、Double、Boolean、Date(只包含日期，不包含时间)；
     * 附带属性的name仅支持15字符，多余的自动截取。
     *
     * @param shpPath  shape文件路径，包括shp文件名称 如：D:\data\tmp\test.shp
     * @param geomType 图形信息类型 Geometry类型，如Point.class、Polygon.class等
     * @param data     图形信息集合
     */
    public static void createShp(String shpPath, Class<?> geomType, List<Map<String, ?>> data) {
        try {
            createShp(shpPath, DEF_ENCODE, geomType, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图形信息写入shp文件。shape文件中的geometry附带属性类型仅支持String（最大255）、Integer、Double、Boolean、Date(只包含日期，不包含时间)；
     * 附带属性的name仅支持15字符，多余的自动截取。
     *
     * @param shpPath  shape文件路径，包括shp文件名称 如：D:\data\tmp\test.shp
     * @param encode   shp文件编码
     * @param geomType 图形信息类型 Geometry类型，如Point.class、Polygon.class等
     * @param data     图形信息集合
     */
    public static void createShp(String shpPath, String encode, Class<?> geomType, List<Map<String, ?>> data) {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;
        ShapefileDataStore ds = null;

        try {
            if (StringUtils.isEmpty(shpPath)) {
                throw new Exception("shp文件的路径不能为空，shpPath: " + shpPath);
            }

            if (StringUtils.isEmpty(encode)) {
                throw new Exception("shp文件的编码不能为空，encode: " + encode);
            }

            if (Objects.isNull(geomType)) {
                throw new Exception("shp文件的图形类型不能为空，geomType: " + geomType);
            }

            if (CollectionUtils.isEmpty(data)) {
                throw new Exception("shp文件的图形数据不能为空，data: " + data);
            }

            if (!data.get(0).containsKey(DEF_GEOM_KEY)) {
                throw new Exception("shp文件的图形数据中必须包含the_geom的属性，data: " + data);
            }

            //创建shape文件对象+
            File file = new File(shpPath);
            Map<String, Serializable> params = new HashMap<>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

            //定义图形信息和属性信息
            ds.createSchema(builderFeatureType(geomType, CollectionUtils.isEmpty(data) ? null : data.get(0)));

            //设置编码
            Charset charset = Charset.forName(encode);
            ds.setCharset(charset);

            //设置Writer
            writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            for (Map<String, ?> map : data) {
                //写下一条
                SimpleFeature feature = writer.next();
                for (String key : map.keySet()) {
                    if (DEF_GEOM_KEY.equals(key)) {
                        feature.setAttribute(key, map.get(key));
                    } else {
                        feature.setAttribute(key, map.get(key));
                    }
                }

            }
            writer.write();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                writer.close();
                ds.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    /**
     * 构建Feature模板
     *
     * @param geomType 图形信息类型 Geometry类型，如Point.class、Polygon.class等
     * @param data     图形信息具体的属性
     * @return featureType
     */
    public static SimpleFeatureType builderFeatureType(Class<?> geomType, Map<String, ?> data) {
        //定义图形信息和属性信息
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setCRS(DefaultGeographicCRS.WGS84);
        ftb.setName("shapefile");
        ftb.add(DEF_GEOM_KEY, geomType);

        if (MapUtils.isNotEmpty(data)) {
            for (String key : data.keySet()) {
                if (Objects.nonNull(data.get(key))) {
                    ftb.add(key, data.get(key).getClass());
                }
            }
        }
        return ftb.buildFeatureType();
    }

    /**
     * 构建属性字段
     *
     * @param geomType
     * @param data     包含属性字段类型的map
     * @return
     */
    public static SimpleFeatureType createFeatureType(Class<?> geomType, Map<String, Class> data) {
        //定义图形信息和属性信息
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setCRS(DefaultGeographicCRS.WGS84);
        ftb.setName("shapefile");

        ftb.add(DEF_GEOM_KEY, geomType);

        if (MapUtils.isNotEmpty(data)) {
            for (String key : data.keySet()) {
                if (Objects.nonNull(data.get(key))) {
                    ftb.add(key, data.get(key));
                }
            }
        }

        return ftb.buildFeatureType();
    }

    /**
     * @param shpPath        文件路径
     * @param encode         编码
     * @param geomType       空间类型
     * @param data           数据
     * @param shpInfoTypemap shp 字段属性类型
     */
    public static void createShp(String shpPath, String encode, Class<?> geomType, List<Map<String, ?>> data, Map<String, Class> shpInfoTypemap) {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;
        ShapefileDataStore ds = null;
        try {
            if (StringUtils.isEmpty(shpPath)) {
                throw new Exception("shp文件的路径不能为空，shpPath: " + shpPath);
            }

            if (StringUtils.isEmpty(encode)) {
                throw new Exception("shp文件的编码不能为空，encode: " + encode);
            }

            if (Objects.isNull(geomType)) {
                throw new Exception("shp文件的图形类型不能为空，geomType: " + geomType);
            }

            if (CollectionUtils.isEmpty(data)) {
                throw new Exception("shp文件的图形数据不能为空，data: " + data);
            }

//            if (!data.get(0).containsKey(DEF_GEOM_KEY)) {
//                throw new Exception("shp文件的图形数据中必须包含the_geom的属性，data: " + data);
//            }
            //创建shape文件对象+
            File file = new File(shpPath);
            Map<String, Serializable> params = new HashMap<>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
            //定义图形信息和属性信息
            ds.createSchema(createFeatureType(geomType, shpInfoTypemap));
            //设置编码
            Charset charset = Charset.forName("GBK");
            ds.setCharset(charset);
            //设置Writer
            writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
            for (Map<String, ?> map : data) {
                SimpleFeature feature = writer.next();
                for (String key : map.keySet()) {
                    feature.setAttribute(key, map.get(key));
                    System.out.println(map.get(key));
                }
            }
            writer.write();
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        } finally {
            try {
                writer.close();
                ds.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 压缩shape文件
     *
     * @param shpPath shape文件路径（包含shape文件名称）
     */
    public static void zipShapeFile(String shpPath) {
        File shpFile = new File(shpPath);
        String shpRoot = shpFile.getParentFile().getPath();
        String shpName = shpFile.getName().substring(0, shpFile.getName().lastIndexOf("."));
        File dbfFile = new File(shpRoot + File.separator + shpName + ".dbf");
        File prjFile = new File(shpRoot + File.separator + shpName + ".prj");
        File shpFile1 = new File(shpRoot + File.separator + shpName + ".shp");
        File shxFile = new File(shpRoot + File.separator + shpName + ".shx");
        File fixFile = new File(shpRoot + File.separator + shpName + ".fix");
        //
        File zipFile = new File(shpPath.substring(0, shpPath.lastIndexOf(".")) + ".zip");
        ZipUtil.zip(zipFile, false, dbfFile, prjFile, shxFile, shpFile1, fixFile);
    }

    /**
     * 实体类转map通用方法
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class<?> aClass = obj.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        try {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                map.put(declaredField.getName(), declaredField.get(obj));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 实体类转map通用方法 value放的是 实体类类型
     *
     * @param obj
     * @return
     */
    public static Map<String, Class> objectMap(Object obj) {
        Map<String, Class> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class<?> aClass = obj.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            map.put(declaredField.getName(), declaredField.getType());
        }

        return map;
    }

    /**
     * shpInfo 封装
     *
     * @param prjMsgDTO
     * @param shpInfo         附属表信息实体类
     * @param geoJson
     * @param dataShpInfoList
     */
    public static void shapeInfoBuilder(Object prjMsgDTO, Object shpInfo, String geoJson, List<Map<String, ?>> dataShpInfoList) {
        try {
            Geometry geometry = com.tc.personal.common.util.GeoToolsUtil.wktToGeom(geoJson);
            Map<String, Object> map = ShapeUtils.object2Map(prjMsgDTO);
            Map<String, Object> mapDTO = ShapeUtils.object2Map(shpInfo);
            //删除可能 造成导入错误的数据
            mapDTO.remove("geoJson");
            //添加空间数据
            map.put(ShapeUtils.DEF_GEOM_KEY, geometry);
            //合并map
            map.putAll(mapDTO);
            dataShpInfoList.add(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("空间坐标解析失败");
        }
    }


    /**
     * value 为 shp属性的 类型 在shp中占位
     *
     * @param prjMsgDTO 主表需要实体
     * @param shpInfo   副表信息
     * @return
     */
    public static Map<String, Class> getShpInfoType(Object prjMsgDTO, Object shpInfo) {
        Map<String, Class> map = ShapeUtils.objectMap(prjMsgDTO);
        Map<String, Class> mapDTO = ShapeUtils.objectMap(shpInfo);
        mapDTO.remove("geoJson");
        map.putAll(mapDTO);
        return map;
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    public static <T> T objParse(Class<T> returnClass, Object value) {
        try {
            if (value.getClass().equals(returnClass)) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.convertValue(value, returnClass);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

}


