package com.tc.personal.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.tc.personal.common.exception.ApiException;
import org.apache.poi.ss.formula.functions.T;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.ReferenceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : LuoYuHong
 * @date : 2023/4/19 17:22
 */
@Service
public class ShpUtils {
    private static final Logger logger = LoggerFactory.getLogger(ShpUtils.class);


    public List<T> readShpFile(String filepath, String filename, Class<?> clazz) throws Exception {
        //解压到指定目录
        File unzip = ZipUtil.unzip(filepath + filename, CharsetUtil.CHARSET_GBK);
        boolean noShp = true;
        List<T> ts = new ArrayList<>();
        for (File shpFile : FileUtil.ls(unzip.getPath())) {
            String suffix = shpFile.getName().substring(shpFile.getName().lastIndexOf(".") + 1).toLowerCase();
            //解析shp文件并入库
            if ("shp".equals(suffix)) {
                noShp = false;
                //getShapeFile(shpFile);
                ts.addAll(importShpFile(shpFile, clazz));
            }
        }
        if (noShp) {
            throw new ApiException("该压缩包中没有shp文件");
        }
        return ts;
    }

    private List<T> importShpFile(File file, Class<?> clazz) throws Exception {
        //String shpType = ShapeTools.getShpType(file);
        //ShapeTools<T> shapeTools = getShapeTools("ybzInfo");
        ShapeTools<T> shapeTools = new ShapeTools<>(clazz);
        return shapeTools.readShp(file, shapeTools.shpEntityBuilder(shapeTools.getClazz()));
    }

    /*private ShapeTools<T> getShapeTools(String shpType,Class clazz) {
        Map<String, ShapeTools<T>> readMap = new ConcurrentHashMap<>();
        readMap.put("ybzInfo", new ShapeTools<>(YbzInfoShpVO.class));
        return readMap.get(shpType);
    }*/

    public List<Map<String, Object>> getShapeFile(File file) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("url", file.toURI().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        //字符转码，防止中文乱码
        ShapefileDataStore shpStore = (ShapefileDataStore) dataStore;
        shpStore.setCharset(Charset.forName("GBK"));
        //获取shp文件坐标系
        SimpleFeatureSource source = shpStore.getFeatureSource();
        SimpleFeatureType schema = source.getSchema();
        Query query = new Query(schema.getTypeName());
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(query);
        List<Map<String, Object>> sqShpVOS = new ArrayList<>();

        //获取shp文件所有的地块
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();
                String name = feature.getName().toString();
                logger.info("地块名称=【{}】", name);

                //坐标系的名称
                ReferenceIdentifier referenceIdentifier = feature.getFeatureType().getCoordinateReferenceSystem().getCoordinateSystem().getName();
                String code = referenceIdentifier.getCode();
                logger.info("地块坐标系=【{}】", code);
                //获取shp文件的属性信息
                Map<String, Object> sq = new HashMap<>();
                for (Property property : feature.getValue()) {
                    sq.put(String.valueOf(property.getName()), property.getValue());
                    logger.info("地块属性名【{}】 地块属性值【{}】", property.getName(), property.getValue());
                }
                sqShpVOS.add(sq);
            }
        }
        return sqShpVOS;
    }


}
