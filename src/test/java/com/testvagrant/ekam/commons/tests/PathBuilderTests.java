package com.testvagrant.ekam.commons.tests;

import com.google.gson.reflect.TypeToken;
import com.testvagrant.ekam.commons.io.FileFinder;
import com.testvagrant.ekam.commons.io.GsonParser;
import com.testvagrant.ekam.commons.io.ResourcePaths;
import com.testvagrant.ekam.commons.os.OsUtils;
import com.testvagrant.ekam.commons.path.PathBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.io.File;
import java.io.FileDescriptor;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

public class PathBuilderTests {
    @Test
    public void verifyPath() {
        PathBuilder path = new PathBuilder("User").append("test").append("screenshot");

        System.out.println(path);

        Path destinationPath =
                new File(
                        path.append("2021-11-08T22:34:43.721552400" + ".png")
                                .toString())
                        .toPath();

        System.out.println(destinationPath);

        String expected = "User"+File.separator.charAt(0) +"test"+ File.separator.charAt(0) + "screenshot" +File.separator.charAt(0)+"2021-11-08T22-34-43.721552400.png";

        System.out.println("expected "+expected);

        Assertions.assertEquals(expected.toString(), destinationPath.toString());
    }
}
