import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class PdfChecker {
    public static void check( String filename ) throws IOException {
    	PdfReader pdf = new PdfReader( filename );
	System.out.println( filename + "\tversion\t" + pdf.getPdfVersion() );
	System.out.println( filename + "\tpages\t" + pdf.getNumberOfPages() );
	System.out.println( filename + "\tencryption\t" + pdf.isEncrypted() );
	HashMap map = pdf.getInfo();
	Iterator ite = map.keySet().iterator();
	while( ite.hasNext() ) {
	    String prop = (String) ite.next();
	    System.out.println( filename + "\t" + prop.toLowerCase() + "\t" + map.get( prop ) );
	}
	PdfReaderContentParser parser = new PdfReaderContentParser( pdf );
        for (int i = 1; i <= pdf.getNumberOfPages(); i++) {
	    System.out.println( filename + "\tpagesize (" + i + ")\t" + pdf.getPageSize( i ) );
	    PdfImageCheckRender listener = new PdfImageCheckRender( filename, i, pdf.getPageSize(i) );
            parser.processContent(i, listener);
	    System.out.println( filename + "\ttext length\t" + listener.getResultantText().length() );
        }
    }
    public static void main( String[] args ) throws IOException {
	for (int i = 0; i < args.length; i++ ) {
	    check( args[i] );
	}
    }
}
