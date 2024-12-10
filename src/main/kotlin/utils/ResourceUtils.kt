package utils

import java.io.File

object ResourceUtils {
    fun getResourceAsFile(resourceName: String): File {
        var resourceURI = ClassLoader.getSystemResource(resourceName);
        return File(resourceURI.file)
    }
}