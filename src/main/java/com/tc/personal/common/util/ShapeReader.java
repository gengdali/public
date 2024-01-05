package com.tc.personal.common.util;


import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @program: commons
 * @description: 读取Shp文件
 * @author: Mr.superbeyone
 * @create: 2018-09-19 15:26
 **/
public class ShapeReader {

    public List<ShapeModel> readShapeFile(String filePath) {
        List modelList = new ArrayList<ShapeModel>();
        File folder = new File(filePath);
        if (!folder.isDirectory()) {
            if (folder.toString().endsWith(".shp")) {
                try {
                    modelList = getShapeFile(folder);
                    return modelList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("选择的文件后缀名不是.shp");
            }
        } else {
            File[] files = folder.listFiles();
            if (!(files.length > 0)) {
                System.out.println("目录文件为空");
                return modelList;
            }

            for (File file : files) {
                if (!file.toString().endsWith(".shp")) {
                    continue;
                }
                try {
                    List<ShapeModel> models = getShapeFile(file);
                    modelList.addAll(models);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return modelList;
    }

    private List<ShapeModel> getShapeFile(File file) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", file.toURI().toURL());
        List<ShapeModel> models = new ArrayList<ShapeModel>();
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        //字符转码，防止中文乱码
        ((ShapefileDataStore) dataStore).setCharset(Charset.forName("utf8"));
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();
        FeatureIterator<SimpleFeature> features = collection.features();
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            ShapeModel model = new ShapeModel();
            Iterator<? extends Property> iterator = feature.getValue().iterator();
            while (iterator.hasNext()) {
                Property property = iterator.next();
                //property数据与实体类对应
                if (property.getName().toString().equals("PrjID"))
                    model.setLayer(property.getValue().toString());
                if (property.getName().toString().equals("the_geom"))
                    model.setTime(property.getValue().toString());
                model.setCreateTime(new Date());
                model.setClazz("");
            }
            models.add(model);
        }
        return models;
    }

}

