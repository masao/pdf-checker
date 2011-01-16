import java.io.IOException;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class PdfChecker {
    public static void check( PdfReader pdf ) throws IOException {
	System.out.println( "PDF version:\t"     + pdf.getPdfVersion() );
	System.out.println( "Number of pages:\t" + pdf.getNumberOfPages() );
	System.out.println( "Encryption:\t"      + pdf.isEncrypted() );
	PdfReaderContentParser parser = new PdfReaderContentParser( pdf );
        for (int i = 1; i <= pdf.getNumberOfPages(); i++) {
	    System.out.println( "Page size (" + i + "):\t" + pdf.getPageSize( i ) );
	    PdfImageCheckRender listener = new PdfImageCheckRender( pdf.getPageSize(i) );
            parser.processContent(i, listener);
	    System.out.println( "Text length:\t" + listener.getResultantText().length() );
        }
    }
    public static void main( String[] argv ) throws IOException {
	for (int i = 0; i < argv.length; i++ ) {
	    System.out.println( "Filename:\t" + argv[i] );
	    check( new PdfReader( argv[i] ) );
	}
    }
}
