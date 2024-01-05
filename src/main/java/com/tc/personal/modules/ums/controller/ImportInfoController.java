package com.tc.personal.modules.ums.controller;

import com.tc.personal.common.api.CommonResult;
import com.tc.personal.common.util.GeometryCreatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author luoyuhong
 * @Date 2022/3/30
 * @Description excel 导入
 */
@RestController
@RequestMapping(value = "/import")
@Api(tags = "导入")
public class ImportInfoController {


    /**
     * 创建多线
     *
     * @return
     */
    @ApiOperation("创建多线")
    @PostMapping(value = "/creatGeometry")
    public CommonResult creatGeometry(@RequestBody String wkt) {
        GeometryCreatorUtils geometryCreatorUtils = new GeometryCreatorUtils();
        try {
            MultiLineString mLineByWKT = geometryCreatorUtils.createMLineByWKT(wkt);
            System.out.println(mLineByWKT);
            return CommonResult.success("成功");
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonResult.failed("");
        }
    }

    /**
     * 创建多面
     *
     * @return
     */
    @ApiOperation("创建多面")
    @PostMapping(value = "/creatMultiPolygon")
    public CommonResult creatMultiPolygon(@RequestBody String wkt) {
        GeometryCreatorUtils geometryCreatorUtils = new GeometryCreatorUtils();
        try {
            MultiPolygon mulPolygonByWKT = geometryCreatorUtils.createMulPolygonByWKT(wkt);
            return CommonResult.success(mulPolygonByWKT);
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonResult.failed();
        }
    }

    /**
     * 创建多点
     *
     * @return
     */
    @ApiOperation("创建多点")
    @PostMapping(value = "/creatMultiPoint")
    public CommonResult creatMultiPoint(@RequestBody String wkt) {
        GeometryCreatorUtils geometryCreatorUtils = new GeometryCreatorUtils();
        try {
            MultiLineString mLineByWKT = geometryCreatorUtils.createMLineByWKT(wkt);
            return CommonResult.success(mLineByWKT);
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonResult.failed();
        }
    }

    /**
     * 导入项目数据
     *
     * @return
     */
    /*@ApiLog(title = "导入Kml项目数据", action = "导入")
    @ApiOperation("导入Kml项目数据（备用）")
    @PostMapping(value = "/importKml", consumes = "multipart/form-data")
    public CommonResult importKml(MultipartFile file) throws MalformedURLException {
        String filepath = Constant.FILE_SHOW_URL + Constant.TEMP_UPLOAD_SHP_PATH;
        //1.上传文件到本地
        FileUtil.upload(file, filepath);
        String filename = file.getOriginalFilename();
        File kmlFile = new File(filepath + filename);
        List<ShpInfoVO> result = new ArrayList<>();
        if (!filename.endsWith(".kml")) {
            throw new ApiException("文件格式错误！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("url", kmlFile.toURI().toURL());
        Map<String, Object> map = new HashMap<>();
        try {
            PullParser parser = new PullParser(new KMLConfiguration(), file.getInputStream(), SimpleFeature.class);
            FeatureJSON fjson = new FeatureJSON();
            SimpleFeature simpleFeature = (SimpleFeature) parser.parse();
            StringWriter sw = new StringWriter();
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
            ObjectMapper objectMapper = new ObjectMapper();
            map = objectMapper.readValue(sw.toString(), Map.class);

        } catch (Exception e) {
            return null;
        }

        return CommonResult.success(map);
    }*/
}
