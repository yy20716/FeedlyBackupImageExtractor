# FeedlyBackupImageExtractor

[Feedly](https://feedly.com) (a news aggregator application) has a feature that automatically backs up webpage files into Dropbox directories when Pro users mark the webpages in feeds as "Read Later". However, one limitation is that this automatic backup feature for "Read Later" does not backup images of the webpages in the Dropbox directory. Therefore, some images in stored webpages are often lost when the original webpages are not available anymore. To alleviate this issue, we built a simple program that (i) extracts image links from the html files in the back-up directory of Feedly and (ii) crawls and downloads corresponding images to a specific directory in a batch-oriented manner.

# Quickstart
Running `mvn package` does a compile and creates the target directory, including a jar as follows.
```
yy20716:~/workspace/feedlyImageExtractor$ mvn clean --quiet
yy20716:~/workspace/feedlyImageExtractor$ mvn package > /dev/null
```
Before trying to execute the jar file, configure the backup directory of Dropbox directory as inputPath and output directory that contains images as outputDirectory
```
[main]
inputPath = /home/yy20716/Dropbox/Apps/Feedly Vault/Saved For Later/2017
outputPath = /home/hadoop/backup
```
Then execute jar file as follows.
```
yy20716:~/workspace/feedlyImageExtractor$ java -jar target/feedlyImageExtractor.jar
2017-04-02 01:36:19 INFO  ImageExtractor:47 - Downloading image: https://jalainacarey.tumblr.com/image/156294117176
...
```

# Contributors
[HyeongSik Kim](https://www.linkedin.com/in/hskim0/)
