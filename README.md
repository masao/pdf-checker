PDF check utitility
===================

**Pdf-Checker** allows you to check the properties/contents of any PDF files in a batch mode. This currently checks the following properties of PDF files:

* PDF version
* Number of pages
* Permission settings of PDF (copy, modification, printing, and/or accessibility
* (On each page):
  * Filetype of embbed images
  * DPI (dot-per-inch) resolution of embbed images within a page
  * Number of characters (text length) within a page
  
How to use
----------

Download the binary package and unpack it. And then run the jar file with specifying the targeted PDF files on command line, as follows:

<pre>
  % unzip pdf-checker-YYYYMMDD.zip
  % java -jar PdfChecker.jar pdf/2010J00*.pdf
  pdf/2010J0001.pdf	version	3
  pdf/2010J0001.pdf	encryption	false
  pdf/2010J0001.pdf	creationdate	D:20060627211618
  pdf/2010J0001.pdf	producer	PDFlib 4.0.3 + PDI (SunOS 5.8)
  pdf/2010J0001.pdf	pages	8
  pdf/2010J0001.pdf	page1	pagesize	Rectangle: 595.0x842.0 (rot: 0 degrees)
  pdf/2010J0001.pdf	page1	imagetype	png
  pdf/2010J0001.pdf	page1	dpi-x	346.8101
  pdf/2010J0001.pdf	page1	dpi-y	346.06174
  pdf/2010J0001.pdf	page1	text length	83
  pdf/2010J0001.pdf	page2	pagesize	Rectangle: 595.0x842.0 (rot: 0 degrees)
  pdf/2010J0001.pdf	page2	imagetype	png
  pdf/2010J0001.pdf	page2	dpi-x	346.8101
  pdf/2010J0001.pdf	page2	dpi-y	346.06174
  pdf/2010J0001.pdf	page2	text length	87
  pdf/2010J0001.pdf	page3	pagesize	Rectangle: 595.0x842.0 (rot: 0 degrees)
  pdf/2010J0001.pdf	page3	imagetype	png
  pdf/2010J0001.pdf	page3	dpi-x	346.8101
  pdf/2010J0001.pdf	page3	dpi-y	346.06174
  pdf/2010J0001.pdf	page3	text length	0
  pdf/2010J0001.pdf	page4	pagesize	Rectangle: 595.0x842.0 (rot: 0 degrees)
  pdf/2010J0001.pdf	page4	imagetype	png
  pdf/2010J0001.pdf	page4	dpi-x	346.8101
  pdf/2010J0001.pdf	page4	dpi-y	346.06174
  pdf/2010J0001.pdf	page4	text length	1794
  .....
</pre>

An example of output above shows that the parsed file is PDF version 3, not encrypted, created at June 27th 2006, produced by a tool called PDFLib, and it contains 8 pages. Each page of the file has a size of "595x842" without rotation, an embedded (roughly) 300 DPI resolution image with PNG-style compression, and its textual contents are 80-1800 characters.

The tool can check multiple files at a time by specifying them as arguments. When specifying multiple files, the first column shows each filename.

As the output format is a simple text file with tab-separated, you can read and analyze the results via other applications like Excel.

Links
-----

Pdf-Checker uses and bundles iText PDF Library and Legion of the Bouncy Castle Java cryptography APIs. Source codes and detailed information is available under:

* iText: <http://itextpdf.com/>
* Bouncy Castle Crypto APIs: <http://www.bouncycastle.org/java.html>
