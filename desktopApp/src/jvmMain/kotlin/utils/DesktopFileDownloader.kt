import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import utils.FileDownloader
import java.io.File
import java.nio.file.Paths

class DesktopFileDownloader(
    private val client: HttpClient
) : FileDownloader {

    override suspend fun download(url: String, fileName: String) {
        val bytes = client.get(url).body<ByteArray>()

        val downloadsDir = Paths.get(
            System.getProperty("user.home"),
            "Downloads"
        ).toFile()

        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }

        File(downloadsDir, fileName).writeBytes(bytes)
    }
}
