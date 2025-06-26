# Selector-Tool

Tool for personal use. Makes a list of files and their sizes.

## TV Series Lister and MovieLister

`TvSeriesLister` and `MovieLister` generate CSV spreadsheets listing your
media collection. Each row of the output file contains:

1. **Title** – the folder name (TV) or file name (movies)
2. **Size (bytes)** – the total size
3. **Watched** – a column of unchecked boxes (represented by `FALSE`)

Both classes share logic via the `BaseLister` class and can be invoked through
the `ListerApp` entry point.

### Build and run

Compile all sources with `javac`:

```bash
javac src/*.java
```

Run the tool in either `tv` or `movie` mode:

*For TV mode, provide one or more parent directories that contain your TV show
folders. The lister will scan only the immediate subdirectories of each parent
folder.*

*For movie mode, provide one or more directories. Every file inside each
directory (recursively) will be listed.*

```bash
java -cp src ListerApp tv /path/to/Show1 /path/to/Show2
java -cp src ListerApp movie /path/to/Movie1 /path/to/Movie2
```

The CSV file name is determined by the mode (`tv_series_list.csv` or
`movie_list.csv`).

### Graphical interface

To use a simple Swing-based UI instead of the command line, launch
`ListerUI`:

```bash
java -cp src ListerUI
```

Choose a folder and whether it contains TV shows or movies, then click
**Generate CSV**.
