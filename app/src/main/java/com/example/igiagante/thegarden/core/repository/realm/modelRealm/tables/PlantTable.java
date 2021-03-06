package com.example.igiagante.thegarden.core.repository.realm.modelRealm.tables;

/**
 * @author Ignacio Giagante, on 26/4/16.
 */

public interface PlantTable extends Table {

    String GARDEN_ID = "gardenId";
    String SEED_DATE = "seedDate";
    String NAME = "name";
    String EC_SOIL = "ecSoil";
    String PH_SOIL = "phSoil";
    String FLOWERING_TIME = "floweringTime";
    String GENOTYPE = "genotype";
    String SIZE = "size";
    String HARVEST = "harvest";
    String DESCRIPTION = "description";
    String FLAVORS = "flavors";
    String ATTRIBUTES = "attributes";
    String PLAGUES = "plagues";

    // this attribute is only to check if some image should be deleted
    String RESOURCES_IDS =  "resourcesIds";

    interface Image {
        String ID = "id";
        String NAME = "name";
        String TYPE = "type";
        String SIZE = "size";
        String URL = "url";
        String THUMBURL = "thumbUrl";
        String MAIN = "main";
    }

    interface Attribute {
        String TYPE = "type";
    }

    interface AttributePerPlant {
        String ATTRIBUTE_ID = "attributeId";
        String PERCENTAGE = "percentage";
    }

    interface Plague {
        String IMAGE_URL = "imageUrl";
    }
}
