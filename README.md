# VES OpenApi Manager

## Description
This application should partially validate incoming service distributions in SDC. It validates each artifact of type
VES_EVENT. Its purpose is to check whether schemaReferences of stndDefined events included in VES_EVENT artifacts are
matching the schemas which VES Collector contains.