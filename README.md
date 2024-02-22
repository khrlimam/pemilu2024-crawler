This is the source code for TPS Crawler in kotlin. Built jar available [here](https://github.com/khrlimam/pemilu2024-crawler/releases/tag/v1.0)

## Usage

```bash
java -cp pemilu2024.main.jar proj.internal.MainKt --<options>
```

### To crawl all tps
```bash
java -cp pemilu2024.main.jar proj.internal.MainKt --crawl
```

### To crawl only wilayah
```bash
java -cp pemilu2024.main.jar proj.internal.MainKt --crawl --wilayah 11
```

where 11 is the wilayah code. This parameter is separated by "/" for nested wilayah e.g provinsi/kabupaten/kecamatan/lurah/tps max 5 level.

For example, to download all tps in kabupaten BIAK NUMFOR Papua, then:
```bash
java -cp pemilu2024.main.jar proj.internal.MainKt --crawl --wilayah 91/9106
```
This wilayah code can easily obtained from Info Publik Pemilu 2024 [web page](https://pemilu2024.kpu.go.id).

### To download TPS from list of tps codes
```bash
java -cp pemilu2024.main.jar proj.internal.MainKt -tps ~/workspaces/pemilu2024/input/tps-anomali.txt -n anomali.csv --worker 1000
```

For other argument can use -h