package utils

interface FileDownloader {
    suspend fun download(
        url: String,
        fileName: String
    )
}