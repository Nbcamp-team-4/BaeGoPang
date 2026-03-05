package com._team._project.domain.region.util;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public final class RegionGeometryUtil {

    private RegionGeometryUtil() {}

    public static MultiPolygon toMultiPolygon(String wkt) {
        try {
            Geometry geometry = new WKTReader().read(wkt);
            geometry.setSRID(4326);

            if (geometry instanceof MultiPolygon multiPolygon) {
                return multiPolygon;
            }
            if (geometry instanceof Polygon polygon) {
                // POLYGON으로 들어오면 MULTIPOLYGON으로 감싸서 변환
                return polygon.getFactory().createMultiPolygon(new Polygon[]{polygon});
            }

            throw new IllegalArgumentException("geomWkt must be POLYGON or MULTIPOLYGON");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid geomWkt");
        }
    }
}