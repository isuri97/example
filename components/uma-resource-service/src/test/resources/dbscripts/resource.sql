
CREATE TABLE IF NOT EXISTS IDN_RESOURCE(
  ID INTEGER NOT NULL AUTO_INCREMENT,
  RESOURCE_ID VARCHAR(255),
  RESOURCE_NAME VARCHAR(255),
  TIME_CREATED TIMESTAMP DEFAULT '0',
  RESOUCE_OWNER_ID VARCHAR(255),
  TENANT_ID INTEGER,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS IDN_RESOURCE_META_DATA (
  RESOURCE_ID VARCHAR(255) NOT NULL,
  PROPERTY_KEY VARCHAR(255),
  PROPERTY_VALUE VARCHAR(255),
  PRIMARY KEY (RESOURCE_ID),
  FOREIGN KEY (RESOURCE_ID) REFERENCES IDN_RESOURCE(RESOURCE_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS IDN_SCOPE
(
    SCOPE_ID INTEGER NOT NULL AUTO_INCREMENT,
    RESOURCE_ID   VARCHAR (255),
    NAME VARCHAR (255),
    DESCRIPTION VARCHAR (255),
    TENANT_ID INTEGER ,
    PRIMARY KEY (SCOPE_ID),
    FOREIGN KEY (RESOURCE_ID) REFERENCES IDN_RESOURCE_META_DATA(RESOURCE_ID) ON DELETE CASCADE
);