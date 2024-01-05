package com.tc.personal.common.util;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据shp直接生成图片文件
 */
public class ShpToImage {

    public static void main(String[] args) throws Exception {
        // 读取shp文件
        File file = new File("/Users/wuchao/Documents/test.shp");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("url", file.toURI().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(params);
        FeatureSource featureSource = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);

        // 为地图创建边界框
        ReferencedEnvelope mapBounds = featureSource.getBounds();

        // 创建地图上下文 定义样式
        MapContent map = new MapContent();
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        FeatureLayer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);

        // 创建渲染器
        GTRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);

        // 设置图片大小并创建缓冲图像
        int imageWidth = 1024;
        int imageHeight = 768;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        // 创建图形对象并设置背景色
        Graphics2D graphics = image.createGraphics();
        graphics.setBackground(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);

        // 设置地图区域并渲染地图
        ReferencedEnvelope envelope = new ReferencedEnvelope(mapBounds);
        envelope.expandBy(0.5 * envelope.getWidth(), 0.5 * envelope.getHeight());
        renderer.setMapContent(map);
        renderer.paint(graphics, image.getData().getBounds(), envelope);

        // 保存图片进文件
        File outputfile = new File("/Users/wuchao/Desktop/data/output.png");
        ImageIO.write(image, "png", outputfile);

        // Clean up
        graphics.dispose();
        map.dispose();
        dataStore.dispose();
    }
}
