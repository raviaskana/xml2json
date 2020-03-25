<?xml version="1.0" encoding="UTF-8"?>
    <loc xsl:version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xslt/java"
         exclude-result-prefixes="java">
        <xsl:for-each select="incident/ti/ev">
            <locations>
                <_id><xsl:value-of select="id"/></_id>
                <description><xsl:value-of select="text"/></description>
                <severity><xsl:value-of select="se"/></severity>
                <eventCode><xsl:value-of select="ec"/></eventCode>
                <validStart><xsl:value-of select="valid/@start"/></validStart>
                <validEnd><xsl:value-of select="valid/@end"/></validEnd>
                <type>"TrafficIncident"</type>
                <lastUpdated><xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('yyyy-MM-ddHH:mm:ss'), java:java.util.Date.new())" /></lastUpdated>
                <xsl:element name="{loc/@type}">
                    <xsl:if test="(loc/@type = 'geo')"><coordinates> <lon><xsl:value-of select="loc/geo/@lon"/></lon><lat><xsl:value-of select="loc/geo/@lat"/></lat></coordinates></xsl:if>
                    <xsl:if test="(loc/@type = 'tmc')"><table><xsl:value-of select="/incident/ti/@table"/></table><id><xsl:value-of select="loc/start/@id"/></id><direction><xsl:value-of select="loc/start/@dir"/></direction></xsl:if>
                </xsl:element>
            </locations>
        </xsl:for-each>
    </loc>