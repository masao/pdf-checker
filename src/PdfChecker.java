import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class PdfChecker {
    public static void main( String[] args ) throws IOException {
	for (int i = 0; i < args.length; i++ ) {
	    check( args[i], args.length );
	}
    }

    public static void check( String filename, int argc ) throws IOException {
    	PdfReader pdf = new PdfReader( filename );
	if ( argc == 1 ) {
	    output( null, "filename", filename );
	    filename = null;
	}
	output( filename, "version", pdf.getPdfVersion() );
	output( filename, "encryption", pdf.isEncrypted() );
	HashMap map = pdf.getInfo();
	Iterator ite = map.keySet().iterator();
	while( ite.hasNext() ) {
	    String prop = (String) ite.next();
	    output( filename, prop.toLowerCase(), map.get( prop ) );
	}
	PdfReaderContentParser parser = new PdfReaderContentParser( pdf );
        for (int i = 1; i <= pdf.getNumberOfPages(); i++) {
	    output( filename, "page"+i, "pagesize", pdf.getPageSize( i ) );
	    PdfImageCheckRender listener = new PdfImageCheckRender( filename, i, pdf.getPageSize(i) );
            parser.processContent(i, listener);
	    output( filename, "page"+i, "text length", listener.getResultantText().length() );
        }
    }

    static void output( String filename, String name, Object value ) {
	StringBuffer buf = new StringBuffer();
	if ( filename != null ) {
	    buf.append( filename + "\t" );
	}
	buf.append( name + "\t" + value );
	System.out.println( buf );
    }
    public static void output( String filename, String name, char value ) {
	output( filename, name, String.valueOf( value ) );
    }
    public static void output( String filename, String name, int value ) {
	output( filename, name, String.valueOf( value ) );
    }
    public static void output( String filename, String name, float value ) {
	output( filename, name, String.valueOf( value ) );
    }
    public static void output( String filename, String name, boolean value ) {
	output( filename, name, String.valueOf( value ) );
    }
    public static void output( String filename, String name1, String name2, Object value ) {
	output( filename, name1 + "\t" + name2, value );
    }
    public static void output( String filename, String name1, String name2, int value ) {
	output( filename, name1 + "\t" + name2, value );
    }
    public static void output( String filename, String name1, String name2, float value ) {
	output( filename, name1 + "\t" + name2, value );
    }
}
