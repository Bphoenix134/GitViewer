package com.myapplication.utils

import android.content.Context
import android.os.Environment
import utils.FileDownloader
import java.io.File
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class AndroidFileDownloader(
    private val context: Context,
    private val client: HttpClient
) : FileDownloader {

    override suspend fun download(url: String, fileName: String) {
        val bytes = client.get(url).body<ByteArray>()

        val dir = context.getExternalFilesDir(
            Environment.DIRECTORY_DOWNLOADS
        ) ?: context.filesDir

        File(dir, fileName).writeBytes(bytes)
    }
}