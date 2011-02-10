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
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.BadElementException;

public class PdfImageCheckRender implements RenderListener {
    private Rectangle pagesize;
    private StringBuffer result = new StringBuffer();
    private ArrayList pageinfo =  new ArrayList();

    public String getResultantText() {
	return result.toString();
    }
    public ArrayList getPageInfo() {
	return pageinfo;
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
            //PdfName filter = (PdfName)img_obj.get(PdfName.FILTER);
	    //System.out.println( "Image filter:\t" + filter );
	    //System.out.println( "Image dictionary:\t" + img_obj.getDictionary() );
// 	    Set keys = img_obj.getDictionary().getKeys();
// 	    Iterator i = keys.iterator();
// 	    while( i.hasNext() ) {
// 		System.out.println( i.next().toString() );
// 	    }
	    //int width = img_obj.getDictionary().getAsNumber( PdfName.WIDTH ).intValue();
	    //int height = img_obj.getDictionary().getAsNumber( PdfName.HEIGHT ).intValue();
	    //System.out.println( width + "x" + height );
	    Image image = Image.getInstance( img_obj.getImageAsBytes() );
	    pageinfo.add( img_obj.getFileType() );
	    //System.out.print( "Image filetype:\t" + img_obj.getFileType() );
	    if ( image.getDpiX() > 0 && image.getDpiX() > 0 ) {
		pageinfo.add( image.getDpiX() );
		pageinfo.add( image.getDpiY() );
		//System.out.println( "DPI-X:\t" + image.getDpiX() );
		//System.out.println( "DPI-Y:\t" + image.getDpiY() );
	    } else {
		pageinfo.add( image.getScaledWidth() / pagesize.getWidth() * 72f );
		pageinfo.add( image.getScaledHeight() / pagesize.getHeight() * 72f );
		//System.out.println( "DPI-X:\t" + (int)( image.getScaledWidth() / pagesize.getWidth() * 72f ) );
		//System.out.println( "DPI-Y:\t" + (int)( image.getScaledHeight() / pagesize.getHeight() * 72f ) );
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
