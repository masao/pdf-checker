import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
 
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.BadElementException;

public class PdfImageCheckRender implements RenderListener {
    private String filename;
    private int page;
    private Rectangle pagesize;
    private StringBuffer result = new StringBuffer();

    public String getResultantText() {
	return result.toString();
    }

    /**
     * Creates a RenderListener that will look for images.
     */
    public PdfImageCheckRender( String filename, int page, Rectangle pagesize ) {
	this.filename = filename;
	this.page     = page;
	this.pagesize = pagesize;
    }
 
    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
     */
    public void beginTextBlock() {
    }
 
    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
     */
    public void endTextBlock() {
    }
 
    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(
     *     com.itextpdf.text.pdf.parser.ImageRenderInfo)
     */
    public void renderImage(ImageRenderInfo renderInfo) {
        try {
            PdfImageObject img_obj = renderInfo.getImage();
	    PdfDictionary img_dict = img_obj.getDictionary();
            PdfName filter = img_dict.getAsName(PdfName.FILTER);
	    //System.out.println( "Image filter:\t" + filter );
	    //System.out.println( "Image dictionary:\t" + img_dict );
	    //Set keys = img_dict.getKeys();
 	    //Iterator i = keys.iterator();
 	    //while( i.hasNext() ) {
	    //    PdfName key = (PdfName) i.next();
	    //    System.out.println( key.toString() + ":\t" + img_dict.get(key) );
	    //}
	    int width = img_dict.getAsNumber( PdfName.WIDTH ).intValue();
	    int height = img_dict.getAsNumber( PdfName.HEIGHT ).intValue();
	    byte[] bytes = null;
	    if ( filter == PdfName.JBIG2DECODE ) {
		outputWidthHeight( width, height );
	    } else {
		bytes = img_obj.getImageAsBytes();
		if ( bytes == null ) {
		    outputWidthHeight( width, height );
		} else {
		    Image image = Image.getInstance( img_obj.getImageAsBytes() );
		    //PdfDictionary dic = image.getAdditional();
		    //if (dic != null) {
		    //    System.out.println( "Image dictionary: " + dic + "\t" + dic.size() );
		    //    Set k = dic.getKeys();
		    //    Iterator j = k.iterator();
		    //    while( j.hasNext() ) {
		    //        System.out.println( j.next() );
		    //    }
		    //}
		    //System.out.println( "area: " + renderInfo.getArea() );
		    //System.out.println( "ctm: " + renderInfo.getImageCTM() );
		    //System.out.println( "size:" + image.getWidth() + "x" + image.getHeight() +"\tgetWidthPercentage:" + image.getWidthPercentage() );
		    //System.out.println( "scaled width:" + image.getScaledWidth() );
		    //System.out.println( "scaled height:" + image.getScaledHeight() );
		    //System.out.println( "pagesize:" + pagesize.getWidth() + "x" + pagesize.getHeight() + "\t" + image.getAbsoluteX() );
		    PdfChecker.output( filename, "page"+page, "imagetype", img_obj.getFileType() );
		    if ( image.getDpiX() > 0 && image.getDpiY() > 0 ) {
			PdfChecker.output( filename, "page"+page, "dpi-x", image.getDpiX() );
			PdfChecker.output( filename, "page"+page, "dpi-y", image.getDpiY() );
		    } else {
			Matrix ctm = renderInfo.getImageCTM();
			float widthScale = ctm.get( Matrix.I11 );
			float heightScale = ctm.get( Matrix.I22 );
			PdfChecker.output( filename, "page"+page, "dpi-x", image.getScaledWidth() / widthScale * 72f );
			PdfChecker.output( filename, "page"+page, "dpi-y", image.getScaledHeight() / heightScale * 72f );
		    }
		}
	    }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
	}
    }
 
    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(
     *     com.itextpdf.text.pdf.parser.TextRenderInfo)
     */
    public void renderText(TextRenderInfo renderInfo) {
	String content_str = renderInfo.getText();
	//System.out.println( "Text length:\t" + content_str.length() );
	result.append( content_str );
    }

    private void outputWidthHeight( int width, int height ) {
	//System.out.println( width + "x" + height );
	if ( width > 0 && height > 0 ) {
	    PdfChecker.output( filename, "page"+page, "dpi-x", width / pagesize.getWidth() * 72f );
	    PdfChecker.output( filename, "page"+page, "dpi-y", height / pagesize.getHeight() * 72f );
	} else {
	    if ( filename != null ) {
	    	System.err.print( filename + "\t" );
	    }
	    System.err.println( "image error (" + page + ")\tinvalid width or height: " + width + "x" + height );
	}
    }
}
