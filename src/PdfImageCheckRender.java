import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
 
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.BadElementException;

public class PdfImageCheckRender implements RenderListener {
    private final Rectangle pagesize;
    private final StringBuffer result = new StringBuffer();;

    public String getResultantText() {
	return result.toString();
    }
    
    /**
     * Creates a RenderListener that will look for images.
     */
    public PdfImageCheckRender( Rectangle pagesize ) {
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
            PdfName filter = (PdfName)img_obj.get(PdfName.FILTER);
	    Image image = Image.getInstance( img_obj.getImageAsBytes() );
	    if ( image.getDpiX() > 0 && image.getDpiX() > 0 ) {
		System.out.println( "DPI-X:\t" + image.getDpiX() );
		System.out.println( "DPI-Y:\t" + image.getDpiY() );
	    } else {
		//System.out.println( "Plain width:\t" + image.getPlainWidth() );
		//System.out.println( "Plain height:\t" + image.getPlainHeight() );	
		//System.out.println( "Scaled width:\t" + image.getScaledWidth() );
		//System.out.println( "Scaled height:\t" + image.getScaledHeight() );
		System.out.println( "DPI-X:\t" + (int)( image.getScaledWidth() / pagesize.getWidth() * 72f ) );
		System.out.println( "DPI-Y:\t" + (int)( image.getScaledHeight() / pagesize.getHeight() * 72f ) );
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
}
 
