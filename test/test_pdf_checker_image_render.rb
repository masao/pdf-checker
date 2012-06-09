#!/usr/bin/env ruby
# -*- coding: utf-8 -*-
# $Id: test_springer.rb,v 1.4 2011/03/06 06:59:25 masao Exp $

require 'test/unit'
# require 'ftools'
require "open3"
require "open-uri"

class TestPdfChecker < Test::Unit::TestCase
   module Util
      class PdfFile
         attr_reader :filename
         def initialize( url, basedir = ".", filename = nil )
            uri = URI.parse( url )
            if filename.nil?
               filename = File.basename( uri.path )
            end
            @filename = File.join( basedir, filename )
            if not File.exist?( @filename )
               open( @filename, "w" ){|out|
                  out.print open( url ){|io| io.read }
               }
            end
         end
      end
   end

   JARFILE = "PdfChecker.jar"
   TEST_PDF_LIST = [
      {  # KURENAI: http://hdl.handle.net/2433/154750
         :url => "http://repository.kulib.kyoto-u.ac.jp/dspace/bitstream/2433/154750/1/sosyo31_contents.pdf",
         :dpi => 300,
      },
      {  #OUKA: http://ir.library.osaka-u.ac.jp/meta-bin/mt-pdetail.cgi?smode=1&edm=0&tlang=1&cd=00030808
         :url => "http://ir.library.osaka-u.ac.jp/metadb/up/LIBOJMK01/ojm01_01_b.pdf",
         :dpi => 600,
      },
      {  #OUKA: http://ir.library.osaka-u.ac.jp/meta-bin/mt-pdetail.cgi?smode=1&edm=0&tlang=1&cd=00041994
         ### pdf-checker-20110914 returns java.lang.ArrayIndexOutOfBoundsException for this pdf:
         :url => "http://ir.library.osaka-u.ac.jp/metadb/up/LIBCLK002/lw_63_3_254.pdf",
      },
      {  # Tulips: http://hdl.handle.net/2241/116687
         :url => "http://www.tulips.tsukuba.ac.jp/dspace/bitstream/2241/116687/1/%e7%ac%ac%e4%b8%89%e5%ad%a6%e7%be%a420%e5%91%a8%e5%b9%b4%e8%aa%8c.pdf",
         :dpi => 400,
         :coverpage => true,
      },
      {  # HUSCAP: http://hdl.handle.net/2115/24400
         :url => "http://eprints.lib.hokudai.ac.jp/dspace/bitstream/2115/24400/1/5_Kantou.pdf",
         :dpi => 300,
         :coverpage => true,
      },
      {  # Teapot: http://hdl.handle.net/10083/3535
         :url => "http://teapot.lib.ocha.ac.jp/ocha/bitstream/10083/3535/1/KJ00004831315.pdf",
         :dpi => 398,
         :coverpage => true,
         :round => true,
         :ignore_dups => true,
      },
      {  # OUKA: http://ir.library.osaka-u.ac.jp/meta-bin/mt-pdetail.cgi?smode=1&edm=0&tlang=1&cd=00042077
         :url => "http://ir.library.osaka-u.ac.jp/metadb/up/HABARA01/oqd_17_47.pdf",
      },
      {	 # OUKA: http://ir.library.osaka-u.ac.jp/meta-bin/mt-pdetail.cgi?smode=1&edm=0&tlang=1&cd=00042206
         :url => "http://ir.library.osaka-u.ac.jp/metadb/up/HIRAO01/rsi64_11_3198.pdf",
         :dpi => 300,
         :coverpage => true,
      }
   ]

   include Util
   def test_pdf_image_check_render
      basedir = "."
      basedir = ".." unless File.exist?( JARFILE )
      jarfile = File.join( basedir, JARFILE )
      TEST_PDF_LIST.each do |test|
         p test[ :url ]
         pdf = PdfFile.new( test[ :url ], basedir )
         pin, pout, perr = Open3.popen3( "java", "-jar", jarfile, pdf.filename )
         stdout_result = pout.readlines
         stderr_result = perr.readlines
         assert( stderr_result.size == 0, "Error output for #{ test[:url] }." )
         done = Hash.new( {} );

         if test[ :dpi ]
            stdout_result.each do |line|
               data = line.chomp.split( /\t/ )
               if data[0] =~ /\Apage\d+\Z/ and data[1] =~ /\Adpi/
                  # p data
                  next if data[0] == "page1" and test[ :coverpage ]
                  next if test[ :ignore_dups ] and done[ data[0] ][ data[1] ]
		  dpi = data[2].to_i
		  dpi = data[2].to_f.ceil if test[ :ceil ]
		  dpi = data[2].to_f.round if test[ :round ]
		  next if dpi == 0
                  assert_equal( test[:dpi], dpi, "DPI for #{ test[:url] } should be #{ test[:dpi] } (#{ data[2] })." )
                  done[ data[0] ] ||= {}
                  done[ data[0] ][ data[1] ] = true
               end
            end
         end
      end
   end
end
