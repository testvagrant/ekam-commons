package com.testvagrant.ekam.commons.tests;

import com.testvagrant.ekam.commons.os.OsUtils;
import com.testvagrant.ekam.commons.path.PathBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

public class PathBuilderTests {
    @Test
    public void verifyFileOrFolderPath() {
        String expected;

        PathBuilder path = new PathBuilder("User").append("test").append("screenshot");

        Path destinationPath =
                new File(
                        path.append("2021-11-08T22:34:43.721552400" + ".png")
                                .toString())
                        .toPath();

        if (OsUtils.isWindows())
            expected = "User" + File.separator.charAt(0) + "test" + File.separator.charAt(0) + "screenshot" + File.separator.charAt(0) + "2021-11-08T22-34-43.721552400.png";
        else
            expected = "User" + File.separator.charAt(0) + "test" + File.separator.charAt(0) + "screenshot" + File.separator.charAt(0) + "2021-11-08T22:34:43.721552400.png";

        Assertions.assertEquals(expected, destinationPath.toString());
    }
}