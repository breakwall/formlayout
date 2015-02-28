#!/usr/bin/env ruby
require 'net/http'

host = Net::HTTP.new("socadmin.verigy.net") # /nexus/content/repositories/eclipse-update-site/
files_to_download  = []

def parseLink(host, ll, files_to_download)
  if ll =~ /http:\/\/socadmin.verigy.net(.+)/
    ll = $1
  else
    p "error"
    exit 0
  end

  res = host.get(ll,nil)

  if res.message == "OK"
    res.body.split("\n").each{|line|
      if line =~ /<a href="(.+)">(.+)<\/a>/
        link = $1
        name = $2
        if name.eql?("Parent Directory")
          next
        end

        if name[-1, 1].eql?("/")
          #dir
          parseLink(host, link, files_to_download)
        else
          #file
          if link =~ /http:\/\/socadmin.verigy.net\/nexus\/content\/(.+)/
            name = $1
          end
          p "#{name} => #{link}"
          files_to_download << [name, link]
        end
      end
    }
  end
end

def mkdirs(dirname)
  if not File.exist?(dirname)
    `mkdir -p #{dirname}`
  end
end

parseLink(host, "http://socadmin.verigy.net/nexus/content/repositories/eclipse-update-site/",files_to_download)

require 'open-uri'

files_to_download.each{|name, link|
  open(link) do |fin|
    size = fin.size
    download_size = 0
#    puts "size: #{size} byte"
    fileName = File.expand_path(name, ".")
    if File.exist?(fileName) && File.size(fileName) == size
      p fileName + " is already download."
      next
    end
    mkdirs(File.dirname(fileName))
    File.open(fileName, "w+"){|fout|
      progress_head = "...#{name.split(//).last(50).to_s} ["
      while buf = fin.read(1024) do
        fout.write buf
        download_size += buf.size

        progs = download_size * 100 / size
        progress_number = "%3d" % progs
        progress_bar = "#" * (progs / 5) + "." * (20 - progs / 5)

        print "\r"
        print progress_head + progress_bar + "]" + "#{progress_number}%"
        $stdout.flush
        sleep 0.05
      end
    }
    puts
  end
}