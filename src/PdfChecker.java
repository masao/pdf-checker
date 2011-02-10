import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class PdfChecker {
    private static void printHeader( int num ) {
    	System.out.print( "filename\tPDF version\tNumber of pages\tEncryption\t" );
	for ( int i = 0; i < num; i++ ) {
	    System.out.print( "Page (" + (i+1) + ")\t" );
	    System.out.print( "imagetype\tdpix\tdpiy\textsize\t" );
	}
    	System.out.println( "" );
    }
    public static void check( String filename ) throws IOException {
    	PdfReader pdf = new PdfReader( filename );
    	ArrayList result = new ArrayList();
	result.add( filename );
	result.add( pdf.getPdfVersion() );
	result.add( pdf.getNumberOfPages() );
	result.add( pdf.isEncrypted() );
	PdfReaderContentParser parser = new PdfReaderContentParser( pdf );
        for (int i = 1; i <= pdf.getNumberOfPages(); i++) {
	    result.add( pdf.getPageSize( i ) );
	    //System.out.println( "Page size (" + i + "):\t" + pdf.getPageSize( i ) );
	    PdfImageCheckRender listener = new PdfImageCheckRender( pdf.getPageSize(i) );
            parser.processContent(i, listener);
	    //System.out.println( "Text length:\t" + listener.getResultantText().length() );
	    result.addAll( listener.getPageInfo() );
	    result.add( listener.getResultantText().length() );
        }
	Iterator i = result.iterator();
	while( true ) {
	    System.out.print( i.next().toString() );
	    if ( i.hasNext() ) {
		System.out.print( "\t" );
	    } else {
		System.out.println( "" );
		break;
	    }
	}
    }
    public static void main( String[] args ) throws IOException {
	printHeader( 3 );
	for (int i = 0; i < args.length; i++ ) {
	    //System.out.println( "Filename:\t" + args[i] );
	    check( args[i] );
	}
    }
}
