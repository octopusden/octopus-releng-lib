package org.octopusden.octopus.releng;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static String getJson(InputStream resourceAsStream) throws IOException {
        return IOUtils.toString(
                resourceAsStream,
                "UTF-8"
        );
    }

}
