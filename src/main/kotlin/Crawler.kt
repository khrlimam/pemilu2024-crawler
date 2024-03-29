package proj.internal

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import me.tongfei.progressbar.ProgressBar
import proj.internal.pojo.Hhcw
import java.nio.file.Path

typealias UpdateProgressHint = (Long) -> Unit

class Crawler(val datasetPath: Path, dataSize: Long = 0L, worker: Int = 1) {

    private val WILAYAH_PARTITION = intArrayOf(2, 4, 6, 10, 13)
    private fun kodeToPath(vararg kode: String): String = kode.joinToString("/")
    private val pb = ProgressBar("Mengunduh Data TPS", dataSize)

    private fun partKodeTps(kodeTps: String): List<String> {
        return WILAYAH_PARTITION.map { kodeTps.substring(0, it) }
    }

    private suspend fun stepProgressBar() = coroutineScope {
        launch {
            pb.step()
        }
    }

    private suspend fun updateMaxHint(newMax: Long) = coroutineScope {
        launch { pb.maxHint(pb.max + newMax) }
    }

    suspend fun crawl(vararg kode: String = arrayOf("0")) {
        coroutineScope {
            val res = KpuService.req.wilayah(kodeToPath(*kode)).body()
            res?.let { wilayahs ->
                wilayahs.forEach {
                    if (it.tingkat == 5) {
                        updateMaxHint(1L)
                        launch { getSuaraAndWriteToCsv(it.kode) }
                    } else launch {
                        if (it.tingkat == 1) crawl(it.kode)
                        else crawl(*kode, it.kode)
                    }
                }
            }
        }
    }

    suspend fun getSuaraAndWriteToCsv(tps: String) {
        reqAndWriteSuara(*partKodeTps(tps).toTypedArray())
        stepProgressBar()
    }

    private suspend fun reqAndWriteSuara(vararg kode: String) = coroutineScope {
        launch {
            val resp = KpuService.req.suaraTps(kodeToPath(*kode))
            writeSuaraToCsv(resp.body(), kode.last())
        }
    }

    private suspend fun writeSuaraToCsv(body: Hhcw?, kodeTps: String) = coroutineScope {
        launch { body?.writeToCsv(datasetPath, kodeTps) }
    }

}