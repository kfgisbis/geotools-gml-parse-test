When parsing a xsd file in gml3 format, all received objects will have empty geometry if at least one 
geometry in the database is empty.

```xml

<wfs:FeatureCollection
        xmlns:te="gisbis" xmlns:wfs="http://www.opengis.net/wfs"
        xmlns:gml="http://www.opengis.net/gml"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        numberOfFeatures="0" timeStamp="2024-07-12T14:38:11.749Z"
        xsi:schemaLocation="http://www.opengis.net/wfs http://127.0.0.1:8083/geoserver/schemas/wfs/1.1.0/wfs.xsd gisbis">
    <gml:featureMembers>
        <te:test_table gml:id="test_table.1">
            <te:desc>empty geom</te:desc>
        </te:test_table>
        <te:test_table gml:id="test_table.2">
            <te:geom>
                <gml:Point srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
                    <gml:pos>11 48</gml:pos>
                </gml:Point>
            </te:geom>
            <te:desc>has geom</te:desc>
        </te:test_table>
    </gml:featureMembers>
</wfs:FeatureCollection>
```

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("List size : " + getCountGeometry("gml.xsd"));
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
```

**RESULT:**<br/>
```console
GEOMETRY NULL : test_table.1
GEOMETRY NULL : test_table.2
List size : 0
```

All objects in SimpleFeatureCollection have empty geometry.
How can I solve this? Are there any solutions?