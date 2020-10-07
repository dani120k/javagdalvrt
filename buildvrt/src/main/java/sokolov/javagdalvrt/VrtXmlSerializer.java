package sokolov.javagdalvrt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import sokolov.model.datasets.VrtDataset;

public class VrtXmlSerializer {
    public String serializeToXml(VrtDataset vrtDataset) throws Exception{
        ObjectMapper objectMapper = new XmlMapper();

        String serializedXml = objectMapper.writeValueAsString(vrtDataset);

        objectMapper.writeValueAsString(vrtDataset);

        return serializedXml;
    }
}
