import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.wfs.GML;
import org.locationtech.jts.geom.Geometry;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Elements without empty geometries, list size : " + getCountGeometry("gml.xsd"));
        System.out.println("Elements with empty geometries, list size : " + getCountGeometry("gml-with-empty.xsd"));
    }

    private static int getCountGeometry(String xsdName) {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(xsdName)) {
            SimpleFeatureCollection features = read(is);

            return getGeometry(features).size();

        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Geometry> getGeometry(SimpleFeatureCollection features) {

        try (SimpleFeatureIterator itr = features.features()) {
            List<Geometry> response = new ArrayList<>();
            while (itr.hasNext()) {
                SimpleFeature feature = itr.next();
                if (feature.getDefaultGeometry() != null) {

                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    response.add(geometry);
                } else {
                    System.out.println("GEOMETRY NULL : " + feature.getID());
                }
            }

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SimpleFeatureCollection read(InputStream inputStream) throws IOException, ParserConfigurationException, SAXException {
        GML gml = new GML(GML.Version.GML3);
        return gml.decodeFeatureCollection(inputStream);
    }

}
