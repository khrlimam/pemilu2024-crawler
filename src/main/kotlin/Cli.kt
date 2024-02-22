package proj.internal

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.cooccurring
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.*
import java.io.File
import java.nio.file.Paths

const val ITERATIONS = 823_235L

sealed class DownloadMethod(name: String) : OptionGroup(name)

class TpsOptions : DownloadMethod("Download TPS dari file daftar kode TPS") {
    val tpsListFile by option("-tps").required()
        .help("Lokasi file daftar kode tps yang ingin didownload, setiap kode tps dipisah dengan baris baru")

    val worker by option("-w", "--worker")
        .int()
        .default(1)
        .help("Jumlah proses download yang diinginkan.")
        .validate { require(it > 0) }
}

class CrawlOptions : DownloadMethod("Crawl data TPS") {
    val crawlAll: Boolean by option("--crawl")
        .flag(default = false)
        .help("Gunakan flag ini jika ingin crawl semua data TPS")

    val wilayah by option("--wilayah")
        .split("/")
        .default(listOf("0"))
        .help("Crawl data tps pada wilayah. Parameter wilayah dipisah dengan \"/\". Untuk memudahkan bisa ambil dari url di website pemilu2024.kpu.go.id. Kedalaman maksimal 5 (TPS)")
        .validate { require(it.size <= 5) }

}

class Cli : CliktCommand(printHelpOnEmptyArgs = true) {

    val outputDir: String by option("-l", "--loc")
        .default("dataset").help("Lokasi folder dataset")

    val datasetName: String by option("-n", "--name")
        .default(currentDatasetName)
        .help("Nama dataset, default yyyy-MM-dd HH:mm.csv")

    val crawlOptions by CrawlOptions()

    val tpsOptions by TpsOptions().cooccurring()

    override fun run(): Unit = runBlocking {
        crawlOptions.apply {
            if (crawlAll) {
                initDataset(this@Cli.outputDir, this@Cli.datasetName)
                val crawler = Crawler(Paths.get(this@Cli.outputDir, this@Cli.datasetName))
                launch {
                    if (wilayah.size < 5) {
                        crawler.crawl(*wilayah.toTypedArray())
                    } else {
                        crawler.getSuaraAndWriteToCsv(wilayah.last())
                    }
                }
            }
        }

        tpsOptions?.apply {
            initDataset(this@Cli.outputDir, this@Cli.datasetName)
            val lines = File(tpsListFile).bufferedReader().readLines()
            val chunked = lines.chunked(lines.size / worker)
            val crawler = Crawler(
                Paths.get(this@Cli.outputDir, this@Cli.datasetName),
                lines.size.toLong()
            )
            chunked.forEach {
                launch {
                    it.forEach {
                        crawler.getSuaraAndWriteToCsv(it)
                    }
                }
            }
        }
    }
}